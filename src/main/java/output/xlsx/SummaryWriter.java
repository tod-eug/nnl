package output.xlsx;

import dto.DocumentCalculated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SummaryWriter {

    private static final int numberOfColumns = 5;
    private static final int headerColSpan = 4;

    private static final String listName = "Summary";

    private static final String headerColumnName = "Brief summary:";

    private static final String finalTaxResultColumnName = "Final tax result to pay:";
    private static final String dividendResultColumnName = "Dividends tax:";
    private static final String tradesResultColumnName = "Trades tax:";
    private static final String interestResultColumnName = "Interest received tax:";
    private static final String finalDeductionResultColumnName = "Final tax deduction:";
    private static final String tradesDeductionColumnName = "Trades deduction:";
    private static final String feesColumnName = "Fees:";
    private static final String feesTransactionColumnName = "Transaction fees:";
    private static final String ofThemColumnName = "Of them:";

    XSSFWorkbook writeSummary(DocumentCalculated documentCalculated, XSSFWorkbook workbook) {

        XSSFSheet sheet = workbook.createSheet(listName);

        int rowCount = 0;

        rowCount = setHeader(sheet, rowCount);
        rowCount++;
        rowCount = writeRows(workbook, sheet, documentCalculated, rowCount);

        XlsWriter.autoSizeColumn(sheet, numberOfColumns);
        return workbook;
    }

    private int setHeader(XSSFSheet sheet, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row, rowCount, columnCount, columnCount+headerColSpan, headerColumnName);
        rowCount++;
        return rowCount;
    }

    private int writeRows(XSSFWorkbook workbook, XSSFSheet sheet, DocumentCalculated dc, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        rowCount = addSummaryRow(sheet, rowCount, doubleCellStyle,
                finalTaxResultColumnName, dc.getFinalTaxResult(),
                finalDeductionResultColumnName, dc.getFinalDeductionResult());

        int columnCount = 0;
        Row row2 = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row2, rowCount, columnCount, 1, ofThemColumnName);
        Cell cell22 = row2.createCell(++columnCount);
        Cell cell23 = row2.createCell(++columnCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row2, rowCount, ++columnCount, 1, ofThemColumnName);
        rowCount++;

        rowCount = addSummaryRow(sheet, rowCount, doubleCellStyle,
                dividendResultColumnName, dc.getDividendResult(),
                tradesDeductionColumnName, dc.getTradesDeductionResult());

        rowCount = addSummaryRow(sheet, rowCount, doubleCellStyle,
                tradesResultColumnName, dc.getTradesTaxResult(),
                feesColumnName, dc.getFeesResult());

        rowCount = addSummaryRow(sheet, rowCount, doubleCellStyle,
                interestResultColumnName, dc.getInterestsTaxResult(),
                feesTransactionColumnName, dc.getFeesTransactionsResult());

        return rowCount;
    }

    private int addSummaryRow(XSSFSheet sheet, int rowCount, CellStyle doubleCellStyle,
                              String parameter1, double value1, String parameter2, double value2) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(parameter1);
        Cell cell2 = row.createCell(++columnCount);
        cell2.setCellStyle(doubleCellStyle);
        cell2.setCellValue(value1);
        Cell cell3 = row.createCell(++columnCount);
        Cell cell4 = row.createCell(++columnCount);
        cell4.setCellValue(parameter2);
        Cell cell5 = row.createCell(++columnCount);
        cell5.setCellStyle(doubleCellStyle);
        cell5.setCellValue(value2);
        rowCount++;
        return rowCount;
    }
}
