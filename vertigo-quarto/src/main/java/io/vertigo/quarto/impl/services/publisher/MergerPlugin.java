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
package io.vertigo.quarto.impl.services.publisher;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import io.vertigo.lang.Plugin;
import io.vertigo.quarto.services.publisher.PublisherFormat;
import io.vertigo.quarto.services.publisher.model.PublisherData;

/**
 * Merger Plugin pour la fusion de documents.
 * 
 * @author adufranne
 */
public interface MergerPlugin extends Plugin {

	/**
	 * Point d'entrée du plugin.
	 * 
	 * @param modelFileURL Chemin vers le fichier model
	 * @param data Données à fusionner avec le model
	 * @return le File généré.
	 * @throws IOException en cas d'erreur de lecture ou d'écriture.
	 */
	File execute(final URL modelFileURL, final PublisherData data) throws IOException;

	/**
	 * @return Type de format géré par ce plugin
	 */
	PublisherFormat getPublisherFormat();

}
