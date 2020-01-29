package io.vertigo.orchestra.dao.planification;

import javax.inject.Inject;

import java.util.Optional;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.Home;
import io.vertigo.datamodel.task.metamodel.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.DAO;
import io.vertigo.datastore.impl.dao.StoreServices;
import io.vertigo.datamodel.smarttype.ModelManager;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.orchestra.domain.planification.OProcessPlanification;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class OProcessPlanificationDAO extends DAO<OProcessPlanification, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public OProcessPlanificationDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final ModelManager modelManager) {
		super(OProcessPlanification.class, entityStoreManager, taskManager, modelManager);
	}


	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder 
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = Home.getApp().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache StTkGetAllLastPastPlanifications.
	 * @param currentDate Instant
	 * @return DtList de OProcessPlanification processPlanifications
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetAllLastPastPlanifications",
			request = "select prp.* from  o_process_planification prp" + 
 "			where prp.expected_time < #currentDate# and prp.SST_CD = 'WAITING'" + 
 "			for update",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessPlanification")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> getAllLastPastPlanifications(@io.vertigo.datamodel.task.proxy.TaskInput(name = "currentDate", smartType = "STyOTimestamp") final java.time.Instant currentDate) {
		final Task task = createTaskBuilder("TkGetAllLastPastPlanifications")
				.addValue("currentDate", currentDate)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache StTkGetLastPlanificationByProId.
	 * @param proId Long
	 * @return Option de OProcessPlanification dtOProcessPlanification
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetLastPlanificationByProId",
			request = "select prp.* from  o_process_planification prp" + 
 "        	where prp.PRO_ID = (select pro.PRO_ID from o_process pro where pro.PRO_ID = #proId# for update) and prp.SST_CD = 'WAITING'" + 
 "        	order by prp.expected_time desc" + 
 "        	limit 1",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessPlanification")
	public Optional<io.vertigo.orchestra.domain.planification.OProcessPlanification> getLastPlanificationByProId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "proId", smartType = "STyOIdentifiant") final Long proId) {
		final Task task = createTaskBuilder("TkGetLastPlanificationByProId")
				.addValue("proId", proId)
				.build();
		return Optional.ofNullable((io.vertigo.orchestra.domain.planification.OProcessPlanification) getTaskManager()
				.execute(task)
				.getResult());
	}

	/**
	 * Execute la tache StTkGetPlanificationsByProId.
	 * @param proId Long
	 * @return DtList de OProcessPlanification dtcOProcessPlanification
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetPlanificationsByProId",
			request = "select prp.*" + 
 "        	from o_process_planification prp" + 
 "        	where prp.PRO_ID = #proId#",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessPlanification")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> getPlanificationsByProId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "proId", smartType = "STyOIdentifiant") final Long proId) {
		final Task task = createTaskBuilder("TkGetPlanificationsByProId")
				.addValue("proId", proId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache StTkGetProcessToExecute.
	 * @param nodId Long
	 * @return DtList de OProcessPlanification dtcOProcessPlanification
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			dataSpace = "orchestra",
			name = "TkGetProcessToExecute",
			request = "select prp.*" + 
 "        	from o_process_planification prp" + 
 "        	where prp.SST_CD = 'RESERVED'" + 
 "        	and prp.NOD_ID = #nodId#",
			taskEngineClass = io.vertigo.dynamox.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtOProcessPlanification")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.orchestra.domain.planification.OProcessPlanification> getProcessToExecute(@io.vertigo.datamodel.task.proxy.TaskInput(name = "nodId", smartType = "STyOIdentifiant") final Long nodId) {
		final Task task = createTaskBuilder("TkGetProcessToExecute")
				.addValue("nodId", nodId)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

}
