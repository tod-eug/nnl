package output.pdf;

import bot.TaxBot;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import dto.DividendCalculated;
import dto.DocumentCalculated;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DividendWriter {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    private static final int numberOfColumns = 10;
    private static final int resultHeaderColSpan = 8;

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

    public Document writeDividends(DocumentCalculated documentCalculated, Document document, Font font) {


        Chunk chunk = new Chunk("Tax calculation, Dividends", font);

        PdfPTable table = new PdfPTable(new float[] { 1.5f, 1.3f, 1, 1, 1, 1, 1, 1, 1, 1 });
        table.setWidthPercentage(100);
        addTableHeader(table);
        table.setHeaderRows(1);
        addRows(table, documentCalculated.getDividends());
        addResult(table, documentCalculated.getDividendResult());

        try {
            document.add(new Paragraph(10, "\u00a0"));
            document.add(chunk);
            document.add(new Paragraph(10, "\u00a0"));
            document.add(table);
        } catch (DocumentException e) {
            log.log(Level.SEVERE, "Error while write dividends in pdf. Exception: ", e);
        }
        return document;
    }

    private void addTableHeader(PdfPTable table) {
        CellsProvider cellsProvider = new CellsProvider();
        Stream.of(tickerColumnName, paymentDateColumnName, dividendGrossColumnName, dividendNetColumnName,
                        taxColumnName, dividendRubColumnName, expectedDividendRubColumnName, payedTaxRubColumnName,
                        resultColumnName, exchangeRateColumnName)
                .forEach(columnTitle -> {
                    PdfPCell header = cellsProvider.getDividendHeaderCell(columnTitle);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, ArrayList<DividendCalculated> list) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TPdfWriter.datePattern);

        CellsProvider cellsProvider = new CellsProvider();
        for (DividendCalculated d : list) {
            table.addCell(d.getTicker());
            table.addCell(cellsProvider.getRowDataCell(dateFormat.format(d.getPaymentDate())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getDividendGross())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getDividendNet())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getTax())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getDividendRub())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getExpectedDividendRub())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getPayedTaxRub())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getResult())));
            table.addCell(cellsProvider.getRowDataCell(df.format(d.getExchangeRate())));
        }
    }

    private void addResult(PdfPTable table, double result) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);

        CellsProvider cellsProvider = new CellsProvider();

        cellsProvider.addEmptyRow(table, numberOfColumns);
        table.addCell(cellsProvider.getResultHeaderCell(finalTaxColumnName, resultHeaderColSpan));
        table.addCell(cellsProvider.getRowDataCell(df.format(result)));
        cellsProvider.addCells(table, numberOfColumns - resultHeaderColSpan - 1);
    }
}
