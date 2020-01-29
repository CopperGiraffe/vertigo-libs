package io.vertigo.struts2.data.domain.users;

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
@io.vertigo.datamodel.smarttype.annotations.Mapper(clazz = io.vertigo.datamodel.structure.util.JsonMapper.class, dataType = io.vertigo.core.lang.BasicType.String)
public final class ApplicationUser implements Entity {
	private static final long serialVersionUID = 1L;

	private Long usrId;
	private String lastName;
	private String firstName;
	private String email;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AUsrPro",
			fkFieldName = "proId",
			primaryDtDefinitionName = "DtProfil",
			primaryIsNavigable = true,
			primaryRole = "Profil",
			primaryLabel = "Profil",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DtApplicationUser",
			foreignIsNavigable = false,
			foreignRole = "ApplicationUser",
			foreignLabel = "Application user",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.struts2.data.domain.users.Profil> proIdAccessor = new StoreVAccessor<>(io.vertigo.struts2.data.domain.users.Profil.class, "Profil");

	/** {@inheritDoc} */
	@Override
	public UID<ApplicationUser> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'USR_ID'.
	 * @return Long usrId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "USR_ID")
	public Long getUsrId() {
		return usrId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'USR_ID'.
	 * @param usrId Long <b>Obligatoire</b>
	 */
	public void setUsrId(final Long usrId) {
		this.usrId = usrId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Last Name'.
	 * @return String lastName
	 */
	@Field(smartType = "STyName", label = "Last Name")
	public String getLastName() {
		return lastName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Last Name'.
	 * @param lastName String
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'First Name'.
	 * @return String firstName
	 */
	@Field(smartType = "STyFirstname", label = "First Name")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'First Name'.
	 * @param firstName String
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'email'.
	 * @return String email
	 */
	@Field(smartType = "STyEmail", label = "email")
	public String getEmail() {
		return email;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'email'.
	 * @param email String
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Profil'.
	 * @return Long proId
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyId", label = "Profil", fkDefinition = "DtProfil")
	public Long getProId() {
		return (Long) proIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Profil'.
	 * @param proId Long
	 */
	public void setProId(final Long proId) {
		proIdAccessor.setId(proId);
	}

	/**
	 * Association : Profil.
	 * @return l'accesseur vers la propriété 'Profil'
	 */
	public StoreVAccessor<io.vertigo.struts2.data.domain.users.Profil> profil() {
		return proIdAccessor;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
