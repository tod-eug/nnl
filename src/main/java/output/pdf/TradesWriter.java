package output.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import dto.DocumentCalculated;
import dto.TradeCalculated;
import dto.TradeResultsForInstrument;
import dto.Trades;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TradesWriter {

    private static final int numberOfColumns = 9;
    private static final int tradeResultHeaderColSpan = 4;
    private static final int instrumentResultHeaderColSpan = 2;
    private static final int finalResultHeaderColSpan = 2;

    private static final String dateColumnName = "Date";
    private static final String quantityColumnName = "Quantity";
    private static final String tradePriceColumnName = "Price";
    private static final String sumColumnName = "Sum";
    private static final String sumRubColumnName = "Sum rub";
    private static final String commissionColumnName = "Commission";
    private static final String commissionRubColumnName = "Commission rub";
    private static final String currencyColumnName = "Currency";
    private static final String exchangeRateColumnName = "Exchange rate";
    private static final String finalPLColumnName = "Final PL:";
    private static final String taxRubColumnName = "Tax rub:";
    private static final String deductionRubColumnName = "Deduction:";
    private static final String sumTaxForInstrumentRubColumnName = "Sum tax to pay for: ";
    private static final String sumDeductionForInstrumentRubColumnName = "Sum deduction to pay for: ";
    private static final String finalTaxColumnName = "Sum tax to pay:";
    private static final String finalDeductionColumnName = "Sum deduction:";

    public Document writeTrades(DocumentCalculated documentCalculated, Document document, Font font) {

        CellsProvider cellsProvider = new CellsProvider();

        document.newPage();
        Chunk chunk = new Chunk("Tax calculation, Trades", font);

        PdfPTable table = new PdfPTable(new float[] { 1.3f, 1, 1, 1, 1, 1, 1, 1, 1 });
        table.setWidthPercentage(100);


        Set<String> instruments = documentCalculated.getTrades().keySet();
        for (String instrument : instruments) {
            addInstrumentHeader(table, instrument);
            cellsProvider.addEmptyRow(table, numberOfColumns);
            cellsProvider.addEmptyRow(table, numberOfColumns);

            List<Trades> list = documentCalculated.getTrades().get(instrument);

            for (Trades t : list) {
                List<TradeCalculated> purchases = t.getPurchases();
                List<TradeCalculated> sells = t.getSells();

                //if position was reduced or closed then do something, otherwise ignore trades with this equity
                if (sells.size() > 0) {
                    addTickerHeader(table, t.getTicker());
                    addTableHeader(table);

                    if (purchases.size() > 0) {
                        for (TradeCalculated tc : purchases) {
                            addRows(table, tc);
                        }
                    }
                    if (sells.size() > 0) {
                        for (TradeCalculated tc : sells) {
                            addRows(table, tc);
                        }
                    }
                    cellsProvider.addEmptyRow(table, numberOfColumns);
                    writeResult(table, t);
                    cellsProvider.addEmptyRow(table, numberOfColumns);
                }
            }
            writeInstrumentResult(table, instrument, documentCalculated.getFinalResultsByInstruments().get(instrument));
            cellsProvider.addEmptyRow(table, numberOfColumns);
        }
        writeFinalResult(table, documentCalculated.getTradesTaxResult(), documentCalculated.getTradesDeductionResult());

        try {
            document.add(chunk);
            document.add(new Paragraph(10, "\u00a0"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return document;
    }

    private void addInstrumentHeader(PdfPTable table, String text) {
        CellsProvider cellsProvider = new CellsProvider();
        PdfPCell cell = cellsProvider.getInstrumentHeaderCell(text, numberOfColumns);
        table.addCell(cell);
    }

    private void addTickerHeader(PdfPTable table, String text) {
        CellsProvider cellsProvider = new CellsProvider();
        PdfPCell cell = cellsProvider.getTicketHeaderCell(text, numberOfColumns);
        table.addCell(cell);
    }

    private void addTableHeader(PdfPTable table) {
        CellsProvider cellsProvider = new CellsProvider();
        Stream.of(dateColumnName, quantityColumnName, tradePriceColumnName, sumColumnName,
                        sumRubColumnName, commissionColumnName, commissionRubColumnName,
                        currencyColumnName, exchangeRateColumnName)
                .forEach(columnTitle -> {
                    PdfPCell header = cellsProvider.getTradeHeaderCell(columnTitle);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, TradeCalculated tc) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TPdfWriter.datePattern);

        CellsProvider cellsProvider = new CellsProvider();
            table.addCell(dateFormat.format(tc.getDate()));
            table.addCell(cellsProvider.getRowDataCell(df.format(tc.getQuantity())));
            table.addCell(cellsProvider.getRowDataCell(df.format(tc.getTradePrice())));
            table.addCell(cellsProvider.getRowDataCell(df.format(tc.getSum())));
            table.addCell(cellsProvider.getRowDataCell(df.format(tc.getSumRub())));
            table.addCell(cellsProvider.getRowDataCell(df.format(tc.getCommission())));
            table.addCell(cellsProvider.getRowDataCell(df.format(tc.getCommissionRub())));
            table.addCell(cellsProvider.getRowDataCell(tc.getCurrency()));
            table.addCell(cellsProvider.getRowDataCell(df.format(tc.getExchangeRate())));
    }

    private void writeResult(PdfPTable table, Trades t) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);

        CellsProvider cellsProvider = new CellsProvider();

        table.addCell(cellsProvider.getResultHeaderCell(finalPLColumnName, tradeResultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(t.getFinalPLRub())));
        cellsProvider.addCells(table, numberOfColumns - tradeResultHeaderColSpan - 1);

        table.addCell(cellsProvider.getResultHeaderCell(taxRubColumnName, tradeResultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(t.getTaxRub())));
        cellsProvider.addCells(table, numberOfColumns - tradeResultHeaderColSpan - 1);

        table.addCell(cellsProvider.getResultHeaderCell(deductionRubColumnName, tradeResultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(t.getDeductionRub())));
        cellsProvider.addCells(table, numberOfColumns - tradeResultHeaderColSpan - 1);
    }

    private void writeInstrumentResult(PdfPTable table, String instrument, TradeResultsForInstrument tradeResultsForInstrument) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);

        CellsProvider cellsProvider = new CellsProvider();

        table.addCell(cellsProvider.getResultHeaderCell(sumTaxForInstrumentRubColumnName + instrument, instrumentResultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(tradeResultsForInstrument.getTax())));
        cellsProvider.addCells(table, numberOfColumns - instrumentResultHeaderColSpan - 1);

        table.addCell(cellsProvider.getResultHeaderCell(sumDeductionForInstrumentRubColumnName + instrument, instrumentResultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(tradeResultsForInstrument.getDeduction())));
        cellsProvider.addCells(table, numberOfColumns - instrumentResultHeaderColSpan - 1);
    }

    private void writeFinalResult(PdfPTable table, double tradesTaxResult, double tradesDeductionResult) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);

        CellsProvider cellsProvider = new CellsProvider();

        table.addCell(cellsProvider.getResultHeaderCell(finalTaxColumnName, finalResultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(tradesTaxResult)));
        cellsProvider.addCells(table, numberOfColumns - finalResultHeaderColSpan - 1);

        table.addCell(cellsProvider.getResultHeaderCell(finalDeductionColumnName, finalResultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(tradesDeductionResult)));
        cellsProvider.addCells(table, numberOfColumns - finalResultHeaderColSpan - 1);
    }
}
