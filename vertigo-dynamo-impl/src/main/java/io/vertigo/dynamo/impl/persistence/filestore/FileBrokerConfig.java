/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamo.impl.persistence.filestore;

import io.vertigo.dynamo.file.metamodel.FileInfoDefinition;
import io.vertigo.dynamo.impl.persistence.filestore.logical.LogicalFileStoreConfig;

/**
 * Implémentation Standard du StoreProvider.
 *
 * @author pchretien
 */
public final class FileBrokerConfig {
	private final LogicalFileStoreConfig logicalFileStoreConfig;

	/**
	 * Constructeur.
	 */
	public FileBrokerConfig() {
		//-----
		logicalFileStoreConfig = new LogicalFileStoreConfig();
	}

	/**
	 * @param fileInfoDefinition Definition de fichier
	 * @param newFileStore Store de fichier
	 */
	void registerFileStorePlugin(final FileInfoDefinition fileInfoDefinition, final FileStore newFileStore) {
		getLogicalFileStoreConfiguration().register(fileInfoDefinition, newFileStore);
	}

	public LogicalFileStoreConfig getLogicalFileStoreConfiguration() {
		return logicalFileStoreConfig;
	}
}
