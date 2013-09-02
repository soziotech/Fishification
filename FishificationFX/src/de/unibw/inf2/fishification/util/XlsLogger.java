/*
 * XlsLogger.java
 *
 * Copyright (c) 2013 Martin Burkhard and Sonja Maier.
 * CSCM Cooperation Systems Center Munich, Institute for Software Technology.
 * Bundeswehr University Munich. All rights reserved.
 *
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at <http://www.eclipse.org/legal/epl-v10.html>.
 *
 * The accompanying materials are made available under the terms
 * of Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * You should have received a copy of the license along with this
 * work.  If not, see <http://creativecommons.org/licenses/by-sa/3.0/>.
 *
 *  Project: FishificationFX
 *   Author: Martin Burkhard
 *     Date: 9/3/13 12:05 AM
 */

package de.unibw.inf2.fishification.util;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;

/**
 * Logs to Xls.
 */
public class XlsLogger {

    private final File   m_outputDirectory;
    private final String m_fileName;
    private final String m_sheetName;
    private final String m_applicationVersion;

    private static final String LOCALE_LANGUAGE = "de";
    private static final String LOCALE_COUNTRY  = "DE";
    private static final int    SHEET           = 0;
    private static final int    HEADER_ROW      = 0;
    private final        Logger m_log           = LogManager.getLogger();

    public XlsLogger(File outputDirectory, String fileName, String sheetName, String applicationVersion) {
        m_outputDirectory = outputDirectory;
        m_fileName = fileName;
        m_sheetName = sheetName;
        m_applicationVersion = applicationVersion;
    }

    public void writeData(XlsLoggerEntry xlsLoggerEntry) {
        String machineName = "";
        try {
            machineName = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException e) {
            m_log.warn("Error getting local host name.");
        }
        writeData(machineName, xlsLoggerEntry);
    }

    void writeData(String machine, XlsLoggerEntry xlsLoggerEntry) {

        try {

            // Open Workbook for writing
            WritableWorkbook workbook = openWritableWorkbook(m_outputDirectory, m_fileName, m_sheetName);
            WritableSheet sheet = workbook.getSheet(SHEET);

            // Get next data row
            int rowIndex = sheet.getRows();

            // Place data in cells
            int columnIndex = 0;
            createLabelCell(sheet, columnIndex++, rowIndex, m_applicationVersion);
            createLabelCell(sheet, columnIndex++, rowIndex, machine);
            createNumberCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getAmountOfFishes());
            createDateCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getDateTime(), "dd.MM.yyyy");
            createDateCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getDateTime(), "hh:mm:ss.000");
            createLabelCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getFishType());
            createFloatCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getFishPosX());
            createFloatCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getFishPosY());
            createLabelCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getIntention());
            createLabelCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getInteraction());
            createFloatCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getInteractionPosX());
            createFloatCell(sheet, columnIndex++, rowIndex, xlsLoggerEntry.getInteractionPosY());
            createLabelCell(sheet, columnIndex, rowIndex, xlsLoggerEntry.getContent().getFlagTitle());

            // Persist data
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            m_log.error(MarkerManager.getMarker("EXCEPTION"), "Error while writing to XLS file.", e);
        }
    }

    private WritableWorkbook openWritableWorkbook(File outputDirectory, String fileName, String sheetName) throws IOException, BiffException, WriteException {

        WritableWorkbook workbook;

        File outputDir = new File(outputDirectory.getAbsolutePath());

        // Initialize output path
        if (!outputDir.exists()) {
            boolean mkdirsSuccess = outputDirectory.mkdirs();
            if (!mkdirsSuccess) {
                m_log.warn("Logger directory could not be created.");
            }
        }

        // Initialize workbook file handle
        File outputFile = new File(outputDir, fileName);

        // Check if file already exists
        if (!outputFile.exists()) {

            // Create a new workbook and sheet
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY));
            workbook = Workbook.createWorkbook(outputFile, wbSettings);
            WritableSheet sheet = workbook.createSheet(sheetName, SHEET);

            int rowIndex = sheet.getRows();

            // write initial XLS header
            if (rowIndex == HEADER_ROW) {
                int columnIndex = 0;
                createLabelCell(sheet, columnIndex++, rowIndex, "version");
                createLabelCell(sheet, columnIndex++, rowIndex, "machine");
                createLabelCell(sheet, columnIndex++, rowIndex, "fishworld amount");
                createLabelCell(sheet, columnIndex++, rowIndex, "date");
                createLabelCell(sheet, columnIndex++, rowIndex, "time");
                createLabelCell(sheet, columnIndex++, rowIndex, "fishworld type");
                createLabelCell(sheet, columnIndex++, rowIndex, "fishworld posX");
                createLabelCell(sheet, columnIndex++, rowIndex, "fishworld posY");
                createLabelCell(sheet, columnIndex++, rowIndex, "intention type");
                createLabelCell(sheet, columnIndex++, rowIndex, "interaction type");
                createLabelCell(sheet, columnIndex++, rowIndex, "interaction posX");
                createLabelCell(sheet, columnIndex++, rowIndex, "interaction posY");
                createLabelCell(sheet, columnIndex, rowIndex, "content title");

                sheet.getRows();
            }
            workbook.write();
            workbook.close();
        }

        // Read in existing workbook and get a writeable copy
        Workbook readableWorkbook = Workbook.getWorkbook(outputFile);
        workbook = Workbook.createWorkbook(outputFile, readableWorkbook);

        return workbook;
    }

    private void createLabelCell(WritableSheet sheet, int col, int row, String value) throws WriteException {
        Label label = new Label(col, row, value);
        sheet.addCell(label);
    }

    private void createNumberCell(WritableSheet sheet, int col, int row, int value) throws WriteException {
        Number number = new Number(col, row, value);
        sheet.addCell(number);
    }

    private void createFloatCell(WritableSheet sheet, int col, int row, double value) throws WriteException {
        WritableCellFormat cf = new WritableCellFormat(NumberFormats.FLOAT);
        Number number = new Number(col, row, value, cf);
        sheet.addCell(number);
    }

    private void createDateCell(WritableSheet sheet, int col, int row, Date date, String format) throws WriteException {
        DateFormat customDateFormat = new DateFormat(format);
        WritableCellFormat cf = new WritableCellFormat(customDateFormat);
        DateTime dateCell = new DateTime(col, row, date, cf);
        sheet.addCell(dateCell);

    }

}
