/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamo.environment.java.data.domain;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.model.VAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
@javax.persistence.Entity
@javax.persistence.Table(name = "ATTACHMENT")
public final class Attachment implements Entity {
	private static final long serialVersionUID = 1L;

	private Long attId;
	private String url;

	@javax.persistence.Transient
	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_CMD_ATT",
			fkFieldName = "CMD_ID",
			primaryDtDefinitionName = "DT_COMMAND",
			primaryIsNavigable = true,
			primaryRole = "Command",
			primaryLabel = "Command",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_ATTACHMENT",
			foreignIsNavigable = true,
			foreignRole = "Attachment",
			foreignLabel = "Attachment",
			foreignMultiplicity = "0..*")
	private final VAccessor<Command> cmdIdAccessor = new VAccessor<>(Command.class, "Command");

	/** {@inheritDoc} */
	@javax.persistence.Transient
	@Override
	public UID<Attachment> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'id'.
	 * @return Long attId <b>Obligatoire</b>
	 */
	@javax.persistence.Id
	@javax.persistence.SequenceGenerator(name = "sequence", sequenceName = "SEQ_ATTACHMENT", allocationSize = 1)
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "sequence")
	@javax.persistence.Column(name = "ATT_ID")
	@Field(domain = "DO_ID", type = "ID", required = true, label = "id")
	public Long getAttId() {
		return attId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'id'.
	 * @param attId Long <b>Obligatoire</b>
	 */
	public void setAttId(final Long attId) {
		this.attId = attId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Url'.
	 * @return String url <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "URL")
	@Field(domain = "DO_KEYWORD", required = true, label = "Url")
	public String getUrl() {
		return url;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Url'.
	 * @param url String <b>Obligatoire</b>
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Command'.
	 * @return Long cmdId
	 */
	@javax.persistence.Column(name = "CMD_ID")
	@Field(domain = "DO_ID", type = "FOREIGN_KEY", label = "Command")
	public Long getCmdId() {
		return (Long) cmdIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Command'.
	 * @param cmdId Long
	 */
	public void setCmdId(final Long cmdId) {
		cmdIdAccessor.setId(cmdId);
	}

	/**
	 * Association : Command.
	 * @return l'accesseur vers la propriété 'Command'
	 */
	@javax.persistence.Transient
	public VAccessor<Command> command() {
		return cmdIdAccessor;
	}

	@Deprecated
	@javax.persistence.Transient
	public Command getCommand() {
		// we keep the lazyness
		if (!cmdIdAccessor.isLoaded()) {
			cmdIdAccessor.load();
		}
		return cmdIdAccessor.get();
	}

	/**
	 * Retourne l'URI: Command.
	 * @return URI de l'association
	 */
	@Deprecated
	@javax.persistence.Transient
	public io.vertigo.dynamo.domain.model.UID<Command> getCommandURI() {
		return cmdIdAccessor.getURI();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
