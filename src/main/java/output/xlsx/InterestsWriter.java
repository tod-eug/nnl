package output.xlsx;

import dto.DocumentCalculated;
import dto.InterestCalculated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class InterestsWriter {

    private static final int numberOfColumns = 5;
    private static final int headerColSpan = 4;

    private static final String headerColumnName = "Interests:";
    private static final String dateColumnName = "Date";
    private static final String descriptionColumnName = "Description";
    private static final String amountColumnName = "Amount";
    private static final String amountRubColumnName = "Amount Rub";
    private static final String taxRubColumnName = "Tax Rub";
    private static final String interestResultColumnName = "Sum received interest:";

    XSSFWorkbook writeInterests(DocumentCalculated documentCalculated, XSSFWorkbook workbook, XSSFSheet sheet) {

        int rowCount = 0;

        rowCount = setHeader(sheet, rowCount);
        rowCount++;
        rowCount = setSheetHeader(sheet, rowCount);
        rowCount = writeRows(workbook, sheet, documentCalculated.getInterests(), rowCount);
        rowCount++;
        rowCount = writeResult(workbook, sheet, rowCount, documentCalculated.getInterestsTaxResult());

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
        cell1.setCellValue(dateColumnName);
        Cell cell2 = row.createCell(++columnCount);
        cell2.setCellValue(descriptionColumnName);
        Cell cell3 = row.createCell(++columnCount);
        cell3.setCellValue(amountColumnName);
        Cell cell4 = row.createCell(++columnCount);
        cell4.setCellValue(amountRubColumnName);
        Cell cell5 = row.createCell(++columnCount);
        cell5.setCellValue(taxRubColumnName);
        rowCount++;
        return rowCount;
    }

    private int writeRows(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<InterestCalculated> list, int rowCount) {
        for (InterestCalculated i : list) {
            CellStylesProvider cellStylesProvider = new CellStylesProvider();
            CellStyle dateCellStyle = cellStylesProvider.getDateCellStyle(workbook, sheet);
            CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

            int columnCount = 0;
            Row rowD = sheet.createRow(rowCount++);
            Cell cell1 = rowD.createCell(columnCount);
            cell1.setCellStyle(dateCellStyle);
            cell1.setCellValue(i.getDate());
            Cell cell2 = rowD.createCell(++columnCount);
            cell2.setCellValue(i.getDescription());
            Cell cell3 = rowD.createCell(++columnCount);
            cell3.setCellStyle(doubleCellStyle);
            cell3.setCellValue(i.getAmount());
            Cell cell4 = rowD.createCell(++columnCount);
            cell4.setCellStyle(doubleCellStyle);
            cell4.setCellValue(i.getAmountRub());
            Cell cell5 = rowD.createCell(++columnCount);
            cell5.setCellStyle(doubleCellStyle);
            cell5.setCellValue(i.getTaxRub());
        }
        rowCount++;
        return rowCount;
    }

    private int writeResult(XSSFWorkbook workbook, XSSFSheet sheet, int rowCount, double result) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        int columnCount = 0;
        Row rowD = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, rowD, rowCount, columnCount, columnCount+headerColSpan, interestResultColumnName);

        Cell cell3 = rowD.createCell(columnCount+headerColSpan);
        cell3.setCellStyle(doubleCellStyle);
        cell3.setCellValue(result);
        rowCount++;
        return rowCount;
    }
}
