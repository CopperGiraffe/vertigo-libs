/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dashboard.ui.dynamo.model;

import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.structure.metamodel.DtDefinition;
import io.vertigo.datamodel.structure.metamodel.DtStereotype;

public final class EntityModel {
	private final DtDefinition dtDefinition;
	private final Double count; // may be null for UI (displayed as N/A)
	private final Double taskCount; // may be null for UI (displayed as N/A)
	private final Double fieldCount; // may be null for UI (displayed as N/A)

	public EntityModel(
			final DtDefinition dtDefinition,
			final Double count,
			final Double taskCount,
			final Double fieldCount) {
		Assertion.check().notNull(dtDefinition);
		//---
		this.dtDefinition = dtDefinition;
		this.count = count;
		this.taskCount = taskCount;
		this.fieldCount = fieldCount;
	}

	public String getName() {
		return dtDefinition.getName();
	}

	public boolean isKeyConcept() {
		return dtDefinition.getStereotype() == DtStereotype.KeyConcept;
	}

	public Double getCount() {
		return count;
	}

	public Double getTaskCount() {
		return taskCount;
	}

	public Double getFieldCount() {
		return fieldCount;
	}

}
