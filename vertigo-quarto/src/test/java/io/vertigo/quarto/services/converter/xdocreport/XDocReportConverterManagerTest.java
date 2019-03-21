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
package io.vertigo.quarto.services.converter.xdocreport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import io.vertigo.AbstractTestCaseJU4;
import io.vertigo.dynamo.file.FileManager;
import io.vertigo.dynamo.file.model.VFile;
import io.vertigo.dynamo.file.util.FileUtil;
import io.vertigo.lang.Assertion;
import io.vertigo.quarto.services.converter.ConverterManager;
import io.vertigo.util.TempFile;

/**
 * Test de l'implémentation avec la librairie XDocReport.
 *
 * @author jgarnier
 */
public final class XDocReportConverterManagerTest extends AbstractTestCaseJU4 {
	/** Logger. */
	private final Logger log = LogManager.getLogger(getClass());

	@Inject
	private ConverterManager converterManager;

	@Inject
	private FileManager fileManager;

	private VFile resultFile;

	/**
	 * Converssion de Odt vers Pdf.
	 */
	@Test
	public void testConvertOdt2Pdf() throws IOException {
		final VFile inputFile = createVFile(fileManager, "../data/testFile.odt", this.getClass());
		resultFile = converterManager.convert(inputFile, "PDF");

		log("Odt2Pdf", resultFile);
	}

	@Test
	public void testConvertDocx2Pdf() throws IOException {
		final VFile inputFile = createVFile(fileManager, "../data/testFile.docx", this.getClass());
		resultFile = converterManager.convert(inputFile, "PDF");

		log("Docx2Pdf", resultFile);
	}

	private void log(final String methode, final VFile vFile) {
		log.info(methode + " => " + fileManager.obtainReadOnlyFile(vFile).getAbsolutePath());
	}

	private static VFile createVFile(final FileManager fileManager, final String fileName, final Class<?> baseClass) throws IOException {
		try (final InputStream in = baseClass.getResourceAsStream(fileName)) {
			Assertion.checkNotNull(in, "fichier non trouvé : {0}", fileName);
			final String fileExtension = FileUtil.getFileExtension(fileName);
			final File file = new TempFile("tmp", '.' + fileExtension);
			FileUtil.copy(in, file);

			final String mimeType;
			if ("docx".equalsIgnoreCase(fileExtension)) {
				mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			} else if ("odt".equalsIgnoreCase(fileExtension)) {
				mimeType = "application/vnd.oasis.opendocument.text";
			} else {
				throw new IllegalArgumentException("File type not supported (" + fileExtension + ")");
			}

			return fileManager.createFile(file.getName(), mimeType, file);
		}
	}
}
