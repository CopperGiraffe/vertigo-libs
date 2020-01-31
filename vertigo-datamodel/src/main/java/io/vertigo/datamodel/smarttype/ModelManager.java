package io.vertigo.datamodel.smarttype;

import java.util.Map;

import io.vertigo.core.lang.BasicTypeAdapter;
import io.vertigo.core.node.component.Manager;
import io.vertigo.datamodel.structure.metamodel.ConstraintException;
import io.vertigo.datamodel.structure.metamodel.FormatterException;

public interface ModelManager extends Manager {

	void checkValue(SmartTypeDefinition smartTypeDefinition, Object value);

	void checkConstraints(SmartTypeDefinition smartTypeDefinition, Object value) throws ConstraintException;

	String valueToString(SmartTypeDefinition smartTypeDefinition, Object objValue);

	Object stringToValue(SmartTypeDefinition smartTypeDefinition, String strValue) throws FormatterException;

	Map<Class, BasicTypeAdapter> getTypeAdapters(final String adapterType);

}
