package output.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import dto.DocumentCalculated;
import dto.FeesCalculated;
import dto.InterestCalculated;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FeesAndInterestWriter {

    private static final int numberOfColumns = 4;
    private static final int resultHeaderColSpan = 3;

    private static final String dateColumnName = "Date";
    private static final String descriptionColumnName = "Description";
    private static final String amountColumnName = "Amount";
    private static final String amountRubColumnName = "Amount Rub";
    private static final String feesResultColumnName = "Sum payed fees:";
    private static final String interestResultColumnName = "Sum received interest:";

    public Document writeFees(DocumentCalculated documentCalculated, Document document, Font font) {

        Chunk chunk = new Chunk("Fees", font);

        PdfPTable table = new PdfPTable(new float[] { 1, 3, 1, 1 });
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        addTableHeader(table);
        table.setHeaderRows(1);
        addRowsFees(table, documentCalculated.getFees());
        addResult(table, feesResultColumnName, documentCalculated.getFeesResult());

        try {
            document.add(new Paragraph(20, "\u00a0"));
            document.add(chunk);
            document.add(new Paragraph(10, "\u00a0"));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    public Document writeInterest(DocumentCalculated documentCalculated, Document document, Font font) {

        Chunk chunk = new Chunk("Interest", font);

        PdfPTable table = new PdfPTable(new float[] { 1, 3, 1, 1 });
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        addTableHeader(table);
        table.setHeaderRows(1);
        addRowsInterests(table, documentCalculated.getInterests());
        addResult(table, interestResultColumnName, documentCalculated.getInterestsResult());

        try {
            document.add(new Paragraph(20, "\u00a0"));
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
        Stream.of(dateColumnName, descriptionColumnName, amountColumnName, amountRubColumnName)
                .forEach(columnTitle -> {
                    PdfPCell header = cellsProvider.getDividendHeaderCell(columnTitle);
                    table.addCell(header);
                });
    }

    private void addRowsFees(PdfPTable table, ArrayList<FeesCalculated> list) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TPdfWriter.datePattern);

        CellsProvider cellsProvider = new CellsProvider();
        for (FeesCalculated f : list) {
            table.addCell(cellsProvider.getRowDataCell(dateFormat.format(f.getDate())));
            table.addCell(f.getDescription());
            table.addCell(cellsProvider.getRowDataCell(df.format(f.getAmount())));
            table.addCell(cellsProvider.getRowDataCell(df.format(f.getAmountRub())));
        }
    }

    private void addRowsInterests(PdfPTable table, ArrayList<InterestCalculated> list) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TPdfWriter.datePattern);

        CellsProvider cellsProvider = new CellsProvider();
        for (InterestCalculated f : list) {
            table.addCell(cellsProvider.getRowDataCell(dateFormat.format(f.getDate())));
            table.addCell(f.getDescription());
            table.addCell(cellsProvider.getRowDataCell(df.format(f.getAmount())));
            table.addCell(cellsProvider.getRowDataCell(df.format(f.getAmountRub())));
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
