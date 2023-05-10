/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.commons.app;

import io.vertigo.commons.CommonsFeatures;
import io.vertigo.commons.plugins.app.registry.redis.RedisAppNodeRegistryPlugin;
import io.vertigo.connectors.redis.RedisFeatures;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.param.Param;

public class RedisAppNodeRegistryPluginTest extends AbstractAppManagerTest {

	@Override
	protected NodeConfig buildNodeConfig() {

		final String redisHost = "docker-vertigo.part.klee.lan.net";
		final int redisPort = 6379;
		final int redisDatabase = 11;

		return buildRootNodeConfig()
				.addModule(new RedisFeatures()
						.withJedis(
								Param.of("host", redisHost),
								Param.of("port", redisPort),
								Param.of("ssl", "false"),
								Param.of("database", redisDatabase))
						.build())
				.addModule(new CommonsFeatures()
						.withNodeRegistryPlugin(RedisAppNodeRegistryPlugin.class)
						.build())
				.build();
	}

}
