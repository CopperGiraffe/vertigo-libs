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
package io.vertigo.datafactory.plugins.search.elasticsearch_5_6;

import java.util.Locale;
import java.util.Optional;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.BasicType;
import io.vertigo.datamodel.smarttype.SmartTypeDefinition;
import io.vertigo.datamodel.structure.metamodel.DtProperty;

final class IndexType {
	private static final String INDEX_TYPE_ERROR_MSG = "indexType ({0}) should respect this usage : indexType : "
			+ "\"myAnalyzer\\{:myDataType\\}\\{:stored|notStored\\}\\{:sortable|notSortable\\}\\{:facetable|notFacetable\\}\"";
	private static final String INDEX_STORED = "stored";
	private static final String INDEX_NOT_STORED = "notStored";
	private static final String INDEX_SORTABLE = "sortable";
	private static final String INDEX_NOT_SORTABLE = "notSortable";
	private static final String INDEX_FACETABLE = "facetable";
	private static final String INDEX_NOT_FACETABLE = "notFacetable";

	private final Optional<String> indexAnalyzer;
	private final String indexDataType;
	private final boolean indexStored;
	private final boolean indexSubKeyword;
	private final boolean indexFieldData;

	private IndexType(final String indexType, final SmartTypeDefinition smartTypeDefinition) {
		Assertion.check().isNotNull(smartTypeDefinition);
		//-----
		checkIndexType(indexType, smartTypeDefinition);
		if (indexType == null) {
			//si pas d'indexType on précise juste le dataType pour rester triable
			indexAnalyzer = Optional.empty();
			indexDataType = obtainDefaultIndexDataType(smartTypeDefinition);
			indexStored = true;
			indexSubKeyword = false;
			indexFieldData = false;
		} else {
			// par convention l'indexType du smartType => l'analyzer de l'index
			// L'indexType peut-être compléter pour préciser le type si différente de string avec le séparateur :
			final String[] indexTypeArray = indexType.split(":", 4);
			indexAnalyzer = Optional.ofNullable(!indexTypeArray[0].isEmpty() ? indexTypeArray[0] : null); //le premier est toujours l'analyzer (ou le normalizer)
			//les suivants sont optionnels et soit indexDataType, soit le indexStored, soit le indexKeyword
			if (indexTypeArray.length == 1) {
				indexDataType = obtainDefaultIndexDataType(smartTypeDefinition);
				indexStored = true;
				indexSubKeyword = false;
				indexFieldData = false;
			} else {
				String parsedIndexDataType = null;
				Boolean parsedIndexStored = null;
				Boolean parsedIndexSubKeyword = null;
				Boolean parsedIndexFieldData = null;
				//On parcours les paramètres et on détermine si on reconnait un mot clé
				for (int i = 1; i < indexTypeArray.length; i++) {
					final String indexTypeParam = indexTypeArray[i];
					if (INDEX_STORED.equals(indexTypeParam) || INDEX_NOT_STORED.equals(indexTypeParam)) {
						Assertion.check().isNull(parsedIndexStored, INDEX_TYPE_ERROR_MSG, indexType);
						parsedIndexStored = INDEX_STORED.equals(indexTypeParam);
					} else if (INDEX_SORTABLE.equals(indexTypeParam) || INDEX_NOT_SORTABLE.equals(indexTypeParam)) {
						Assertion.check().isNull(parsedIndexSubKeyword, INDEX_TYPE_ERROR_MSG, indexType);
						parsedIndexSubKeyword = INDEX_SORTABLE.equals(indexTypeParam);
					} else if (INDEX_FACETABLE.equals(indexTypeParam) || INDEX_NOT_FACETABLE.equals(indexTypeParam)) {
						Assertion.check().isNull(parsedIndexFieldData, INDEX_TYPE_ERROR_MSG, indexType);
						parsedIndexFieldData = INDEX_FACETABLE.equals(indexTypeParam);
					} else {
						Assertion.check().isNull(parsedIndexDataType, INDEX_TYPE_ERROR_MSG, indexType);
						parsedIndexDataType = indexTypeParam;
					}
				}
				//valeurs par défaut
				indexDataType = parsedIndexDataType != null ? parsedIndexDataType : obtainDefaultIndexDataType(smartTypeDefinition);
				indexStored = parsedIndexStored != null ? parsedIndexStored : true;
				indexSubKeyword = parsedIndexSubKeyword != null ? parsedIndexSubKeyword : false;
				indexFieldData = parsedIndexFieldData != null ? parsedIndexFieldData : false;
			}
		}
	}

