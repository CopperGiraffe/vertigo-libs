/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.stella.impl.work;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import io.vertigo.lang.Activeable;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;
import io.vertigo.stella.impl.work.listener.WorkListener;
import io.vertigo.stella.impl.work.listener.WorkListenerImpl;
import io.vertigo.stella.impl.work.worker.Coordinator;
import io.vertigo.stella.impl.work.worker.distributed.DistributedCoordinator;
import io.vertigo.stella.impl.work.worker.local.LocalCoordinator;
import io.vertigo.stella.work.WorkEngine;
import io.vertigo.stella.work.WorkEngineProvider;
import io.vertigo.stella.work.WorkManager;
import io.vertigo.stella.work.WorkProcessor;
import io.vertigo.stella.work.WorkResultHandler;

/**
 * Implémentation de workManager.
 *
 * @author pchretien, npiedeloup
 */
public final class WorkManagerImpl implements WorkManager, Activeable {

	private final WorkListener workListener;
	//There is always ONE LocalWorker, but distributedWorker is optionnal
	private final LocalCoordinator localCoordinator;
	private final Optional<DistributedCoordinator> distributedCoordinator;

	/**
	 * Constructeur.
	 * @param workerCount Nb workers
	 * @param masterPlugin Optional plugin for work's distribution
	 */
	@Inject
	public WorkManagerImpl(
			final @Named("workerCount") int workerCount,
			final Optional<MasterPlugin> masterPlugin) {
		Assertion.checkNotNull(masterPlugin);
		//-----
		workListener = new WorkListenerImpl(/*analyticsManager*/);
		localCoordinator = new LocalCoordinator(workerCount);
		distributedCoordinator = masterPlugin.isPresent() ? Optional.of(new DistributedCoordinator(masterPlugin.get())) : Optional.<DistributedCoordinator> empty();
	}

	/** {@inheritDoc} */
	@Override
	public void start() {
		//coordinator n'étant pas un plugin
		//il faut le démarrer et l'arréter explicitement.
		if (distributedCoordinator.isPresent()) {
			distributedCoordinator.get().start();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		if (distributedCoordinator.isPresent()) {
			distributedCoordinator.get().stop();
		}
		localCoordinator.close();
	}

	private static String createWorkId() {
		return UUID.randomUUID().toString();
	}

	/** {@inheritDoc} */
	@Override
	public <R, W> WorkProcessor<R, W> createProcessor(final WorkEngineProvider<R, W> workEngineProvider) {
		return new WorkProcessorImpl<>(this, workEngineProvider);
	}

	/** {@inheritDoc} */
	@Override
	public <R, W> R process(final W work, final WorkEngineProvider<R, W> workEngineProvider) {
		Assertion.checkNotNull(work);
		Assertion.checkNotNull(workEngineProvider);
		//-----
		final WorkItem<R, W> workItem = new WorkItem<>(createWorkId(), work, workEngineProvider);
		final Future<R> result = submit(workItem, Optional.<WorkResultHandler<R>> empty());
		try {
			return result.get();
		} catch (final ExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			}
			throw WrappedException.wrap(e.getCause());
		} catch (final InterruptedException e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public <R, W> void schedule(final W work, final WorkEngineProvider<R, W> workEngineProvider, final WorkResultHandler<R> workResultHandler) {
		Assertion.checkNotNull(work);
		Assertion.checkNotNull(workEngineProvider);
		Assertion.checkNotNull(workResultHandler);
		//-----
		final WorkItem<R, W> workItem = new WorkItem<>(createWorkId(), work, workEngineProvider);
		submit(workItem, Optional.of(workResultHandler));
	}

	@Override
	public <R> void schedule(final Callable<R> callable, final WorkResultHandler<R> workResultHandler) {
		Assertion.checkNotNull(callable);
		Assertion.checkNotNull(workResultHandler);
		//-----
		final WorkEngineProvider<R, Void> workEngineProvider = new WorkEngineProvider<>(new CallableWorkEngine<>(callable));

		final WorkItem<R, Void> workItem = new WorkItem<>(createWorkId(), null, workEngineProvider);
		submit(workItem, Optional.of(workResultHandler));
	}

	private <R, W> Future<R> submit(final WorkItem<R, W> workItem, final Optional<WorkResultHandler<R>> workResultHandler) {
		final Coordinator coordinator = resolveCoordinator(workItem);
		//---
		workListener.onStart(workItem.getWorkType());
		boolean executed = false;
		final long start = System.currentTimeMillis();
		try {
			final Future<R> future = coordinator.submit(workItem, workResultHandler);
			executed = true;
			return future;
		} finally {
			workListener.onFinish(workItem.getWorkType(), System.currentTimeMillis() - start, executed);
		}
	}

	private <R, W> Coordinator resolveCoordinator(final WorkItem<R, W> workItem) {
		Assertion.checkNotNull(workItem);
		//-----
		/*
		 * On recherche un Worker capable d'effectuer le travail demandé.
		 * 1- On recherche parmi les works externes
		 * 2- Si le travail n'est pas déclaré comme étant distribué on l'exécute localement
		 */
		if (distributedCoordinator.isPresent() && distributedCoordinator.get().accept(workItem)) {
			return distributedCoordinator.get();
		}
		return localCoordinator;
	}

	private static final class CallableWorkEngine<R> implements WorkEngine<R, Void> {
		private final Callable<R> callable;

		CallableWorkEngine(final Callable<R> callable) {
			this.callable = callable;
		}

		/** {@inheritDoc} */
		@Override
		public R process(final Void dummy) {
			try {
				return callable.call();
			} catch (final Exception e) {
				throw WrappedException.wrap(e);
			}
		}
	}

}
