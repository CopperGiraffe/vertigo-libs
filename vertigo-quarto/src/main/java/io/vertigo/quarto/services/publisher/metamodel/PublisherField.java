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
package io.vertigo.quarto.services.publisher.metamodel;

import java.util.Optional;
import java.util.regex.Pattern;

import io.vertigo.lang.Assertion;

/**
 * Définition de la structure d'un champ d'un noeud du modèle d'édition.
 * Tous les champs sont nommés et typés.
 * @author npiedeloup
 */
public final class PublisherField {
	/**
	 * Expression régulière vérifiée par les noms des champs.
	 */
	private static final Pattern REGEX_FIELD_NAME = Pattern.compile("[A-Z][A-Z0-9_]{2,59}");

	private final String name;
	private final PublisherFieldType fieldType;

	private final Optional<PublisherNodeDefinition> nodeDefinition;

	/**
	 * Constructeur pour les champs composites (noeud de l'arbre de définition).
	 * @param name Nom du champ
	 * @param fieldType Type du champ
	 * @param publisherDataNodeDefinition Définition du noeud sous-jacent
	 */
	PublisherField(final String name, final PublisherFieldType fieldType, final PublisherNodeDefinition publisherDataNodeDefinition) {
		Assertion.checkArgNotEmpty(name);
		Assertion.checkNotNull(fieldType);
		Assertion.checkArgument(REGEX_FIELD_NAME.matcher(name).matches(), "Le nom du champ {0} doit matcher le pattern {1}", name, REGEX_FIELD_NAME);
		if (publisherDataNodeDefinition != null) {
			Assertion.checkArgument(
					fieldType == PublisherFieldType.Node || fieldType == PublisherFieldType.List,
					"Le champ {0} n''est pas du bon type ({1}). Les champs de type Data ou List ont besoin d''une nodeDefinition", name, fieldType);
		} else {
			Assertion.checkArgument(
					fieldType != PublisherFieldType.Node && fieldType != PublisherFieldType.List,
					"Le champ {0} n''est pas du bon type ({1}). Seul les champs de type Data ou List ont besoin d''une nodeDefinition", name, fieldType);
		}
		//-----
		this.name = name;
		this.fieldType = fieldType;
		nodeDefinition = Optional.ofNullable(publisherDataNodeDefinition);
	}

	/**
	 * Retourne le nom du champ.
	 * @return Nom du champ
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Type du champ.
	 */
	public PublisherFieldType getFieldType() {
		return fieldType;
	}

	/**
	 *
	 * Si et seulement si le champ est une liste ou un objet
	 */
	public Optional<PublisherNodeDefinition> getNodeDefinition() {
		return nodeDefinition;
	}
}
