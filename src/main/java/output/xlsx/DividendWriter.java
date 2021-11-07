package output.xlsx;

import dto.DividendCalculated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class DividendWriter {

    private static final String listName = "Dividends";

    private static final String tickerColumnName = "Ticker";
    private static final String paymentDateColumnName = "Payment Date";
    private static final String dividendGrossColumnName = "Dividend gross";
    private static final String dividendNetColumnName = "Dividend net";
    private static final String taxColumnName = "Tax";
    private static final String dividendRubColumnName = "Dividend rub";
    private static final String expectedDividendRubColumnName = "Expected dividend rub";
    private static final String payedTaxRubColumnName = "Payed tax rub";
    private static final String resultColumnName = "Result";


    public static XSSFWorkbook writeDividends(ArrayList<DividendCalculated> list, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(listName);

        int rowCount = 0;

        rowCount = setSheetHeader(sheet, rowCount);

        writeDividends(workbook, sheet, list, rowCount);
        XlsWriter.autoSizeColumn(sheet, 10);
        return workbook;
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
            CellStylesProvider cellStylesProvider = new CellStylesProvider();
            CellStyle dateCellStyle = cellStylesProvider.getDateCellStyle(workbook, sheet);
            CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

            int columnCount = 0;
            Row rowD = sheet.createRow(rowCount++);
            Cell cellD1 = rowD.createCell(columnCount);
            cellD1.setCellValue(d.getTicker());
            Cell cellD2 = rowD.createCell(++columnCount);
            cellD2.setCellStyle(dateCellStyle);
            cellD2.setCellValue(d.getPaymentDate());
            Cell cellD3 = rowD.createCell(++columnCount);
            cellD3.setCellStyle(doubleCellStyle);
            cellD3.setCellValue(d.getDividendGross());
            Cell cellD4 = rowD.createCell(++columnCount);
            cellD4.setCellStyle(doubleCellStyle);
            cellD4.setCellValue(d.getDividendNet());
            Cell cellD5 = rowD.createCell(++columnCount);
            cellD5.setCellStyle(doubleCellStyle);
            cellD5.setCellValue(d.getTax());
            Cell cellD6 = rowD.createCell(++columnCount);
            cellD6.setCellStyle(doubleCellStyle);
            cellD6.setCellValue(d.getDividendRub());
            Cell cellD7 = rowD.createCell(++columnCount);
            cellD7.setCellStyle(doubleCellStyle);
            cellD7.setCellValue(d.getExpectedDividendRub());
            Cell cellD8 = rowD.createCell(++columnCount);
            cellD8.setCellStyle(doubleCellStyle);
            cellD8.setCellValue(d.getPayedTaxRub());
            Cell cellD9 = rowD.createCell(++columnCount);
            cellD9.setCellStyle(doubleCellStyle);
            cellD9.setCellValue(d.getResult());
        }
    }
}
