package output.xlsx;

import dto.DocumentCalculated;
import dto.TradeCalculated;
import dto.TradeResultsForInstrument;
import dto.Trades;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Set;

public class TradesWriter {

    private static final int numberOfColumns = 9;
    private static final int tradeResultHeaderColSpan = 3;
    private static final int instrumentResultHeaderColSpan = 5;
    private static final int finalResultHeaderColSpan = 5;

    private static final String listName = "Trades";

    private static final String dateColumnName = "Date";
    private static final String quantityColumnName = "Quantity";
    private static final String tradePriceColumnName = "Price";
    private static final String sumColumnName = "Sum";
    private static final String sumRubColumnName = "Sum rub";
    private static final String commissionColumnName = "Commission";
    private static final String commissionRubColumnName = "Commission rub";
    private static final String currencyRubColumnName = "Currency";
    private static final String exchangeRateColumnName = "Exchange rate";
    private static final String finalPLColumnName = "Final PL:";
    private static final String taxRubColumnName = "Tax rub:";
    private static final String deductionRubColumnName = "Deduction:";
    private static final String sumTaxForInstrumentRubColumnName = "Sum tax to pay for: ";
    private static final String sumDeductionForInstrumentRubColumnName = "Sum deduction to pay for: ";
    private static final String finalTaxColumnName = "Sum tax to pay:";
    private static final String finalDeductionColumnName = "Sum deduction:";


    public XSSFWorkbook writeTrades(DocumentCalculated dc, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(listName);

        int rowCount = 0;

        Set<String> instruments = dc.getTrades().keySet();
        for (String instrument : instruments) {
            rowCount = setInstrumentHeader(sheet, instrument, rowCount);
            rowCount++;
            rowCount++;

            List<Trades> list = dc.getTrades().get(instrument);

            for (Trades t : list) {
                List<TradeCalculated> purchases = t.getPurchases();
                List<TradeCalculated> sells = t.getSells();

                //if position was reduced or closed then do something, otherwise ignore trades with this equity
                if (sells.size() > 0) {
                    rowCount = setTickerHeader(sheet, t.getTicker(), rowCount);
                    rowCount = setTradeHeader(sheet, rowCount);

                    if (purchases.size() > 0) {
                        for (TradeCalculated tc : purchases) {
                            rowCount = writeTrade(workbook, sheet, tc, rowCount);
                        }
                    }
                    if (sells.size() > 0) {
                        for (TradeCalculated tc : sells) {
                            rowCount = writeTrade(workbook, sheet, tc, rowCount);
                        }
                    }
                    rowCount++;
                    rowCount = writeTradeResult(workbook, sheet, t, rowCount);
                }
            }
            rowCount = writeInstrumentResult(workbook, sheet, instrument, dc.getFinalResultsByInstruments().get(instrument), rowCount);
        }
        rowCount = writeFinalResult(workbook, sheet, dc.getTradesTaxResult(), dc.getTradesDeductionResult(), rowCount);
        XlsWriter.autoSizeColumn(sheet, numberOfColumns);
        return workbook;
    }

