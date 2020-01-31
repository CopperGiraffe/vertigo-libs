package io.vertigo.orchestra.domain.planification;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.datamodel.structure.stereotype.DataSpace("orchestra")
public final class OProcessPlanification implements Entity {
	private static final long serialVersionUID = 1L;

	private Long prpId;
	private java.time.Instant expectedTime;
	private String initialParams;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "APrpPro",
			fkFieldName = "proId",
			primaryDtDefinitionName = "DtOProcess",
			primaryIsNavigable = true,
			primaryRole = "Processus",
			primaryLabel = "Processus",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtOProcessPlanification",
			foreignIsNavigable = false,
			foreignRole = "ProcessPlanification",
			foreignLabel = "PlanificationProcessus",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.definition.OProcess> proIdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.definition.OProcess.class, "Processus");

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "APrpNod",
			fkFieldName = "nodId",
			primaryDtDefinitionName = "DtONode",
			primaryIsNavigable = true,
			primaryRole = "Node",
			primaryLabel = "Node",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DtOProcessPlanification",
			foreignIsNavigable = false,
			foreignRole = "ProcessPlanification",
			foreignLabel = "PlanificationProcessus",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.execution.ONode> nodIdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.execution.ONode.class, "Node");

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "APrpPst",
			fkFieldName = "sstCd",
			primaryDtDefinitionName = "DtOSchedulerState",
			primaryIsNavigable = true,
			primaryRole = "PlanificationState",
			primaryLabel = "PlanificationState",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DtOProcessPlanification",
			foreignIsNavigable = false,
			foreignRole = "ProcessPlanification",
			foreignLabel = "ProcessPlanification",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.referential.OSchedulerState> sstCdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.referential.OSchedulerState.class, "PlanificationState");

	/** {@inheritDoc} */
	@Override
	public UID<OProcessPlanification> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id Planification'.
	 * @return Long prpId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOIdentifiant", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id Planification")
	public Long getPrpId() {
		return prpId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id Planification'.
	 * @param prpId Long <b>Obligatoire</b>
	 */
	public void setPrpId(final Long prpId) {
		this.prpId = prpId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date d'execution prévue'.
	 * @return Instant expectedTime
	 */
	@Field(smartType = "STyOTimestamp", label = "Date d'execution prévue")
	public java.time.Instant getExpectedTime() {
		return expectedTime;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date d'execution prévue'.
	 * @param expectedTime Instant
	 */
	public void setExpectedTime(final java.time.Instant expectedTime) {
		this.expectedTime = expectedTime;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Paramètres initiaux sous forme de JSON'.
	 * @return String initialParams
	 */
	@Field(smartType = "STyOJsonText", label = "Paramètres initiaux sous forme de JSON")
	public String getInitialParams() {
		return initialParams;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Paramètres initiaux sous forme de JSON'.
	 * @param initialParams String
	 */
	public void setInitialParams(final String initialParams) {
		this.initialParams = initialParams;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Processus'.
	 * @return Long proId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyOIdentifiant", label = "Processus", fkDefinition = "DtOProcess" )
	public Long getProId() {
		return (Long) proIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Processus'.
	 * @param proId Long <b>Obligatoire</b>
	 */
	public void setProId(final Long proId) {
		proIdAccessor.setId(proId);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Node'.
	 * @return Long nodId
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyOIdentifiant", label = "Node", fkDefinition = "DtONode" )
	public Long getNodId() {
		return (Long) nodIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Node'.
	 * @param nodId Long
	 */
	public void setNodId(final Long nodId) {
		nodIdAccessor.setId(nodId);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'PlanificationState'.
	 * @return String sstCd
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyOCodeIdentifiant", label = "PlanificationState", fkDefinition = "DtOSchedulerState" )
	public String getSstCd() {
		return (String) sstCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'PlanificationState'.
	 * @param sstCd String
	 */
	public void setSstCd(final String sstCd) {
		sstCdAccessor.setId(sstCd);
	}

 	/**
	 * Association : Node.
	 * @return l'accesseur vers la propriété 'Node'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.execution.ONode> node() {
		return nodIdAccessor;
	}

 	/**
	 * Association : Processus.
	 * @return l'accesseur vers la propriété 'Processus'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.definition.OProcess> processus() {
		return proIdAccessor;
	}

 	/**
	 * Association : PlanificationState.
	 * @return l'accesseur vers la propriété 'PlanificationState'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.referential.OSchedulerState> planificationState() {
		return sstCdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
