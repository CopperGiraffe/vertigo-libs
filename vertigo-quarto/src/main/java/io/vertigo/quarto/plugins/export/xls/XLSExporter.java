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
package io.vertigo.quarto.plugins.export.xls;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.util.CellRangeAddress;

import io.vertigo.core.locale.MessageText;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.metamodel.Domain;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.lang.Assertion;
import io.vertigo.quarto.impl.services.export.util.ExportUtil;
import io.vertigo.quarto.services.export.model.Export;
import io.vertigo.quarto.services.export.model.ExportField;
import io.vertigo.quarto.services.export.model.ExportSheet;

/**
 * Export XLS.
 * Uses POI.
 *
 * @author pchretien, npiedeloup
 */
final class XLSExporter {
	private static final int MAX_COLUMN_WIDTH = 50;

	private final Map<DtField, Map<Object, String>> referenceCache = new HashMap<>();
	private final Map<DtField, Map<Object, String>> denormCache = new HashMap<>();

	private final Map<DataType, HSSFCellStyle> evenHssfStyleCache = new EnumMap<>(DataType.class);
	private final Map<DataType, HSSFCellStyle> oddHssfStyleCache = new EnumMap<>(DataType.class);

	private final StoreManager storeManager;

	/**
	 * Constructor.
	 * @param storeManager Store manager
	 */
	XLSExporter(final StoreManager storeManager) {
		Assertion.checkNotNull(storeManager);
		//-----
		this.storeManager = storeManager;
	}

