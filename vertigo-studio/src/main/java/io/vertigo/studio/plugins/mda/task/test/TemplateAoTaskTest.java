package io.vertigo.studio.plugins.mda.task.test;

import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.lang.Assertion;
import io.vertigo.studio.plugins.mda.FileGeneratorConfig;

/**
 * Objet utilisé par FreeMarker.
 *
 * @author sezratty
 */
public final class TemplateAoTaskTest {
	private final String packageName;
	private final String className;
	private final TemplateTaskDefinition taskDefinition;
	private final String daoTestBaseClass;

	/**
	 * Constructeur.
	 */
	TemplateAoTaskTest(final FileGeneratorConfig taskConfiguration, final TaskDefinition taskDefinition, final String packageName, final String className, final String daoTestBaseClass) {
		Assertion.checkNotNull(taskConfiguration);
		Assertion.checkNotNull(taskDefinition);
		Assertion.checkNotNull(packageName);
		//-----
		this.packageName = packageName;

		this.className = className;
		this.taskDefinition = new TemplateTaskDefinition(taskDefinition, packageName, className);
		this.daoTestBaseClass = daoTestBaseClass;
	}
	
	/**
	 * @return Simple Nom (i.e. sans le package) de la classe de test.
	 */
	public String getClassSimpleName() {
		return className;
	}

	/**
	 * @return Task
	 */
	public TemplateTaskDefinition getTaskDefinition() {
		return taskDefinition;
	}

	/**
	 * @return Nom du package de la classe de DAO.
	 */
	public String getPackageName() {
		return packageName;
	}	

	/**
	 * @return Si ce pao utilise au moins une Option : vertigo.core.lang.Option
	 */
	public boolean isOptions() {
		return this.taskDefinition.hasOptions();
	}

	/**
	 * @return Nom canonique de la classe de base pour le test de DAO.
	 */
	public String getDaoTestBaseClass() {
		return daoTestBaseClass;
	}
	
	/**
	 * @return Nom simple de la classe de base pour le test de DAO.
	 */
	public String getDaoTestBaseClassSimpleName() {
		return getLastPackagename(daoTestBaseClass);
	}
	
	private static String getLastPackagename(final String canonicalName) {
		final String[] parts = canonicalName.split("\\.");
		return parts[parts.length - 1];
	}
}
