package output.pdf;

import bot.TaxBot;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import dto.DocumentCalculated;
import dto.FeesTransactionsCalculated;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FeesTransactionsWriter {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    private static final int numberOfColumns = 7;
    private static final int resultHeaderColSpan = 6;

    private static final String tickerColumnName = "Ticker";
    private static final String dateColumnName = "Date";
    private static final String descriptionColumnName = "Description";
    private static final String quantityColumnName = "Quantity";
    private static final String tradePriceColumnName = "Trade price";
    private static final String amountColumnName = "Amount";
    private static final String amountRubColumnName = "Amount Rub";
    private static final String resultColumnName = "Sum payed transactions fees:";

    public Document writeFees(DocumentCalculated documentCalculated, Document document) {

        PhraseProvider phraseProvider = new PhraseProvider();
        Phrase phrase = phraseProvider.getPhraseHeader("Transaction fees");

        PdfPTable table = new PdfPTable(new float[] { 1, 1.3f, 3, 1, 1, 1, 1 });
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        addTableHeader(table);
        table.setHeaderRows(1);
        addRows(table, documentCalculated.getFeesTransactions(), phraseProvider);
        addResult(table, resultColumnName, documentCalculated.getFeesTransactionsResult());

        try {
            document.add(new Paragraph(20, "\u00a0"));
            document.add(phrase);
            document.add(new Paragraph(10, "\u00a0"));
            document.add(table);
        } catch (DocumentException e) {
            log.log(Level.SEVERE, "Error while write fees transactions in pdf. Exception: ", e);
        }
        return document;
    }

    private void addTableHeader(PdfPTable table) {
        CellsProvider cellsProvider = new CellsProvider();
        Stream.of(tickerColumnName, dateColumnName, descriptionColumnName, quantityColumnName, tradePriceColumnName,
                        amountColumnName, amountRubColumnName)
                .forEach(columnTitle -> {
                    PdfPCell header = cellsProvider.getDividendHeaderCell(columnTitle);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, ArrayList<FeesTransactionsCalculated> list, PhraseProvider phraseProvider) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TPdfWriter.datePattern);

        CellsProvider cellsProvider = new CellsProvider();
        for (FeesTransactionsCalculated ft : list) {
            table.addCell(phraseProvider.getPhraseRow(ft.getTicker()));
            table.addCell(cellsProvider.getRowDataCell(dateFormat.format(ft.getDate())));
            table.addCell(phraseProvider.getPhraseRow(ft.getDescription()));
            table.addCell(cellsProvider.getRowDataCell(df.format(ft.getQuantity())));
            table.addCell(cellsProvider.getRowDataCell(df.format(ft.getTradePrice())));
            table.addCell(cellsProvider.getRowDataCell(df.format(ft.getAmount())));
            table.addCell(cellsProvider.getRowDataCell(df.format(ft.getAmountRub())));
        }
    }

    private void addResult(PdfPTable table, String columnName, double result) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);

        CellsProvider cellsProvider = new CellsProvider();

        cellsProvider.addEmptyRow(table, numberOfColumns);
        table.addCell(cellsProvider.getResultHeaderCell(columnName, resultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(result)));
        cellsProvider.addCells(table, numberOfColumns - resultHeaderColSpan - 1);
    }
}
