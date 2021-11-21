package output.xlsx;

import dto.DividendCalculated;
import dto.DocumentCalculated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class DividendWriter {

    private static final int numberOfColumns = 10;
    private static final int headerColSpan = 7;

    private static final String listName = "Dividends";

    private static final String tickerColumnName = "Ticker";
    private static final String paymentDateColumnName = "Payment Date";
    private static final String dividendGrossColumnName = "Dividend gross";
    private static final String dividendNetColumnName = "Dividend net";
    private static final String taxColumnName = "Tax";
    private static final String dividendRubColumnName = "Dividend rub";
    private static final String expectedDividendRubColumnName = "Expected tax rub";
    private static final String payedTaxRubColumnName = "Payed tax rub";
    private static final String resultColumnName = "Result";
    private static final String exchangeRateColumnName = "Exchange rate";
    private static final String finalTaxColumnName = "Sum tax to pay:";


    public XSSFWorkbook writeDividends(DocumentCalculated dc, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(listName);

        int rowCount = 0;

        rowCount = setSheetHeader(sheet, rowCount);

        rowCount = writeDividends(workbook, sheet, dc.getDividends(), rowCount);
        rowCount = writeResult(workbook, sheet, rowCount, dc.getDividendResult());
        XlsWriter.autoSizeColumn(sheet, 10);
        return workbook;
    }

    private int setSheetHeader(XSSFSheet sheet, int rowCount) {
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
        Cell cell10 = row.createCell(++columnCount);
        cell10.setCellValue(exchangeRateColumnName);
        rowCount++;
        return rowCount;
    }

    private int writeDividends(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<DividendCalculated> list, int rowCount) {
        for (DividendCalculated d : list) {
            CellStylesProvider cellStylesProvider = new CellStylesProvider();
            CellStyle dateCellStyle = cellStylesProvider.getDateCellStyle(workbook, sheet);
            CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);
            CellStyle exchangeRateCellStyle = cellStylesProvider.getExchangeRateCellStyle(workbook, sheet);

            int columnCount = 0;
            Row rowD = sheet.createRow(rowCount++);
            Cell cell1 = rowD.createCell(columnCount);
            cell1.setCellValue(d.getTicker());
            Cell cell2 = rowD.createCell(++columnCount);
            cell2.setCellStyle(dateCellStyle);
            cell2.setCellValue(d.getPaymentDate());
            Cell cell3 = rowD.createCell(++columnCount);
            cell3.setCellStyle(doubleCellStyle);
            cell3.setCellValue(d.getDividendGross());
            Cell cell4 = rowD.createCell(++columnCount);
            cell4.setCellStyle(doubleCellStyle);
            cell4.setCellValue(d.getDividendNet());
            Cell cell5 = rowD.createCell(++columnCount);
            cell5.setCellStyle(doubleCellStyle);
            cell5.setCellValue(d.getTax());
            Cell cell6 = rowD.createCell(++columnCount);
            cell6.setCellStyle(doubleCellStyle);
            cell6.setCellValue(d.getDividendRub());
            Cell cell7 = rowD.createCell(++columnCount);
            cell7.setCellStyle(doubleCellStyle);
            cell7.setCellValue(d.getExpectedDividendRub());
            Cell cell8 = rowD.createCell(++columnCount);
            cell8.setCellStyle(doubleCellStyle);
            cell8.setCellValue(d.getPayedTaxRub());
            Cell cell9 = rowD.createCell(++columnCount);
            cell9.setCellStyle(doubleCellStyle);
            cell9.setCellValue(d.getResult());
            Cell cell10 = rowD.createCell(++columnCount);
            cell10.setCellStyle(exchangeRateCellStyle);
            cell10.setCellValue(d.getExchangeRate());
        }
        rowCount++;
        return rowCount;
    }

    private int writeResult(XSSFWorkbook workbook, XSSFSheet sheet, int rowCount, double result) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        int columnCount = 0;
        Row rowD = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, rowD, rowCount, columnCount, columnCount+headerColSpan, finalTaxColumnName);

        Cell cell3 = rowD.createCell(columnCount+headerColSpan+1);
        cell3.setCellStyle(doubleCellStyle);
        cell3.setCellValue(result);
        rowCount++;
        return rowCount;
    }
}