    private int setInstrumentHeader(XSSFSheet sheet, String instrumentName, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row, rowCount, columnCount, columnCount+numberOfColumns-1, instrumentName);
        rowCount++;
        return rowCount;
    }

    private int setTickerHeader(XSSFSheet sheet, String tickerName, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row, rowCount, columnCount, columnCount+numberOfColumns-1, tickerName);
        rowCount++;
        return rowCount;
    }

    private int setTradeHeader(XSSFSheet sheet, int rowCount) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell2 = row.createCell(columnCount);
        cell2.setCellValue(dateColumnName);
        Cell cell3 = row.createCell(++columnCount);
        cell3.setCellValue(quantityColumnName);
        Cell cell4 = row.createCell(++columnCount);
        cell4.setCellValue(tradePriceColumnName);
        Cell cell5 = row.createCell(++columnCount);
        cell5.setCellValue(sumColumnName);
        Cell cell6 = row.createCell(++columnCount);
        cell6.setCellValue(sumRubColumnName);
        Cell cell7 = row.createCell(++columnCount);
        cell7.setCellValue(commissionColumnName);
        Cell cell8 = row.createCell(++columnCount);
        cell8.setCellValue(commissionRubColumnName);
        Cell cell12 = row.createCell(++columnCount);
        cell12.setCellValue(currencyRubColumnName);
        Cell cell14 = row.createCell(++columnCount);
        cell14.setCellValue(exchangeRateColumnName);
        rowCount++;
        return rowCount;
    }

    private int writeTrade(XSSFWorkbook workbook, XSSFSheet sheet, TradeCalculated t, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle dateCellStyle = cellStylesProvider.getDateCellStyle(workbook, sheet);
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);
        CellStyle exchangeRateCellStyle = cellStylesProvider.getExchangeRateCellStyle(workbook, sheet);

        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell2 = row.createCell(columnCount);
        cell2.setCellStyle(dateCellStyle);
        cell2.setCellValue(t.getDate());
        Cell cell3 = row.createCell(++columnCount);
        cell3.setCellStyle(doubleCellStyle);
        cell3.setCellValue(t.getQuantity());
        Cell cell4 = row.createCell(++columnCount);
        cell4.setCellStyle(doubleCellStyle);
        cell4.setCellValue(t.getTradePrice());
        Cell cell5 = row.createCell(++columnCount);
        cell5.setCellStyle(doubleCellStyle);
        cell5.setCellValue(t.getSum());
        Cell cell6 = row.createCell(++columnCount);
        cell6.setCellStyle(doubleCellStyle);
        cell6.setCellValue(t.getSumRub());
        Cell cell7 = row.createCell(++columnCount);
        cell7.setCellStyle(doubleCellStyle);
        cell7.setCellValue(t.getCommission());
        Cell cell8 = row.createCell(++columnCount);
        cell8.setCellStyle(doubleCellStyle);
        cell8.setCellValue(t.getCommissionRub());
        Cell cell12 = row.createCell(++columnCount);
        cell12.setCellValue(t.getCurrency());
        Cell cell14 = row.createCell(++columnCount);
        cell14.setCellStyle(exchangeRateCellStyle);
        cell14.setCellValue(t.getExchangeRate());
        rowCount++;
        return rowCount;
        }

    public int writeTradeResult(XSSFWorkbook workbook, XSSFSheet sheet, Trades t, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row, rowCount, columnCount, columnCount+tradeResultHeaderColSpan, finalPLColumnName);
        Cell cell2 = row.createCell(columnCount+tradeResultHeaderColSpan+1);
        cell2.setCellStyle(doubleCellStyle);
        cell2.setCellValue(t.getFinalPLRub());
        columnCount = 0;
        Row row1 = sheet.createRow(++rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row1, rowCount, columnCount, columnCount+tradeResultHeaderColSpan, taxRubColumnName);
        Cell cell12 = row1.createCell(columnCount+tradeResultHeaderColSpan+1);
        cell12.setCellStyle(doubleCellStyle);
        cell12.setCellValue(t.getTaxRub());
        columnCount = 0;
        Row row2 = sheet.createRow(++rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row2, rowCount, columnCount, columnCount+tradeResultHeaderColSpan, deductionRubColumnName);
        Cell cell22 = row2.createCell(columnCount+tradeResultHeaderColSpan+1);
        cell22.setCellStyle(doubleCellStyle);
        cell22.setCellValue(t.getDeductionRub());
        rowCount++;
        rowCount++;
        return rowCount;
    }

    public int writeInstrumentResult(XSSFWorkbook workbook, XSSFSheet sheet, String instrument,
                                     TradeResultsForInstrument tradeResultsForInstrument, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row, rowCount, columnCount, columnCount+instrumentResultHeaderColSpan, sumTaxForInstrumentRubColumnName + instrument);
        Cell cell2 = row.createCell(columnCount+instrumentResultHeaderColSpan+1);
        cell2.setCellStyle(doubleCellStyle);
        cell2.setCellValue(tradeResultsForInstrument.getTax());
        columnCount = 0;
        Row row1 = sheet.createRow(++rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row1, rowCount, columnCount, columnCount+instrumentResultHeaderColSpan, sumDeductionForInstrumentRubColumnName + instrument);
        Cell cell12 = row1.createCell(columnCount+instrumentResultHeaderColSpan+1);
        cell12.setCellStyle(doubleCellStyle);
        cell12.setCellValue(tradeResultsForInstrument.getDeduction());
        rowCount++;
        rowCount++;
        return rowCount;
    }

    public int writeFinalResult(XSSFWorkbook workbook, XSSFSheet sheet, double tradesTaxResult, double tradesDeductionResult,
                                int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row, rowCount, columnCount, finalResultHeaderColSpan, finalTaxColumnName);
        Cell cell2 = row.createCell(columnCount+finalResultHeaderColSpan+1);
        cell2.setCellStyle(doubleCellStyle);
        cell2.setCellValue(tradesTaxResult);
        columnCount = 0;
        Row row1 = sheet.createRow(++rowCount);
        sheet = cellStylesProvider.addMergedCell(sheet, row1, rowCount, columnCount, finalResultHeaderColSpan, finalDeductionColumnName);
        Cell cell12 = row1.createCell(columnCount+finalResultHeaderColSpan+1);
        cell12.setCellStyle(doubleCellStyle);
        cell12.setCellValue(tradesDeductionResult);
        rowCount++;
        rowCount++;
        return rowCount;
    }
}
