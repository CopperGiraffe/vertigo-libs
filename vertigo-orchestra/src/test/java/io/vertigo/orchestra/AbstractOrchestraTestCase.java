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
package io.vertigo.orchestra;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import io.vertigo.app.AutoCloseableApp;
import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.commons.transaction.VTransactionWritable;
import io.vertigo.core.component.ComponentSpace;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamox.task.TaskEngineProc;
import io.vertigo.util.ListBuilder;

/**
 * Test Junit de Vertigo Orchestra.
 *
 * @author mlaroche.
 * @version $Id$
 */
public abstract class AbstractOrchestraTestCase {
	private static AutoCloseableApp app;

	@Inject
	private VTransactionManager transactionManager;
	@Inject
	private TaskManager taskManager;

	@BeforeAll
	public static final void setUp() throws Exception {
		app = new AutoCloseableApp(MyNodeConfig.config());
	}

	@AfterAll
	public static final void tearDown() throws Exception {
		if (app != null) {
			app.close();
		}
	}

	public final void setUpInjection() throws Exception {
		if (app != null) {
			ComponentSpace.injectMembers(this);
		}
	}

	@BeforeEach
	public void doSetUp() throws Exception {
		setUpInjection();
		//A chaque test on supprime tout
		try (VTransactionWritable transaction = transactionManager.createCurrentTransaction()) {
			final List<String> requests = new ListBuilder<String>()
					.add(" delete from o_activity_log;")
					.add(" delete from o_activity_workspace;")
					.add(" delete from o_process_planification;")
					.add(" delete from o_activity_execution;")
					.add(" delete from o_process_execution;")
					.add(" delete from o_activity;")
					.add(" delete from o_process;")
					.build();

			for (final String request : requests) {
				final TaskDefinition taskDefinition = TaskDefinition.builder("TkClean")
						.withDataSpace("orchestra")
						.withEngine(TaskEngineProc.class)
						.withRequest(request)
						.build();
				final Task task = Task.builder(taskDefinition).build();
				taskManager.execute(task);
			}
			transaction.commit();
		}

	}

}
