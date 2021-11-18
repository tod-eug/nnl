package output.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import dto.DividendCalculated;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

public class DividendWriter {

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

    public Document writeDividends(ArrayList<DividendCalculated> list, Document document, Font font) {


        Chunk chunk = new Chunk("Tax calculation, Dividends", font);

        PdfPTable table = new PdfPTable(new float[] { 1.5f, 1.3f, 1, 1, 1, 1, 1, 1, 1, 1 });
        table.setWidthPercentage(100);
        addTableHeader(table);
        table.setHeaderRows(1);
        addRows(table, list);

        try {
            document.add(chunk);
            document.add(new Paragraph(10, "\u00a0"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
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
}