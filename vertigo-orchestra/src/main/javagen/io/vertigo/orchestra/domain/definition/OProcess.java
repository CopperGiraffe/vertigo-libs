package io.vertigo.orchestra.domain.definition;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.datamodel.structure.stereotype.DataSpace("orchestra")
public final class OProcess implements Entity {
	private static final long serialVersionUID = 1L;

	private Long proId;
	private String name;
	private String label;
	private String cronExpression;
	private String initialParams;
	private Boolean multiexecution;
	private Boolean activeVersion;
	private Boolean active;
	private Integer rescuePeriod;
	private String metadatas;
	private Boolean needUpdate;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AProTrt",
			fkFieldName = "trtCd",
			primaryDtDefinitionName = "DtTriggerType",
			primaryIsNavigable = true,
			primaryRole = "TriggerType",
			primaryLabel = "TriggerType",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtOProcess",
			foreignIsNavigable = false,
			foreignRole = "Process",
			foreignLabel = "Process",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.referential.TriggerType> trtCdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.referential.TriggerType.class, "TriggerType");

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AProPrt",
			fkFieldName = "prtCd",
			primaryDtDefinitionName = "DtOProcessType",
			primaryIsNavigable = true,
			primaryRole = "ProcessType",
			primaryLabel = "ProcessType",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DtOProcess",
			foreignIsNavigable = false,
			foreignRole = "Process",
			foreignLabel = "Process",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.orchestra.domain.referential.OProcessType> prtCdAccessor = new StoreVAccessor<>(io.vertigo.orchestra.domain.referential.OProcessType.class, "ProcessType");

	/** {@inheritDoc} */
	@Override
	public UID<OProcess> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id de la definition du processus'.
	 * @return Long proId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOIdentifiant", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id de la definition du processus")
	public Long getProId() {
		return proId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id de la definition du processus'.
	 * @param proId Long <b>Obligatoire</b>
	 */
	public void setProId(final Long proId) {
		this.proId = proId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nom du processus'.
	 * @return String name
	 */
	@Field(smartType = "STyOLibelle", label = "Nom du processus")
	public String getName() {
		return name;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nom du processus'.
	 * @param name String
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Libellé du processus'.
	 * @return String label
	 */
	@Field(smartType = "STyOLibelle", label = "Libellé du processus")
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Libellé du processus'.
	 * @param label String
	 */
	public void setLabel(final String label) {
		this.label = label;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Expression récurrence du processus'.
	 * @return String cronExpression
	 */
	@Field(smartType = "STyOLibelle", label = "Expression récurrence du processus")
	public String getCronExpression() {
		return cronExpression;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Expression récurrence du processus'.
	 * @param cronExpression String
	 */
	public void setCronExpression(final String cronExpression) {
		this.cronExpression = cronExpression;
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
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Accepte la multi-execution'.
	 * @return Boolean multiexecution
	 */
	@Field(smartType = "STyOBooleen", label = "Accepte la multi-execution")
	public Boolean getMultiexecution() {
		return multiexecution;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Accepte la multi-execution'.
	 * @param multiexecution Boolean
	 */
	public void setMultiexecution(final Boolean multiexecution) {
		this.multiexecution = multiexecution;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Version active'.
	 * @return Boolean activeVersion <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOBooleen", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Version active")
	public Boolean getActiveVersion() {
		return activeVersion;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Version active'.
	 * @param activeVersion Boolean <b>Obligatoire</b>
	 */
	public void setActiveVersion(final Boolean activeVersion) {
		this.activeVersion = activeVersion;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Processus actif'.
	 * @return Boolean active <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOBooleen", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Processus actif")
	public Boolean getActive() {
		return active;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Processus actif'.
	 * @param active Boolean <b>Obligatoire</b>
	 */
	public void setActive(final Boolean active) {
		this.active = active;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Temps de validité d'une planification'.
	 * @return Integer rescuePeriod <b>Obligatoire</b>
	 */
	@Field(smartType = "STyONombre", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Temps de validité d'une planification")
	public Integer getRescuePeriod() {
		return rescuePeriod;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Temps de validité d'une planification'.
	 * @param rescuePeriod Integer <b>Obligatoire</b>
	 */
	public void setRescuePeriod(final Integer rescuePeriod) {
		this.rescuePeriod = rescuePeriod;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Meta-données du processus'.
	 * @return String metadatas
	 */
	@Field(smartType = "STyOMetadatas", label = "Meta-données du processus")
	public String getMetadatas() {
		return metadatas;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Meta-données du processus'.
	 * @param metadatas String
	 */
	public void setMetadatas(final String metadatas) {
		this.metadatas = metadatas;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Doit être mise à jour lors du démarrage'.
	 * @return Boolean needUpdate <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOBooleen", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Doit être mise à jour lors du démarrage")
	public Boolean getNeedUpdate() {
		return needUpdate;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Doit être mise à jour lors du démarrage'.
	 * @param needUpdate Boolean <b>Obligatoire</b>
	 */
	public void setNeedUpdate(final Boolean needUpdate) {
		this.needUpdate = needUpdate;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'TriggerType'.
	 * @return String trtCd <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyOCodeIdentifiant", label = "TriggerType", fkDefinition = "DtTriggerType" )
	public String getTrtCd() {
		return (String) trtCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'TriggerType'.
	 * @param trtCd String <b>Obligatoire</b>
	 */
	public void setTrtCd(final String trtCd) {
		trtCdAccessor.setId(trtCd);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'ProcessType'.
	 * @return String prtCd
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyOCodeIdentifiant", label = "ProcessType", fkDefinition = "DtOProcessType" )
	public String getPrtCd() {
		return (String) prtCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'ProcessType'.
	 * @param prtCd String
	 */
	public void setPrtCd(final String prtCd) {
		prtCdAccessor.setId(prtCd);
	}

 	/**
	 * Association : ProcessType.
	 * @return l'accesseur vers la propriété 'ProcessType'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.referential.OProcessType> processType() {
		return prtCdAccessor;
	}

 	/**
	 * Association : TriggerType.
	 * @return l'accesseur vers la propriété 'TriggerType'
	 */
	public StoreVAccessor<io.vertigo.orchestra.domain.referential.TriggerType> triggerType() {
		return trtCdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
