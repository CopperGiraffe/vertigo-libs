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
package io.vertigo.stella.plugins.work.rest.worker;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.lang.Assertion;
import io.vertigo.stella.impl.node.WorkDispatcherConfUtil;
import io.vertigo.stella.impl.node.WorkerPlugin;
import io.vertigo.stella.impl.work.WorkItem;
import io.vertigo.stella.work.WorkManager;

/**
 * Implémentation de DistributedWorkManager, pour l'execution de travaux par des Workers distant.
 * Cette implémentation représente la partie client qui se déploie en ferme.
 * 1- contacte la partie serveur pour récupérer les travaux qu'elle sait gérer,
 * 2- execute la tache en synchrone exclusivement
 * 3- retourne le résultat au serveur
 *
 * @author npiedeloup, pchretien
 */
public final class RestWorkerPlugin implements WorkerPlugin {
	private final Map<String, Integer> workTypes;
	private final RestQueueClient restQueueClient; //devrait etre un plugin

	/**
	 * Constructeur.
	 * @param nodeId Identifiant du noeud
	 * @param workTypes Types de travail gérés
	 * @param serverUrl Url du serveur
	 * @param timeoutSeconds Timeout en seconde des connections vers le serveur (doit être > au timeoutSeconds du serveur)
	 * @param workManager Manager des works
	 * @param codecManager Manager d'encodage/decodage
	 */
	@Inject
	public RestWorkerPlugin(
			@Named("nodeId") final String nodeId,
			@Named("workTypes") final String workTypes,
			@Named("serverUrl") final String serverUrl,
			@Named("timeoutSeconds") final int timeoutSeconds,
			final WorkManager workManager,
			final CodecManager codecManager) {
		Assertion.checkArgNotEmpty(workTypes);
		Assertion.checkArgNotEmpty(serverUrl);
		Assertion.checkArgument(timeoutSeconds < 10000, "Le timeout s'exprime en seconde.");
		//-----
		this.workTypes = WorkDispatcherConfUtil.readWorkTypeConf(workTypes);
		restQueueClient = new RestQueueClient(nodeId, serverUrl + "/backend/workQueue", timeoutSeconds, codecManager);
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Integer> getWorkTypes() {
		return workTypes;
	}

	/** {@inheritDoc} */
	@Override
	public <WR, W> WorkItem<WR, W> pollWorkItem(final String workType) {
		return restQueueClient.pollWorkItem(workType);
	}

	/** {@inheritDoc} */
	@Override
	public <R> void putResult(final String workId, final R result, final Throwable error) {
		restQueueClient.putResult(workId, result, error);
	}

	/** {@inheritDoc} */
	@Override
	public void putStart(final String workId) {
		restQueueClient.putStart(workId);
	}
}
