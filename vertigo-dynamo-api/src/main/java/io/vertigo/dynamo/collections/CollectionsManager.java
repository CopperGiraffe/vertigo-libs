/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamo.collections;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;

import io.vertigo.dynamo.collections.model.FacetedQuery;
import io.vertigo.dynamo.collections.model.FacetedQueryResult;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.lang.Manager;

/**
 * Some tools on collections/lists to allow
 *  - sort
 *  - filter
 *  - facets.
 * @author  pchretien
 */
public interface CollectionsManager extends Manager {
	/**
	 * Constructeur d'un filtre de range.
	 * @param fieldName Nom du champ
	 * @param min Valeur minimale
	 * @param max Valeur maximale
	 * @return Filtre
	 * @param <C> Type des bornes
	 */
	<C extends Comparable<?>, D extends DtObject> DtList<D> filterByRange(final DtList<D> list, final String fieldName, final Optional<C> min, final Optional<C> max);

	/**
	 * Constructeur de la function de filtrage à partir d'un filtre de liste.
	 *
	 * @param listFilter Filtre de liste
	 * @return Function de filtrage
	 */
	<D extends DtObject> Predicate<D> filter(final ListFilter listFilter);

	/**
	 * Constructeur d'un filtre champ = valeur.
	 * @param fieldName Nom du champ
	 * @param value Valeur
	 * @return Filtre
	 */
	<D extends DtObject> Predicate<D> filterByValue(final String fieldName, final Serializable value);

	/**
	 * Builds a sub list from a list without changing it.
	 * @param list the list to filter
	 * @param start the start index (Included)
	 * @param end the end index (exculed)
	 * @return the filtered list
	 */
	<D extends DtObject> DtList<D> subList(final DtList<D> list, final int start, final int end);

	/**
	 * Sorts a list from a column.
	 * @param list the list to sort
	 * @param fieldName the field name
	 * @param desc if the sotr is desc
	 * @return the sorted list
	 */
	<D extends DtObject> DtList<D> sort(final DtList<D> list, final String fieldName, final boolean desc);

	/**
	 * Filter or sort a list via a listProcessor powered by an index engine, can be composed of filters or sorters.
	 * @return DtListIndexProcessor
	 * @param <D> Type de l'objet de la liste
	 */
	<D extends DtObject> IndexDtListFunctionBuilder<D> createIndexDtListFunctionBuilder();

	/**
	 * Facettage d'une liste selon une requete.
	 * Le facettage s'effectue en deux temps :
	 *  - Filtrage de la liste
	 *  - Facettage proprement dit
	 * @param dtList Liste à facetter
	 * @param facetedQuery Requete à appliquer (filtrage)
	 * @return Résultat correspondant à la requête
	 * @param <R> Type de l'objet de la liste
	 */
	<R extends DtObject> FacetedQueryResult<R, DtList<R>> facetList(final DtList<R> dtList, final FacetedQuery facetedQuery);
}
