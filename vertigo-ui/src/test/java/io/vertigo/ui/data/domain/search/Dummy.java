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
package io.vertigo.ui.data.domain.search;

import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Attention cette classe est générée automatiquement !
 * Objet de données Dummy
 */
public final class Dummy implements DtObject {

	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Long dummyLong;

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return Long dummyLong
	 */
	@Field(domain = "DO_ID", label = "Id")
	public Long getDummyLong() {
		return dummyLong;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Id'.
	 * @param dummyLong Long
	 */
	public void setDummyLong(final Long dummyLong) {
		this.dummyLong = dummyLong;
	}

	//Aucune Association déclarée

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
