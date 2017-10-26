package io.vertigo.orchestra.domain.history;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.model.VAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.dynamo.domain.stereotype.DataSpace("orchestra")
public final class OJobLog implements Entity {
	private static final long serialVersionUID = 1L;

	private Long jloId;
	private java.time.ZonedDateTime dateTrace;
	private String level;
	private String typeExecCd;
	private String message;
	private String parametre;
	private String erreur;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_JLO_JEX",
			fkFieldName = "PRO_ID",
			primaryDtDefinitionName = "DT_O_JOB_EXECUTION",
			primaryIsNavigable = true,
			primaryRole = "JobExecution",
			primaryLabel = "JobExecution",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_O_JOB_LOG",
			foreignIsNavigable = false,
			foreignRole = "JobLog",
			foreignLabel = "JobLog",
			foreignMultiplicity = "0..*")
	private final VAccessor<io.vertigo.orchestra.domain.history.OJobExecution> proIdAccessor = new VAccessor<>(io.vertigo.orchestra.domain.history.OJobExecution.class, "JobExecution");

	/** {@inheritDoc} */
	@Override
	public URI<OJobLog> getURI() {
		return DtObjectUtil.createURI(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id d'une trace d'execution d'un job'.
	 * @return Long jloId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "ID", required = true, label = "Id d'une trace d'execution d'un job")
	public Long getJloId() {
		return jloId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id d'une trace d'execution d'un job'.
	 * @param jloId Long <b>Obligatoire</b>
	 */
	public void setJloId(final Long jloId) {
		this.jloId = jloId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Date de la trace'.
	 * @return java.time.ZonedDateTime dateTrace <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_TIMESTAMP", required = true, label = "Date de la trace")
	public java.time.ZonedDateTime getDateTrace() {
		return dateTrace;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Date de la trace'.
	 * @param dateTrace java.time.ZonedDateTime <b>Obligatoire</b>
	 */
	public void setDateTrace(final java.time.ZonedDateTime dateTrace) {
		this.dateTrace = dateTrace;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Niveau de la trace'.
	 * @return String level <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_CODE_IDENTIFIANT", required = true, label = "Niveau de la trace")
	public String getLevel() {
		return level;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Niveau de la trace'.
	 * @param level String <b>Obligatoire</b>
	 */
	public void setLevel(final String level) {
		this.level = level;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Type de trace'.
	 * @return String typeExecCd <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_CODE_IDENTIFIANT", required = true, label = "Type de trace")
	public String getTypeExecCd() {
		return typeExecCd;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Type de trace'.
	 * @param typeExecCd String <b>Obligatoire</b>
	 */
	public void setTypeExecCd(final String typeExecCd) {
		this.typeExecCd = typeExecCd;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Message'.
	 * @return String message
	 */
	@Field(domain = "DO_O_TEXT", label = "Message")
	public String getMessage() {
		return message;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Message'.
	 * @param message String
	 */
	public void setMessage(final String message) {
		this.message = message;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Paramètre'.
	 * @return String parametre
	 */
	@Field(domain = "DO_O_TEXT", label = "Paramètre")
	public String getParametre() {
		return parametre;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Paramètre'.
	 * @param parametre String
	 */
	public void setParametre(final String parametre) {
		this.parametre = parametre;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Stacktrace d'erreur'.
	 * @return String erreur
	 */
	@Field(domain = "DO_O_TEXT", label = "Stacktrace d'erreur")
	public String getErreur() {
		return erreur;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Stacktrace d'erreur'.
	 * @param erreur String
	 */
	public void setErreur(final String erreur) {
		this.erreur = erreur;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'JobExecution'.
	 * @return Long proId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_O_IDENTIFIANT", type = "FOREIGN_KEY", required = true, label = "JobExecution")
	@Deprecated
	public Long getProId() {
		return (Long)  proIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'JobExecution'.
	 * @param proId Long <b>Obligatoire</b>
	 */
	@Deprecated
	public void setProId(final Long proId) {
		proIdAccessor.setId(proId);
	}

 	/**
	 * Association : JobExecution.
	 * @return l'accesseur vers la propriété 'JobExecution'
	 */
	public VAccessor<io.vertigo.orchestra.domain.history.OJobExecution> getJobExecutionAccessor() {
		return proIdAccessor;
	}
	
	@Deprecated
	public io.vertigo.orchestra.domain.history.OJobExecution getJobExecution() {
		// we keep the lazyness
		if (!proIdAccessor.isLoaded()) {
			proIdAccessor.load();
		}
		return proIdAccessor.get();
	}

	/**
	 * Retourne l'URI: JobExecution.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.URI<io.vertigo.orchestra.domain.history.OJobExecution> getJobExecutionURI() {
		return proIdAccessor.getURI();
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
