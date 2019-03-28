/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.stella.plugins.work.rest.master;

import javax.inject.Inject;
import javax.inject.Named;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.lang.Assertion;
import io.vertigo.stella.impl.master.MasterPlugin;
import io.vertigo.stella.impl.master.WorkResult;
import io.vertigo.stella.impl.work.WorkItem;
import io.vertigo.vega.webservice.WebServices;
import io.vertigo.vega.webservice.stereotype.PathPrefix;

/**
 * Exécution synchrone et distante des Works avec un transfert par WS REST.
 *
 * @author npiedeloup, pchretien
 */
@PathPrefix("/backend/workQueue")
public final class RestMasterPlugin implements MasterPlugin, WebServices {
	private final RestQueueServer restQueueServer;

	/**
	 * Constructeur.
	 * @param daemonManager Manager des daemons
	 * @param timeoutSeconds Timeout des travaux en attente de traitement
	 * @param codecManager Manager d'encodage/decodage
	 */
	@Inject
	public RestMasterPlugin(
			@Named("timeoutSeconds") final int timeoutSeconds,
			final CodecManager codecManager) {
		Assertion.checkArgument(timeoutSeconds < 10000, "Le timeout s'exprime en seconde.");
		//-----
		restQueueServer = new RestQueueServer(20, codecManager, 5);
	}

	/** {@inheritDoc} */
	@Override
	public WorkResult pollResult(final int waitTimeSeconds) {
		return restQueueServer.pollResult(waitTimeSeconds);
	}

	/** {@inheritDoc} */
	@Override
	public <R, W> void putWorkItem(final WorkItem<R, W> workItem) {
		restQueueServer.putWorkItem(workItem);
	}

	/**
	 * @return RestQueueServer (use by WebService)
	 */
	RestQueueServer getRestQueueServer() {
		return restQueueServer;
	}

}
