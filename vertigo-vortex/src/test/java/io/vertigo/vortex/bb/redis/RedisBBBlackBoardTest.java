/*
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
package io.vertigo.vortex.bb.redis;

import io.vertigo.connectors.redis.RedisFeatures;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.param.Param;
import io.vertigo.vortex.VortexFeatures;
import io.vertigo.vortex.bb.AbstractBBBlackBoardTest;

public class RedisBBBlackBoardTest extends AbstractBBBlackBoardTest {

	@Override
	protected NodeConfig buildNodeConfig() {
		return NodeConfig.builder()
				.addModule(new RedisFeatures()
						.withJedis(
								Param.of("host", "docker-vertigo.part.klee.lan.net"),
								Param.of("port", 6379),
								Param.of("database", 0))
						.build())
				.addModule(
						new VortexFeatures()
								.withBlackboard()
								.withRedisBlackboard()
								.build())
				.build();
	}

}
