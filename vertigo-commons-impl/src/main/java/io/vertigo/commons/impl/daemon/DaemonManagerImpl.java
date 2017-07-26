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
package io.vertigo.commons.impl.daemon;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import io.vertigo.app.Home;
import io.vertigo.commons.daemon.Daemon;
import io.vertigo.commons.daemon.DaemonDefinition;
import io.vertigo.commons.daemon.DaemonManager;
import io.vertigo.commons.daemon.DaemonScheduled;
import io.vertigo.commons.daemon.DaemonStat;
import io.vertigo.core.component.Activeable;
import io.vertigo.core.component.AopPlugin;
import io.vertigo.core.component.Component;
import io.vertigo.core.definition.Definition;
import io.vertigo.core.definition.DefinitionSpace;
import io.vertigo.core.definition.SimpleDefinitionProvider;
import io.vertigo.lang.Assertion;
import io.vertigo.util.ClassUtil;

/**
 * Manager of all the daemons.
 *
 * @author TINGARGIOLA
 */
public final class DaemonManagerImpl implements DaemonManager, Activeable, SimpleDefinitionProvider {

	private final DaemonExecutor daemonExecutor = new DaemonExecutor();

	/**
	 * Construct an instance of DaemonManagerImpl.
	 */
	@Inject
	public DaemonManagerImpl() {
		Home.getApp().registerPreActivateFunction(this::startAllDaemons);

	}

	@Override
	public List<? extends Definition> provideDefinitions(final DefinitionSpace definitionSpace) {
		// we need to unwrap the component to scan the real class and not the enhanced version
		final AopPlugin aopPlugin = Home.getApp().getConfig().getBootConfig().getAopPlugin();
		return Home.getApp().getComponentSpace().keySet()
				.stream()
				.map(id -> aopPlugin.unwrap(Home.getApp().getComponentSpace().resolve(id, Component.class)))
				.flatMap(component -> createDaemonDefinitions(component).stream())
				.collect(Collectors.toList());
	}

	private static List<DaemonDefinition> createDaemonDefinitions(final Component component) {
		return Stream.of(component.getClass().getMethods())
				.filter(method -> method.isAnnotationPresent(DaemonScheduled.class))
				.map(
						method -> {
							Assertion.checkState(method.getParameterTypes().length == 0, "Method {0} on component {1} cannot have any parameter to be used as a daemon", method.getName(), component.getClass().getName());
							//---
							final DaemonScheduled daemonSchedule = method.getAnnotation(DaemonScheduled.class);
							return new DaemonDefinition(
									daemonSchedule.name(),
									() -> () -> ClassUtil.invoke(component, method), daemonSchedule.periodInSeconds());
						})
				.collect(Collectors.toList());

	}

	/** {@inheritDoc} */
	@Override
	public List<DaemonStat> getStats() {
		return daemonExecutor.getStats();
	}

	/** {@inheritDoc} */
	@Override
	public void start() {
		daemonExecutor.start();
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		daemonExecutor.stop();
	}

	/**
	 * Démarre un démon.
	 * Celui-ci aura été préalablement enregistré.
	 * Il sera lancé puis réexécuté périodiquement.
	 * L'instance du démon est créée par injection de dépendances.
	 *
	 * @param daemonDefinition Le démon à lancer.
	 */
	private void startDaemon(final DaemonDefinition daemonDefinition) {
		Assertion.checkNotNull(daemonDefinition);
		// -----
		final Daemon daemon = createDaemon(daemonDefinition);
		daemonExecutor.scheduleDaemon(daemonDefinition, daemon);
	}

	/**
	 * @param daemonDefinition
	 * @return Daemon
	 */
	private static Daemon createDaemon(final DaemonDefinition daemonDefinition) {
		return daemonDefinition.getDaemonSupplier().get();
	}

	/**
	 * Démarre l'ensemble des démons préalablement enregistré dans le spaceDefinition.
	 */
	private void startAllDaemons() {
		Home.getApp().getDefinitionSpace().getAll(DaemonDefinition.class).stream()
				.forEach(this::startDaemon);
	}

}
