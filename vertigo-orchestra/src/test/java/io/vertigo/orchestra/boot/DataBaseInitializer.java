/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
/**
 *
 */
package io.vertigo.orchestra.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Collections;

import javax.inject.Inject;

import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.resource.ResourceManager;
import io.vertigo.database.sql.SqlDataBaseManager;
import io.vertigo.database.sql.connection.SqlConnection;
import io.vertigo.database.sql.statement.SqlStatement;

/**
 * Init masterdata list.
 * @author jmforhan
 */
public class DataBaseInitializer implements Component, Activeable {

	private static String ORCHESTRA_CONNECTION_NAME = "orchestra";

	@Inject
	private ResourceManager resourceManager;
	@Inject
	private SqlDataBaseManager sqlDataBaseManager;

	/** {@inheritDoc} */
	@Override
	public void start() {
		createDataBase();
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		//
	}

	private void createDataBase() {
		final SqlConnection connection = sqlDataBaseManager.getConnectionProvider(ORCHESTRA_CONNECTION_NAME).obtainConnection();
		execCallableStatement(connection, sqlDataBaseManager, "DROP ALL OBJECTS; ");
		execSqlScript(connection, "file:./src/main/database/scripts/install/orchestra_create_init_v1.0.0.sql");
		try {
			connection.commit();
			connection.release();
		} catch (final SQLException e) {
			throw WrappedException.wrap(e, "Can't release connection");
		}
	}

	private void execSqlScript(final SqlConnection connection, final String scriptPath) {
		final StringBuilder crebaseSql = new StringBuilder();
		try (final BufferedReader in = new BufferedReader(new InputStreamReader(resourceManager.resolve(scriptPath).openStream()))) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				final String adaptedInputLine = inputLine.replaceAll("-- .*", "");//removed comments
				if (!"".equals(adaptedInputLine)) {
					crebaseSql.append(adaptedInputLine).append('\n');
				}
				if (inputLine.trim().endsWith(";")) {
					execCallableStatement(connection, sqlDataBaseManager, crebaseSql.toString());
					crebaseSql.setLength(0);
				}
			}
		} catch (final IOException e) {
			throw WrappedException.wrap(e, "Can't exec script {0}", scriptPath);
		}
	}

	private static void execCallableStatement(final SqlConnection connection, final SqlDataBaseManager sqlDataBaseManager, final String sql) {
		try {
			sqlDataBaseManager.executeUpdate(SqlStatement.builder(sql).build(), Collections.emptyMap(), connection);
		} catch (final SQLException e) {
			throw WrappedException.wrap(e, "Can't exec command {0}", sql);
		}
	}

}
