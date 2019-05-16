/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.quarto.plugins.export.pdf;

import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.quarto.plugins.export.pdfrtf.AbstractExporterIText;

/**
 * Export PDF avec iText. Configuré par ExportParametersPDF.
 *
 * @author evernat
 */
final class PDFExporter extends AbstractExporterIText {
	/**
	 * Constructor.
	 * @param storeManager Store manager
	 */
	PDFExporter(final StoreManager storeManager) {
		super(storeManager);
	}

	/** {@inheritDoc} */
	@Override
	protected void createWriter(final Document document, final OutputStream out) throws DocumentException {
		final PdfWriter writer = PdfWriter.getInstance(document, out);
		// add the event handler for advanced page numbers : x/y
		writer.setPageEvent(new PDFAdvancedPageNumberEvents());
	}
}
