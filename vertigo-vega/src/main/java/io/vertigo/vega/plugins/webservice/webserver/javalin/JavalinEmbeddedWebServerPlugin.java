/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, Vertigo.io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.vega.plugins.webservice.webserver.javalin;

import java.util.Optional;

import javax.inject.Inject;

import io.javalin.Javalin;
import io.vertigo.core.param.ParamValue;

/**
 * RoutesRegisterPlugin use to register Javalin route.
 * @author npiedeloup
 */
public final class JavalinEmbeddedWebServerPlugin extends AbstractJavalinWebServerPlugin {

	private final int port;
	private final Javalin javalinApp;

	/**
	 * @param apiPrefix globale api prefix
	 * @param port Server port
	 */
	@Inject
	public JavalinEmbeddedWebServerPlugin(
			@ParamValue("apiPrefix") final Optional<String> apiPrefix,
			@ParamValue("port") final int port) {
		super(apiPrefix);
		this.port = port;
		//---
		final String tempDir = System.getProperty("java.io.tmpdir");
		javalinApp = Javalin.create()
				.before(new JettyMultipartConfig(tempDir))
				.after(new JettyMultipartCleaner());

	}

	@Override
	protected Javalin startJavalin() {
		return javalinApp.start(port);
	}

	@Override
	protected void stopJavalin(final Javalin javalinApp) {
		javalinApp.stop();
	}
}
