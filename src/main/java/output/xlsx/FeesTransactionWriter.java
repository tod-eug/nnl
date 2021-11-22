package output.xlsx;

import dto.DocumentCalculated;
import dto.FeesTransactionsCalculated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class FeesTransactionWriter {

    private static final int numberOfColumns = 7;
    private static final int headerColSpan = 6;

    private static final String headerColumnName = "Transactions Fees:";
    private static final String tickerColumnName = "Ticker";
    private static final String dateColumnName = "Date";
    private static final String descriptionColumnName = "Description";
    private static final String quantityColumnName = "Quantity";
    private static final String tradePriceColumnName = "Trade price";
    private static final String amountColumnName = "Amount";
    private static final String amountRubColumnName = "Amount Rub";
    private static final String resultColumnName = "Sum payed transactions fees:";

    XSSFWorkbook writeFeesTransactions(DocumentCalculated documentCalculated, XSSFWorkbook workbook, XSSFSheet sheet) {

        int rowCount = sheet.getLastRowNum() + 2;

        rowCount = setHeader(sheet, rowCount);
        rowCount++;
        rowCount = setSheetHeader(sheet, rowCount);
        rowCount = writeRows(workbook, sheet, documentCalculated.getFeesTransactions(), rowCount);
        rowCount++;
        rowCount = writeResult(workbook, sheet, rowCount, documentCalculated.getFeesTransactionsResult());

        XlsWriter.autoSizeColumn(sheet, numberOfColumns);
        return workbook;

    }

    private int setHeader(XSSFSheet sheet, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row, rowCount, columnCount, columnCount+headerColSpan, headerColumnName);
        return rowCount;
    }

    private int setSheetHeader(XSSFSheet sheet, int rowCount) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(tickerColumnName);
        Cell cell2 = row.createCell(++columnCount);
        cell2.setCellValue(dateColumnName);
        Cell cell3 = row.createCell(++columnCount);
        cell3.setCellValue(descriptionColumnName);
        Cell cell4 = row.createCell(++columnCount);
        cell4.setCellValue(quantityColumnName);
        Cell cell5 = row.createCell(++columnCount);
        cell5.setCellValue(tradePriceColumnName);
        Cell cell6 = row.createCell(++columnCount);
        cell6.setCellValue(amountColumnName);
        Cell cell7 = row.createCell(++columnCount);
        cell7.setCellValue(amountRubColumnName);
        rowCount++;
        return rowCount;
    }

    private int writeRows(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<FeesTransactionsCalculated> list, int rowCount) {
        for (FeesTransactionsCalculated ft : list) {
            CellStylesProvider cellStylesProvider = new CellStylesProvider();
            CellStyle dateCellStyle = cellStylesProvider.getDateCellStyle(workbook, sheet);
            CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

            int columnCount = 0;
            Row rowD = sheet.createRow(rowCount++);
            Cell cell1 = rowD.createCell(columnCount);
            cell1.setCellValue(ft.getTicker());
            Cell cell2 = rowD.createCell(++columnCount);
            cell2.setCellStyle(dateCellStyle);
            cell2.setCellValue(ft.getDate());
            Cell cell3 = rowD.createCell(++columnCount);
            cell3.setCellValue(ft.getDescription());
            Cell cell4 = rowD.createCell(++columnCount);
            cell4.setCellStyle(doubleCellStyle);
            cell4.setCellValue(ft.getQuantity());
            Cell cell5 = rowD.createCell(++columnCount);
            cell5.setCellStyle(doubleCellStyle);
            cell5.setCellValue(ft.getTradePrice());
            Cell cell6 = rowD.createCell(++columnCount);
            cell6.setCellStyle(doubleCellStyle);
            cell6.setCellValue(ft.getAmount());
            Cell cell7 = rowD.createCell(++columnCount);
            cell7.setCellStyle(doubleCellStyle);
            cell7.setCellValue(ft.getAmountRub());
        }
        rowCount++;
        return rowCount;
    }

    private int writeResult(XSSFWorkbook workbook, XSSFSheet sheet, int rowCount, double result) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        int columnCount = 0;
        Row rowD = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, rowD, rowCount, columnCount, columnCount+headerColSpan, resultColumnName);

        Cell cell3 = rowD.createCell(columnCount+headerColSpan);
        cell3.setCellStyle(doubleCellStyle);
        cell3.setCellValue(result);
        rowCount++;
        return rowCount;
    }
}
