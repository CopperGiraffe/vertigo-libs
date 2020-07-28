package io.vertigo.orchestra.monitoring.dao.uidefinitions;

import javax.inject.Inject;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.App;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.datamodel.task.metamodel.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.impl.dao.StoreServices;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class UidefinitionsPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public UidefinitionsPAO(final TaskManager taskManager) {
		Assertion.check().isNotNull(taskManager);
		//-----
		this.taskManager = taskManager;
	}

	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder 
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = App.getApp().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache StTkGetProcessByName.
	 * @param name String
	 * @return OProcessUi dtOProcessUi
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetProcessByName",
			request = "select  pro.PRO_ID as PRO_ID," +
					"        			pro.NAME as NAME," +
					"        			pro.LABEL as LABEL," +
					"        			pro.CRON_EXPRESSION as CRON_EXPRESSION," +
					"        			pro.INITIAL_PARAMS as INITIAL_PARAMS," +
					"        			pro.MULTIEXECUTION as MULTIEXECUTION," +
					"        			pro.ACTIVE as ACTIVE," +
					"        			pro.RESCUE_PERIOD as RESCUE_PERIOD," +
					"        			pro.METADATAS as METADATAS" +
					"        	from o_process pro   " +
					"        	where pro.NAME = #name# and pro.ACTIVE_VERSION is true",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessUi")
	public io.vertigo.orchestra.monitoring.domain.uidefinitions.OProcessUi getProcessByName(@io.vertigo.datamodel.task.proxy.TaskInput(name = "name", smartType = "STyOLibelle") final String name) {
		final Task task = createTaskBuilder("TkGetProcessByName")
				.addValue("name", name)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache StTkSearchProcessByLabel.
	 * @param search String
	 * @return DtList de OProcessUi dtcOProcessUi
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkSearchProcessByLabel",
			request = "select  pro.PRO_ID as PRO_ID," +
					"        			pro.NAME as NAME," +
					"        			pro.LABEL as LABEL," +
					"        			pro.CRON_EXPRESSION as CRON_EXPRESSION," +
					"        			pro.INITIAL_PARAMS as INITIAL_PARAMS," +
					"        			pro.MULTIEXECUTION as MULTIEXECUTION," +
					"        			pro.ACTIVE as ACTIVE," +
					"        			pro.RESCUE_PERIOD as RESCUE_PERIOD," +
					"        			pro.METADATAS as METADATAS" +
					"        	from o_process pro   " +
					"        	where lower(pro.LABEL) like lower(#search#)  and pro.ACTIVE_VERSION is true",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessUi")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.orchestra.monitoring.domain.uidefinitions.OProcessUi> searchProcessByLabel(@io.vertigo.datamodel.task.proxy.TaskInput(name = "search", smartType = "STyOLibelle") final String search) {
		final Task task = createTaskBuilder("TkSearchProcessByLabel")
				.addValue("search", search)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	private TaskManager getTaskManager() {
		return taskManager;
	}
}
