package output.xlsx;

import dto.DividendCalculated;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XlsWriter {

    private static final String fileName = "Taxes.xlsx";

    private static final String listName = "Taxes";

    private static final String tickerColumnName = "Ticker";
    private static final String paymentDateColumnName = "Payment Date";
    private static final String dividendGrossColumnName = "Dividend gross";
    private static final String dividendNetColumnName = "Dividend net";
    private static final String taxColumnName = "Tax";
    private static final String dividendRubColumnName = "Dividend rub";
    private static final String expectedDividendRubColumnName = "Expected dividend rub";
    private static final String payedTaxRubColumnName = "Payed tax rub";
    private static final String resultColumnName = "Result";

    private static final String dateFormat = "dd.mm.yyyy";
    private static final String doubleFormat = "0.00";

    public static void writeXlsFile(ArrayList<DividendCalculated> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(listName);

        int rowCount = 0;

        rowCount = setSheetHeader(sheet, rowCount);

        writeDividends(workbook, sheet, list, rowCount);
        autoSizeColumn(sheet, 10);

        writeFile(workbook);
    }

    private static int setSheetHeader(XSSFSheet sheet, int rowCount) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(tickerColumnName);
        Cell cell2 = row.createCell(++columnCount);
        cell2.setCellValue(paymentDateColumnName);
        Cell cell3 = row.createCell(++columnCount);
        cell3.setCellValue(dividendGrossColumnName);
        Cell cell4 = row.createCell(++columnCount);
        cell4.setCellValue(dividendNetColumnName);
        Cell cell5 = row.createCell(++columnCount);
        cell5.setCellValue(taxColumnName);
        Cell cell6 = row.createCell(++columnCount);
        cell6.setCellValue(dividendRubColumnName);
        Cell cell7 = row.createCell(++columnCount);
        cell7.setCellValue(expectedDividendRubColumnName);
        Cell cell8 = row.createCell(++columnCount);
        cell8.setCellValue(payedTaxRubColumnName);
        Cell cell9 = row.createCell(++columnCount);
        cell9.setCellValue(resultColumnName);
        rowCount++;
        return rowCount;
    }

    private static void writeDividends(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<DividendCalculated> list, int rowCount) {
        for (DividendCalculated d : list) {
            int columnCount = 0;
            Row rowD = sheet.createRow(rowCount++);
            Cell cellD1 = rowD.createCell(columnCount);
            cellD1.setCellValue(d.getTicker());
            Cell cellD2 = rowD.createCell(++columnCount);
            cellD2.setCellStyle(XlsWriter.getDateCellStyle(workbook, sheet));
            cellD2.setCellValue(d.getPaymentDate());
            Cell cellD3 = rowD.createCell(++columnCount);
            cellD3.setCellStyle(XlsWriter.getDoubleCellStyle(workbook, sheet));
            cellD3.setCellValue(d.getDividendGross());
            Cell cellD4 = rowD.createCell(++columnCount);
            cellD4.setCellStyle(XlsWriter.getDoubleCellStyle(workbook, sheet));
            cellD4.setCellValue(d.getDividendNet());
            Cell cellD5 = rowD.createCell(++columnCount);
            cellD5.setCellStyle(XlsWriter.getDoubleCellStyle(workbook, sheet));
            cellD5.setCellValue(d.getTax());
            Cell cellD6 = rowD.createCell(++columnCount);
            cellD6.setCellStyle(XlsWriter.getDoubleCellStyle(workbook, sheet));
            cellD6.setCellValue(d.getDividendRub());
            Cell cellD7 = rowD.createCell(++columnCount);
            cellD7.setCellStyle(XlsWriter.getDoubleCellStyle(workbook, sheet));
            cellD7.setCellValue(d.getExpectedDividendRub());
            Cell cellD8 = rowD.createCell(++columnCount);
            cellD8.setCellStyle(XlsWriter.getDoubleCellStyle(workbook, sheet));
            cellD8.setCellValue(d.getPayedTaxRub());
            Cell cellD9 = rowD.createCell(++columnCount);
            cellD9.setCellStyle(XlsWriter.getDoubleCellStyle(workbook, sheet));
            cellD9.setCellValue(d.getResult());
        }
    }

    private static CellStyle getDateCellStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle dateCell = sheet.getWorkbook().createCellStyle();
        dateCell.setDataFormat(createHelper.createDataFormat().getFormat(XlsWriter.dateFormat));
        return dateCell;
    }

    private static CellStyle getDoubleCellStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle doubleCell = sheet.getWorkbook().createCellStyle();
        doubleCell.setDataFormat(createHelper.createDataFormat().getFormat(XlsWriter.doubleFormat));
        return doubleCell;
    }

    private static void autoSizeColumn(XSSFSheet sheet, int length) {
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i, false);
        }
    }
    private static void writeFile(XSSFWorkbook workbook) {
        try (FileOutputStream outputStream = new FileOutputStream(XlsWriter.fileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