	private static HSSFCellStyle createHeaderCellStyle(final HSSFWorkbook workbook) {
		final HSSFCellStyle cellStyle = workbook.createCellStyle();
		final HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setVerticalAlignment((short) 3);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	private static HSSFCellStyle createRowCellStyle(final HSSFWorkbook workbook, final boolean odd) {
		final HSSFCellStyle cellStyle = workbook.createCellStyle();
		final HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		cellStyle.setFillForegroundColor(odd ? HSSFColor.WHITE.index : HSSFColor.GREY_25_PERCENT.index);

		return cellStyle;
	}

	/**
	 * Réalise l'export des données de contenu et de la ligne d'en-tête.
	 *
	 * @param parameters Paramètre de cet export
	 * @param workbook Document excel
	 * @param sheet Feuille Excel
	 * @param forceLandscape Indique si le parametrage force un affichage en paysage
	 */
	private void exportData(final ExportSheet parameters, final HSSFWorkbook workbook, final HSSFSheet sheet, final boolean forceLandscape) {
		// Column width
		final Map<Integer, Double> maxWidthPerColumn = new HashMap<>();
		if (parameters.hasDtObject()) {
			exportObject(parameters, workbook, sheet, maxWidthPerColumn);
		} else {
			exportList(parameters, workbook, sheet, maxWidthPerColumn);
		}
		// On definit la largeur des colonnes:
		double totalWidth = 0;
		int cellIndex;
		for (final Map.Entry<Integer, Double> entry : maxWidthPerColumn.entrySet()) {
			cellIndex = entry.getKey();
			final Double maxLength = entry.getValue();
			final int usesMaxLength = Double.valueOf(Math.min(maxLength.doubleValue(), MAX_COLUMN_WIDTH)).intValue();
			sheet.setColumnWidth(cellIndex, usesMaxLength * 256);
			totalWidth += usesMaxLength;
		}
		/**
		 * @todo ne serait-il pas plus simple d'utilisersheet.autoSizeColumn(i); de poi 3.0.1 ?
		 */

		// note: il ne semble pas simple de mettre title et author dans les propriétés du document
		final String title = parameters.getTitle();
		if (title != null) {
			final HSSFHeader header = sheet.getHeader();
			header.setLeft(title);
		}
		sheet.setHorizontallyCenter(true);
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
		if (forceLandscape || totalWidth > 85) {
			sheet.getPrintSetup().setLandscape(true);
		}

		// On définit le footer
		final HSSFFooter footer = sheet.getFooter();
		footer.setRight("Page " + HeaderFooter.page() + " / " + HeaderFooter.numPages());
	}

	private void initHssfStyle(final HSSFWorkbook workbook) {
		// default:
		final HSSFCellStyle oddCellStyle = createRowCellStyle(workbook, true);
		final HSSFCellStyle evenCellStyle = createRowCellStyle(workbook, true);
		oddHssfStyleCache.put(DataType.Boolean, oddCellStyle);
		oddHssfStyleCache.put(DataType.String, oddCellStyle);
		evenHssfStyleCache.put(DataType.Boolean, evenCellStyle);
		evenHssfStyleCache.put(DataType.String, evenCellStyle);

		// Nombre sans décimal
		final HSSFCellStyle oddLongCellStyle = createRowCellStyle(workbook, true);
		final HSSFCellStyle evenLongCellStyle = createRowCellStyle(workbook, true);
		oddLongCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		evenLongCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		oddHssfStyleCache.put(DataType.Long, oddLongCellStyle);
		oddHssfStyleCache.put(DataType.Integer, oddLongCellStyle);
		evenHssfStyleCache.put(DataType.Long, evenLongCellStyle);
		evenHssfStyleCache.put(DataType.Integer, evenLongCellStyle);

		// Nombre a décimal
		final HSSFCellStyle oddDoubleCellStyle = createRowCellStyle(workbook, true);
		final HSSFCellStyle evenDoubleCellStyle = createRowCellStyle(workbook, true);
		oddDoubleCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		evenDoubleCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		oddHssfStyleCache.put(DataType.Double, oddDoubleCellStyle);
		oddHssfStyleCache.put(DataType.BigDecimal, oddDoubleCellStyle);
		evenHssfStyleCache.put(DataType.Double, evenDoubleCellStyle);
		evenHssfStyleCache.put(DataType.BigDecimal, evenDoubleCellStyle);

		// Date
		final HSSFCellStyle oddDateCellStyle = createRowCellStyle(workbook, true);
		final HSSFCellStyle evenDateCellStyle = createRowCellStyle(workbook, true);
		oddDateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy" /* "m/d/yy h:mm" */));
		evenDateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy" /* "m/d/yy h:mm" */));
		oddHssfStyleCache.put(DataType.Date, oddDateCellStyle);
		evenHssfStyleCache.put(DataType.Date, evenDateCellStyle);
		oddHssfStyleCache.put(DataType.LocalDate, oddDateCellStyle);
		evenHssfStyleCache.put(DataType.LocalDate, evenDateCellStyle);

		// Instant
		final HSSFCellStyle oddDateTimeCellStyle = createRowCellStyle(workbook, true);
		final HSSFCellStyle evenDateTimeCellStyle = createRowCellStyle(workbook, true);
		oddDateTimeCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		evenDateTimeCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		oddHssfStyleCache.put(DataType.Instant, oddDateTimeCellStyle);
		evenHssfStyleCache.put(DataType.Instant, evenDateTimeCellStyle);

	}

	private void exportList(final ExportSheet parameters, final HSSFWorkbook workbook, final HSSFSheet sheet, final Map<Integer, Double> maxWidthPerColumn) {
		// exporte le header
		final HSSFRow headerRow = sheet.createRow(0);
		int cellIndex = 0;
		for (final ExportField exportColumn : parameters.getExportFields()) {
			final HSSFCell cell = headerRow.createCell(cellIndex);
			final String displayedLabel = exportColumn.getLabel().getDisplay();
			cell.setCellValue(new HSSFRichTextString(displayedLabel));
			cell.setCellStyle(createHeaderCellStyle(workbook));

			updateMaxWidthPerColumn(displayedLabel, 1.2, cellIndex, maxWidthPerColumn); // +20% pour les majuscules
			cellIndex++;
		}
		//La premiere ligne est répétable
		sheet.setRepeatingRows(new CellRangeAddress(0, 0, -1, -1));

		int rowIndex = 1;
		for (final DtObject dto : parameters.getDtList()) {
			final HSSFRow row = sheet.createRow(rowIndex);
			cellIndex = 0;
			Object value;
			for (final ExportField exportColumn : parameters.getExportFields()) {
				final HSSFCell cell = row.createCell(cellIndex);

				value = ExportUtil.getValue(storeManager, referenceCache, denormCache, dto, exportColumn);
				putValueInCell(value, cell, rowIndex % 2 == 0 ? evenHssfStyleCache : oddHssfStyleCache, cellIndex, maxWidthPerColumn, exportColumn.getDtField().getDomain());

				cellIndex++;
			}
			rowIndex++;
		}
	}

	private void exportObject(final ExportSheet parameters, final HSSFWorkbook workbook, final HSSFSheet sheet, final Map<Integer, Double> maxWidthPerColumn) {
		int rowIndex = 0;
		final int labelCellIndex = 0;
		final int valueCellIndex = 1;
		final DtObject dto = parameters.getDtObject();
		Object value;
		for (final ExportField exportColumn : parameters.getExportFields()) {
			final HSSFRow row = sheet.createRow(rowIndex);

			final HSSFCell cell = row.createCell(labelCellIndex);
			final MessageText label = exportColumn.getLabel();
			cell.setCellValue(new HSSFRichTextString(label.getDisplay()));
			cell.setCellStyle(createHeaderCellStyle(workbook));
			updateMaxWidthPerColumn(label.getDisplay(), 1.2, labelCellIndex, maxWidthPerColumn); // +20% pour les majuscules

			final HSSFCell valueCell = row.createCell(valueCellIndex);
			value = ExportUtil.getValue(storeManager, referenceCache, denormCache, dto, exportColumn);
			putValueInCell(value, valueCell, oddHssfStyleCache, valueCellIndex, maxWidthPerColumn, exportColumn.getDtField().getDomain());
			rowIndex++;
		}

	}

	private static void putValueInCell(final Object value, final HSSFCell cell, final Map<DataType, HSSFCellStyle> rowCellStyle, final int cellIndex, final Map<Integer, Double> maxWidthPerColumn, final Domain domain) {
		String stringValueForColumnWidth;
		cell.setCellStyle(rowCellStyle.get(domain.getDataType()));
		if (value != null) {
			stringValueForColumnWidth = String.valueOf(value);

			if (value instanceof String) {
				final String stringValue = (String) value;
				cell.setCellValue(new HSSFRichTextString(stringValue));
			} else if (value instanceof Integer) {
				final Integer integerValue = (Integer) value;
				cell.setCellValue(integerValue.doubleValue());
			} else if (value instanceof Double) {
				final Double dValue = (Double) value;
				cell.setCellValue(dValue.doubleValue());
				stringValueForColumnWidth = String.valueOf(Math.round(dValue.doubleValue() * 100) / 100D);
			} else if (value instanceof Long) {
				final Long lValue = (Long) value;
				cell.setCellValue(lValue.doubleValue());
			} else if (value instanceof BigDecimal) {
				final BigDecimal bigDecimalValue = (BigDecimal) value;
				cell.setCellValue(bigDecimalValue.doubleValue());
				stringValueForColumnWidth = String.valueOf(Math.round(bigDecimalValue.doubleValue() * 100) / 100D);
			} else if (value instanceof Boolean) {
				final Boolean bValue = (Boolean) value;
				//cell.setCellValue(bValue.booleanValue() ? "Oui" : "Non");
				cell.setCellValue(domain.valueToString(bValue.booleanValue()));
			} else if (value instanceof Date) {
				final Date dateValue = (Date) value;
				// sans ce style "date" les dates apparaîtraient au format
				// "nombre"
				cell.setCellValue(dateValue);
				stringValueForColumnWidth = "DD/MM/YYYY";
				// ceci ne sert que pour déterminer la taille de la cellule, on a pas besoin de la vrai valeur
			} else {
				throw new UnsupportedOperationException("Type " + domain.getDataType() + " not supported by this Excel exporter");
			}
			updateMaxWidthPerColumn(stringValueForColumnWidth, 1, cellIndex, maxWidthPerColumn); // +20% pour les majuscules
		}
	}

	private static void updateMaxWidthPerColumn(final String value, final double textSizeCoeff, final int cellIndex, final Map<Integer, Double> maxWidthPerColumn) {
		// Calcul de la largeur des colonnes
		final double newLenght = value != null ? value.length() * textSizeCoeff + 2 : 0; // +textSizeCoeff% pour les majuscules, et +2 pour les marges
		final Double oldLenght = maxWidthPerColumn.get(cellIndex);
		if (oldLenght == null || oldLenght.doubleValue() < newLenght) {
			maxWidthPerColumn.put(cellIndex, newLenght);
		}
	}

	/**
	 * Méthode principale qui gère l'export d'un tableau vers un fichier ODS.
	 *
	 * @param documentParameters Paramètres du document à exporter
	 * @param out Flux de sortie
	 * @throws IOException Io exception
	 */
	void exportData(final Export documentParameters, final OutputStream out) throws IOException {
		// Workbook
		final boolean forceLandscape = Export.Orientation.Landscape == documentParameters.getOrientation();
		try (final HSSFWorkbook workbook = new HSSFWorkbook()) {
			initHssfStyle(workbook);
			for (final ExportSheet exportSheet : documentParameters.getSheets()) {
				final String title = exportSheet.getTitle();
				final HSSFSheet sheet = title == null ? workbook.createSheet() : workbook.createSheet(title);
				exportData(exportSheet, workbook, sheet, forceLandscape);

			}
			workbook.write(out);
		}
	}
}
