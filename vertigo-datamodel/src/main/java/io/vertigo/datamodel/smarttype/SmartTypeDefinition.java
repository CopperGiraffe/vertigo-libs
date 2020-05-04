/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, Vertigo.io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.datamodel.smarttype;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.node.definition.Definition;
import io.vertigo.core.node.definition.DefinitionPrefix;
import io.vertigo.core.util.ClassUtil;
import io.vertigo.datamodel.structure.metamodel.Properties;

/**
 * A smarttype exists to enrich the primitive datatypes, giving them super powers.
 *
 * A smarttype has
 *  - a class that represent the value
 *  - an ability to be transformed in and from a primitive datatype
 *
 * Examples :
 *  A mail is not defined by a simple "String", but by a smartType called 'Mail'.
 *  Weights, currencies, codes, labels...
 *
 *  An application is built with some dozens of smartType.
 *
 * @author pchretien
 */
@DefinitionPrefix("STy")
public final class SmartTypeDefinition implements Definition {
	public enum Scope {
		PRIMITIVE, VALUE_OBJECT, DATA_OBJECT;

		/**
		 * @return if the smartType is a primitive type
		 */
		public boolean isPrimitive() {
			return this == Scope.PRIMITIVE;
		}

		/**
		 * @return if the smartType is a value-object
		 */
		public boolean isValueObject() {
			return this == Scope.VALUE_OBJECT;
		}

		/**
		 * @return if the smartType is a data-object
		 */
		public boolean isDataObject() {
			return this == Scope.DATA_OBJECT;
		}
	}

	private final String name;
	private final Scope scope;
	private final String valueObjectClassName;
	private final Optional<BasicType> basicTypeOpt; //nullable
	private final AdapterConfig wildCardAdapterConfig; //nullable
	private final Map<String, AdapterConfig> adapterConfigs;
	private final FormatterConfig formatterConfig;
	private final List<ConstraintConfig> constraintConfigs;

	private final Properties properties;

	public SmartTypeDefinition(
			final String name,
			final Scope scope,
			final String valueObjectClassName,
			final List<AdapterConfig> adapterConfigs,
			final FormatterConfig formatterConfig,
			final List<ConstraintConfig> constraintConfigs,
			final Properties properties) {
		Assertion.check()
				.argNotEmpty(name)
				.notNull(scope)
				.notNull(valueObjectClassName)
				.notNull(adapterConfigs)
				.notNull(constraintConfigs)
				.notNull(properties);
		//-----
		this.name = name;
		this.scope = scope;
		this.valueObjectClassName = valueObjectClassName;
		basicTypeOpt = BasicType.of(getJavaClass());
		this.adapterConfigs = adapterConfigs
				.stream()
				.collect(Collectors.toMap(AdapterConfig::getType, Function.identity(), (a, b) -> {
					throw new IllegalArgumentException("Only one adapter per type is supported. Smarttype '" + name + "' declares multiple adapters for type '" + a.getType() + "'");
				}));
		wildCardAdapterConfig = this.adapterConfigs.get("*");
		this.properties = properties;
		this.formatterConfig = formatterConfig;
		this.constraintConfigs = constraintConfigs;

	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the smartType scope
	 */
	public Scope getScope() {
		return scope;
	}

	public String getValueObjectClassName() {
		return valueObjectClassName;
	}

	public Class getJavaClass() {
		return ClassUtil.classForName(valueObjectClassName);
	}

	public BasicType getBasicType() {
		Assertion.checkState(basicTypeOpt.isPresent(), "Only smarttypes that are derived from BasicTypes have a basic type, use a dedicated adapter instead to convert the value into a BasicType");
		return basicTypeOpt.get();
	}

	public Map<String, AdapterConfig> getAdapterConfigs() {
		return adapterConfigs;
	}

	public AdapterConfig getAdapterConfig(final String type) {
		return adapterConfigs.getOrDefault(type, wildCardAdapterConfig);
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * Returns the formatter of the domain.
	 *
	 * @return the formatter.
	 */
	public FormatterConfig getFormatterConfig() {
		return formatterConfig;
	}

	public List<ConstraintConfig> getConstraintConfigs() {
		return constraintConfigs;
	}

	public static SmartTypeDefinitionBuilder builder(final String name, final BasicType dataType) {
		return new SmartTypeDefinitionBuilder(name, dataType);
	}

	public static SmartTypeDefinitionBuilder builder(final String name, final Class clazz) {
		return new SmartTypeDefinitionBuilder(name, clazz);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name;
	}

}
