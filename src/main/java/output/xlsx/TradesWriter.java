package output.xlsx;

import dto.TradeCalculated;
import dto.Trades;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TradesWriter {

    private static final String listName = "Trades";

    private static final String tickerColumnName = "Ticker";
    private static final String dateColumnName = "Date";
    private static final String quantityColumnName = "Quantity";
    private static final String tradePriceColumnName = "Price";
    private static final String sumColumnName = "Sum";
    private static final String sumRubColumnName = "Sum rub";
    private static final String commissionColumnName = "Commission";
    private static final String commissionRubColumnName = "Commission rub";
    private static final String basisColumnName = "Basis";
    private static final String basisRubColumnName = "Basis rub";
    private static final String realizedPLColumnName = "Realized PL";
    private static final String realizedPLRubColumnName = "Realized PL Rub";
    private static final String resultColumnName = "Result";
    private static final String exchangeRateColumnName = "Exchange rate";
    private static final String finalPLColumnName = "Final PL:";
    private static final String taxRubColumnName = "Tax rub:";
    private static final String deductionRubColumnName = "Deduction:";


    public static XSSFWorkbook writeTrades(Map<String, List<Trades>> trades, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(listName);

        int rowCount = 0;

        Set<String> instruments = trades.keySet();
        for (String instrument : instruments) {
            rowCount = setInstrumentHeader(sheet, instrument, rowCount);
            rowCount++;
            rowCount++;

            List<Trades> list = trades.get(instrument);

            for (Trades t : list) {
                rowCount = setTickerHeader(sheet, t.getTicker(), rowCount);

                List<TradeCalculated> purchases = t.getPurchases();
                List<TradeCalculated> sells = t.getSells();

                if (purchases.size() > 0) {
                    for (TradeCalculated tc : purchases) {
                        rowCount = setTradeHeader(sheet, rowCount);
                        rowCount = writeTrade(workbook, sheet, tc, rowCount);
                        rowCount++;
                    }
                }
                if (sells.size() > 0) {
                    for (TradeCalculated tc : sells) {
                        rowCount = setTradeHeader(sheet, rowCount);
                        rowCount = writeTrade(workbook, sheet, tc, rowCount);
                        rowCount++;
                    }
                }
                rowCount = writeResult(workbook, sheet, t, rowCount);
            }
        }
        XlsWriter.autoSizeColumn(sheet, 14);
        return workbook;
    }

    private static int setInstrumentHeader(XSSFSheet sheet, String instrumentName, int rowCount) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(instrumentName);
        rowCount++;
        return rowCount;
    }

    private static int setTickerHeader(XSSFSheet sheet, String tickerName, int rowCount) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(tickerName);
        rowCount++;
        return rowCount;
    }

    private static int setTradeHeader(XSSFSheet sheet, int rowCount) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(tickerColumnName);
        Cell cell2 = row.createCell(++columnCount);
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
        Cell cell9 = row.createCell(++columnCount);
        cell9.setCellValue(basisColumnName);
        Cell cell10 = row.createCell(++columnCount);
        cell10.setCellValue(basisRubColumnName);
        Cell cell11 = row.createCell(++columnCount);
        cell11.setCellValue(realizedPLColumnName);
        Cell cell12 = row.createCell(++columnCount);
        cell12.setCellValue(realizedPLRubColumnName);
        Cell cell13 = row.createCell(++columnCount);
        cell13.setCellValue(resultColumnName);
        Cell cell14 = row.createCell(++columnCount);
        cell14.setCellValue(exchangeRateColumnName);
        rowCount++;
        return rowCount;
    }

    private static int writeTrade(XSSFWorkbook workbook, XSSFSheet sheet, TradeCalculated t, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle dateCellStyle = cellStylesProvider.getDateCellStyle(workbook, sheet);
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);
        CellStyle exchangeRateCellStyle = cellStylesProvider.getExchangeRateCellStyle(workbook, sheet);

        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(t.getTicker());
        Cell cell2 = row.createCell(++columnCount);
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
        Cell cell9 = row.createCell(++columnCount);
        cell9.setCellStyle(doubleCellStyle);
        cell9.setCellValue(t.getBasis());
        Cell cell10 = row.createCell(++columnCount);
        cell10.setCellStyle(doubleCellStyle);
        cell10.setCellValue(t.getBasisRub());
        Cell cell11 = row.createCell(++columnCount);
        cell11.setCellStyle(doubleCellStyle);
        cell11.setCellValue(t.getRealizedPL());
        Cell cell12 = row.createCell(++columnCount);
        cell12.setCellStyle(doubleCellStyle);
        cell12.setCellValue(t.getRealizedPLRub());
        Cell cell13 = row.createCell(++columnCount);
        cell13.setCellStyle(doubleCellStyle);
        cell13.setCellValue(t.getResult());
        Cell cell14 = row.createCell(++columnCount);
        cell14.setCellStyle(exchangeRateCellStyle);
        cell14.setCellValue(t.getExchangeRate());
        rowCount++;
        return rowCount;
        }

    public static int writeResult(XSSFWorkbook workbook, XSSFSheet sheet, Trades t, int rowCount) {
        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cell1 = row.createCell(columnCount);
        cell1.setCellValue(finalPLColumnName);
        Cell cell2 = row.createCell(++columnCount);
        cell2.setCellStyle(doubleCellStyle);
        cell2.setCellValue(t.getFinalPLRub());
        columnCount = 0;
        Row row1 = sheet.createRow(++rowCount);
        Cell cell11 = row1.createCell(columnCount);
        cell11.setCellValue(taxRubColumnName);
        Cell cell12 = row1.createCell(++columnCount);
        cell12.setCellStyle(doubleCellStyle);
        cell12.setCellValue(t.getTaxRub());
        columnCount = 0;
        Row row2 = sheet.createRow(++rowCount);
        Cell cell21 = row2.createCell(columnCount);
        cell21.setCellValue(deductionRubColumnName);
        Cell cell22 = row2.createCell(++columnCount);
        cell22.setCellStyle(doubleCellStyle);
        cell22.setCellValue(t.getDeductionRub());
        rowCount++;
        rowCount++;
        return rowCount;
    }
}
