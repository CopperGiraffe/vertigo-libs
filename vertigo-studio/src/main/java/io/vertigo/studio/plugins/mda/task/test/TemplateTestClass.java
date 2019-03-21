/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.studio.plugins.mda.task.test;

import io.vertigo.lang.Assertion;

/**
 * Objet utilisé par FreeMarker.
 *
 * @author sezratty
 */
public final class TemplateTestClass {
	private final String packageName;
	private final String className;

	/**
	 * Constructeur.
	 */
	TemplateTestClass(final String packageName, final String className) {
		Assertion.checkNotNull(packageName);
		Assertion.checkNotNull(className);
		//-----
		this.packageName = packageName;
		this.className = className;
	}

	/**
	 * @return Nom du package de la classe de suite.
	 */
	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}
	
	/**
	 * @return Nom canonique de la classe de test
	 */
	public String getClassCanonicalName() {
		return packageName + "." + className;
	}
}