	// par convention l'indexType du smartType => l'analyzer de l'index
	// L'indexType peut-être compléter pour préciser le type si différente de string avec le séparateur :

	static IndexType readIndexType(final SmartTypeDefinition smartTypeDefinition) {
		final String indexType = smartTypeDefinition.getProperties().getValue(DtProperty.INDEX_TYPE);
		if (indexType == null) {
			return new IndexType(null, smartTypeDefinition);
		}
		return new IndexType(indexType, smartTypeDefinition);
	}

	private static String obtainDefaultIndexDataType(final SmartTypeDefinition smartTypeDefinition) {
		// On peut préciser pour chaque smartType le type d'indexation
		// Calcul automatique  par default.
		Assertion.check().isTrue(
				smartTypeDefinition.getScope().isPrimitive() || smartTypeDefinition.getScope().isValueObject(),
				"Type de donnée non pris en charge pour le keyconcept indexé [" + smartTypeDefinition + "].");
		final BasicType basicType;
		if (smartTypeDefinition.getScope().isPrimitive()) {
			basicType = smartTypeDefinition.getBasicType();
		} else { // smartTypeDefinition.getScope().isValueObject()
			basicType = smartTypeDefinition.getAdapterConfig("search").getTargetBasicType();
		}

		switch (basicType) {
			case Boolean:
			case Double:
			case Integer:
			case Long:
				return basicType.name().toLowerCase(Locale.ROOT);
			case String:
				return "text";
			case LocalDate:
			case Instant:
				return "date";
			case BigDecimal:
				return "scaled_float";
			case DataStream:
			default:
				throw new IllegalArgumentException("Type de donnée non pris en charge pour l'indexation [" + smartTypeDefinition + "].");
		}
	}

	private static void checkIndexType(final String indexType, final SmartTypeDefinition smartTypeDefinition) {
		// On peut préciser pour chaque smartType le type d'indexation
		// Calcul automatique  par default.
		Assertion.check().isTrue(
				smartTypeDefinition.getScope().isPrimitive() || smartTypeDefinition.getScope().isValueObject(),
				"Type de donnée non pris en charge pour le keyconcept indexé [" + smartTypeDefinition + "].");

		final BasicType basicType;
		if (smartTypeDefinition.getScope().isPrimitive()) {
			basicType = smartTypeDefinition.getBasicType();
		} else { // smartTypeDefinition.getScope().isValueObject()
			basicType = smartTypeDefinition.getAdapterConfig("search").getTargetBasicType();
		}
		switch (basicType) {
			case Boolean:
			case LocalDate:
			case Instant:
			case Double:
			case Integer:
			case Long:
			case BigDecimal:
				// All these types are native
				break;
			case String:
				if (indexType == null) {
					throw new IllegalArgumentException("Précisez la valeur \"indexType\" dans le smart type [" + smartTypeDefinition + "].");
				}
				break;
			case DataStream:
			default:
				throw new IllegalArgumentException("Type de donnée non pris en charge pour l'indexation [" + smartTypeDefinition + "].");
		}
	}

	public Optional<String> getIndexAnalyzer() {
		return indexAnalyzer;
	}

	public String getIndexDataType() {
		return indexDataType;
	}

	public boolean isIndexStored() {
		return indexStored;
	}

	public boolean isIndexSubKeyword() {
		return indexSubKeyword;
	}

	public boolean isIndexFieldData() {
		return indexFieldData;
	}
}
