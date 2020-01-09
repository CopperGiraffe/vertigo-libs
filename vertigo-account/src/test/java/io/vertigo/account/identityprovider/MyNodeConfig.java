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
package io.vertigo.account.identityprovider;

import io.vertigo.account.AccountFeatures;
import io.vertigo.account.data.TestUserSession;
import io.vertigo.account.identityprovider.model.DtDefinitions;
import io.vertigo.commons.CommonsFeatures;
import io.vertigo.connectors.ldap.LdapFeatures;
import io.vertigo.connectors.redis.RedisFeatures;
import io.vertigo.core.node.config.DefinitionProviderConfig;
import io.vertigo.core.node.config.ModuleConfig;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.node.config.NodeConfigBuilder;
import io.vertigo.core.param.Param;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.database.DatabaseFeatures;
import io.vertigo.database.impl.sql.vendor.h2.H2DataBase;
import io.vertigo.dynamo.DataStoreFeatures;
import io.vertigo.dynamo.DataModelFeatures;
import io.vertigo.dynamo.plugins.environment.ModelDefinitionProvider;

public final class MyNodeConfig {
	private static final String REDIS_HOST = "redis-pic.part.klee.lan.net";
	private static final int REDIS_PORT = 6379;
	private static final int REDIS_DATABASE = 15;

	enum IdpPlugin {
		ldap, text, store
	}

	public static NodeConfig config(final IdpPlugin idpPlugin, final boolean redis) {
		final NodeConfigBuilder nodeConfigBuilder = NodeConfig.builder()
				.beginBoot()
				.withLocales("fr")
				.addPlugin(ClassPathResourceResolverPlugin.class)
				.endBoot();

		if (redis) {
			nodeConfigBuilder.addModule(new RedisFeatures()
					.withJedis(
							Param.of("host", REDIS_HOST),
							Param.of("port", Integer.toString(REDIS_PORT)),
							Param.of("database", Integer.toString(REDIS_DATABASE)))
					.build());
		}

		final CommonsFeatures commonsFeatures = new CommonsFeatures()
				.withScript()
				.withJaninoScript()
				.withCache()
				.withMemoryCache();

		final DatabaseFeatures databaseFeatures = new DatabaseFeatures();
		final DataStoreFeatures dynamoFeatures = new DataStoreFeatures();
		final AccountFeatures accountFeatures = new AccountFeatures()
				.withSecurity(Param.of("userSessionClassName", TestUserSession.class.getName()))
				.withAccount()
				.withIdentityProvider();

		if (redis) {
			accountFeatures
					.withRedisAccountCache();
		}
		accountFeatures
				.withTextAccount(
						Param.of("accountFilePath", "io/vertigo/account/data/identities.txt"),
						Param.of("accountFilePattern", "^(?<id>[^;]+);(?<displayName>[^;]+);(?<email>(?<authToken>[^;@]+)@[^;]+);(?<photoUrl>.*)$"),
						Param.of("groupFilePath", "io/vertigo/account/data/groups.txt"),
						Param.of("groupFilePattern", "^(?<id>[^;]+);(?<displayName>[^;]+);(?<accountIds>.*)$"));

		if (idpPlugin == IdpPlugin.ldap) {
			nodeConfigBuilder.addModule(new LdapFeatures().withLdap(
					Param.of("host", "docker-vertigo.part.klee.lan.net"),
					Param.of("port", "389"),
					Param.of("readerLogin", "cn=admin,dc=vertigo,dc=io"),
					Param.of("readerPassword", "v3rt1g0")).build());

			accountFeatures.withLdapIdentityProvider(
					Param.of("ldapAccountBaseDn", "dc=vertigo,dc=io"),
					Param.of("ldapUserAuthAttribute", "cn"),
					Param.of("userIdentityEntity", "DtUser"),
					Param.of("ldapUserAttributeMapping", "usrId:cn, fullName:description, photo:jpegPhoto"));
		} else if (idpPlugin == IdpPlugin.text) {
			accountFeatures.withTextIdentityProvider(
					Param.of("identityFilePath", "io/vertigo/account/data/identities.txt"),
					Param.of("identityFilePattern", "^(?<usrId>[^;]+);(?<fullName>[^;]+);(?<email>(?<authToken>[^;@]+)@[^;]+);(?<photoUrl>.*)$"),
					Param.of("userAuthField", "email"),
					Param.of("userIdentityEntity", "DtUser"));
		} else if (idpPlugin == IdpPlugin.store) {
			databaseFeatures
					.withSqlDataBase()
					.withC3p0(
							Param.of("dataBaseClass", H2DataBase.class.getName()),
							Param.of("jdbcDriver", "org.h2.Driver"),
							Param.of("jdbcUrl", "jdbc:h2:mem:database"));
			dynamoFeatures
					.withStore()
					.withSqlStore();
			accountFeatures.withStoreIdentityProvider(
					Param.of("userIdentityEntity", "DtUser"),
					Param.of("userAuthField", "email"));
		}

		return nodeConfigBuilder
				.addModule(commonsFeatures.build())
				.addModule(databaseFeatures.build())
				.addModule(new DataModelFeatures().build())
				.addModule(dynamoFeatures.build())
				.addModule(accountFeatures.build())
				.addModule(ModuleConfig.builder("app")
						.addDefinitionProvider(
								DefinitionProviderConfig.builder(ModelDefinitionProvider.class)
										.addDefinitionResource("classes", DtDefinitions.class.getName())
										.addDefinitionResource("kpr", "account/domains.kpr")
										.build())
						.build())
				.build();
	}
}
