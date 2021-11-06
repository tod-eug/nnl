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
    private static final String finalPLColumnName = "Final PL:";
    private static final String taxRubColumnName = "Tax rub:";
    private static final String deductionRubColumnName = "Deduction:";


    public static XSSFWorkbook writeTrades(Map<String, List<Trades>> trades, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(listName);

        int rowCount = 0;

        CellStylesProvider cellStylesProvider = new CellStylesProvider();
        CellStyle dateCellStyle = cellStylesProvider.getDateCellStyle(workbook, sheet);
        CellStyle doubleCellStyle = cellStylesProvider.getDoubleCellStyle(workbook, sheet);

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
                        rowCount = writeTrade(sheet, tc, rowCount, dateCellStyle, doubleCellStyle);
                        rowCount++;
                    }
                }
                if (sells.size() > 0) {
                    for (TradeCalculated tc : sells) {
                        rowCount = setTradeHeader(sheet, rowCount);
                        rowCount = writeTrade(sheet, tc, rowCount, dateCellStyle, doubleCellStyle);
                        rowCount++;
                    }
                }
                rowCount = writeResult(sheet, t, rowCount, doubleCellStyle);
            }
        }
        XlsWriter.autoSizeColumn(sheet, 13);
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
        rowCount++;
        return rowCount;
    }

    private static int writeTrade(XSSFSheet sheet, TradeCalculated t, int rowCount,
                                   CellStyle dateCellStyle, CellStyle doubleCellStyle) {
        int columnCount = 0;
        Row row = sheet.createRow(rowCount);
        Cell cellD1 = row.createCell(columnCount);
        cellD1.setCellValue(t.getTicker());
        Cell cellD2 = row.createCell(++columnCount);
        cellD2.setCellStyle(dateCellStyle);
        cellD2.setCellValue(t.getDate());
        Cell cellD3 = row.createCell(++columnCount);
        cellD3.setCellStyle(doubleCellStyle);
        cellD3.setCellValue(t.getQuantity());
        Cell cellD4 = row.createCell(++columnCount);
        cellD4.setCellStyle(doubleCellStyle);
        cellD4.setCellValue(t.getTradePrice());
        Cell cellD5 = row.createCell(++columnCount);
        cellD5.setCellStyle(doubleCellStyle);
        cellD5.setCellValue(t.getSum());
        Cell cellD6 = row.createCell(++columnCount);
        cellD6.setCellStyle(doubleCellStyle);
        cellD6.setCellValue(t.getSumRub());
        Cell cellD7 = row.createCell(++columnCount);
        cellD7.setCellStyle(doubleCellStyle);
        cellD7.setCellValue(t.getCommission());
        Cell cellD8 = row.createCell(++columnCount);
        cellD8.setCellStyle(doubleCellStyle);
        cellD8.setCellValue(t.getCommissionRub());
        Cell cellD9 = row.createCell(++columnCount);
        cellD9.setCellStyle(doubleCellStyle);
        cellD9.setCellValue(t.getBasis());
        Cell cellD10 = row.createCell(++columnCount);
        cellD10.setCellStyle(doubleCellStyle);
        cellD10.setCellValue(t.getBasisRub());
        Cell cellD11 = row.createCell(++columnCount);
        cellD11.setCellStyle(doubleCellStyle);
        cellD11.setCellValue(t.getRealizedPL());
        Cell cellD12 = row.createCell(++columnCount);
        cellD12.setCellStyle(doubleCellStyle);
        cellD12.setCellValue(t.getRealizedPLRub());
        Cell cellD13 = row.createCell(++columnCount);
        cellD13.setCellStyle(doubleCellStyle);
        cellD13.setCellValue(t.getResult());
        rowCount++;
        return rowCount;
        }

    public static int writeResult(XSSFSheet sheet, Trades t, int rowCount, CellStyle doubleCellStyle) {
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
