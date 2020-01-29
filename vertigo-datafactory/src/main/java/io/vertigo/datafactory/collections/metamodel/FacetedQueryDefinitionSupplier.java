package io.vertigo.datafactory.collections.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.DefinitionSupplier;
import io.vertigo.datamodel.smarttype.SmartTypeDefinition;

public final class FacetedQueryDefinitionSupplier implements DefinitionSupplier {

	private final String name;
	private String myCriteriaSmartTypeName;
	private String myListFilterBuilderQuery;
	private Class<? extends ListFilterBuilder> myListFilterBuilderClass;
	private final List<String> facets = new ArrayList<>();

	public FacetedQueryDefinitionSupplier(final String name) {
		this.name = name;
	}

	public FacetedQueryDefinitionSupplier withFacet(final String facetName) {
		facets.add(facetName);
		return this;
	}

	public FacetedQueryDefinitionSupplier withCriteriaSmartType(final String criteriaSmartTypeName) {
		this.myCriteriaSmartTypeName = criteriaSmartTypeName;
		return this;
	}

	public FacetedQueryDefinitionSupplier withListFilterBuilderQuery(final String listFilterBuilderQuery) {
		this.myListFilterBuilderQuery = listFilterBuilderQuery;
		return this;
	}

	public FacetedQueryDefinitionSupplier withListFilterBuilderClass(final Class<? extends ListFilterBuilder> listFilterBuilderClass) {
		this.myListFilterBuilderClass = listFilterBuilderClass;
		return this;
	}

	@Override
	public FacetedQueryDefinition get(final DefinitionSpace definitionSpace) {
		final List<FacetDefinition> facetDefinitions = facets.stream()
				.map(facetName -> definitionSpace.resolve(facetName, FacetDefinition.class))
				.collect(Collectors.toList());
		final SmartTypeDefinition criteriaSmartType = definitionSpace.resolve(myCriteriaSmartTypeName, SmartTypeDefinition.class);
		return new FacetedQueryDefinition(
				name,
				facetDefinitions,
				criteriaSmartType,
				myListFilterBuilderClass,
				myListFilterBuilderQuery);
	}
}
