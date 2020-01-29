package io.vertigo.datafactory.collections.metamodel;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.locale.MessageText;
import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.DefinitionSupplier;
import io.vertigo.datafactory.collections.ListFilter;
import io.vertigo.datafactory.collections.metamodel.FacetDefinition.FacetOrder;
import io.vertigo.datafactory.collections.model.FacetValue;
import io.vertigo.datamodel.structure.metamodel.DtDefinition;

public final class FacetRangeDefinitionSupplier implements DefinitionSupplier {

	private final String myName;
	private String myDtDefinitionName;
	private String myFieldName;
	private String myLabel;
	private boolean myMultiSelectable;
	private final List<FacetValue> facetValues = new ArrayList<>();
	private FacetOrder myOrder;

	public FacetRangeDefinitionSupplier(final String name) {
		this.myName = name;
	}

	public FacetRangeDefinitionSupplier withDtDefinition(final String dtDefinitionName) {
		this.myDtDefinitionName = dtDefinitionName;
		return this;
	}

	public FacetRangeDefinitionSupplier withFieldName(final String fieldName) {
		this.myFieldName = fieldName;
		return this;
	}

	public FacetRangeDefinitionSupplier withLabel(final String label) {
		this.myLabel = label;
		return this;
	}

	public FacetRangeDefinitionSupplier withMultiSelectable() {
		myMultiSelectable = true;
		return this;
	}

	public FacetRangeDefinitionSupplier withRange(final String code, final String filter, final String label) {
		facetValues.add(new FacetValue(code, ListFilter.of(filter), MessageText.of(label)));
		return this;
	}

	public FacetRangeDefinitionSupplier withOrder(final FacetOrder order) {
		this.myOrder = order;
		return this;
	}

	@Override
	public FacetDefinition get(final DefinitionSpace definitionSpace) {
		final DtDefinition indexDtDefinition = definitionSpace.resolve(myDtDefinitionName, DtDefinition.class);
		return FacetDefinition.createFacetDefinitionByRange(
				myName,
				indexDtDefinition.getField(myFieldName),
				MessageText.of(myLabel),
				facetValues,
				myMultiSelectable,
				myOrder);
	}
}
