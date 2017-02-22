/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.app.config;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.lang.Assertion;
import io.vertigo.lang.Builder;

/**
 * The moduleConfigBuilder defines the configuration of a module.
 * A module has a name.
 * A module is composed of
 *  - components & plugins
 *  - aspects
 *  - definitions (defined by resources or providers)
 *
 * @author npiedeloup, pchretien
 */
public final class DefinitionProviderConfigBuilder implements Builder<DefinitionProviderConfig> {
	private final Class<? extends DefinitionProvider> myClass;

	private final List<DefinitionResourceConfig> myDefinitionResourceConfigs = new ArrayList<>();

	/**
	 * Constructor.
	 * @param definitionProviderClass the class of the definitionProvider
	 */

	public DefinitionProviderConfigBuilder(final Class<? extends DefinitionProvider> definitionProviderClass) {
		Assertion.checkNotNull(definitionProviderClass);
		//-----
		myClass = definitionProviderClass;
	}

	/**
	 * Adds definitions defined by a resource file.
	 * @param resourceType Type of the resource
	 * @param resourcePath Path of the resource
	* @return this builder
	 */
	public DefinitionProviderConfigBuilder addDefinitionResource(final String resourceType, final String resourcePath) {
		Assertion.checkArgNotEmpty(resourceType);
		Assertion.checkNotNull(resourcePath);
		//-----
		myDefinitionResourceConfigs.add(new DefinitionResourceConfig(resourceType, resourcePath));
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public DefinitionProviderConfig build() {
		final DefinitionProviderConfig definitionProviderConfig = new DefinitionProviderConfig(
				myClass,
				myDefinitionResourceConfigs);

		return definitionProviderConfig;
	}

}
