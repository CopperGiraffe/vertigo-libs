package io.vertigo.orchestra.domain.referential;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@io.vertigo.datamodel.structure.stereotype.DataSpace("orchestra")
@io.vertigo.datamodel.smarttype.annotations.Mapper(clazz = io.vertigo.datamodel.structure.util.JsonMapper.class, dataType = io.vertigo.core.lang.BasicType.String)
public final class TriggerType implements Entity {
	private static final long serialVersionUID = 1L;

	private String trtCd;
	private String label;

	/** {@inheritDoc} */
	@Override
	public UID<TriggerType> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Code'.
	 * @return String trtCd <b>Obligatoire</b>
	 */
	@Field(smartType = "STyOCodeIdentifiant", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Code")
	public String getTrtCd() {
		return trtCd;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Code'.
	 * @param trtCd String <b>Obligatoire</b>
	 */
	public void setTrtCd(final String trtCd) {
		this.trtCd = trtCd;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Libellé'.
	 * @return String label
	 */
	@Field(smartType = "STyOLibelle", label = "Libellé")
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Libellé'.
	 * @param label String
	 */
	public void setLabel(final String label) {
		this.label = label;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
