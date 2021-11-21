package output.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import dto.DocumentCalculated;
import dto.InterestCalculated;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

public class InterestWriter {

    private static final int numberOfColumns = 5;
    private static final int resultHeaderColSpan = 4;

    private static final String dateColumnName = "Date";
    private static final String descriptionColumnName = "Description";
    private static final String amountColumnName = "Amount";
    private static final String amountRubColumnName = "Amount Rub";
    private static final String taxRubColumnName = "Tax Rub";
    private static final String interestResultColumnName = "Sum received interest:";


    public Document writeInterest(DocumentCalculated documentCalculated, Document document, Font font) {

        Chunk chunk = new Chunk("Interest", font);

        PdfPTable table = new PdfPTable(new float[] { 1, 3, 1, 1, 1 });
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        addTableHeader(table);
        table.setHeaderRows(1);
        addRowsInterests(table, documentCalculated.getInterests());
        addResult(table, interestResultColumnName, documentCalculated.getInterestsTaxResult());

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
        Stream.of(dateColumnName, descriptionColumnName, amountColumnName, amountRubColumnName, taxRubColumnName)
                .forEach(columnTitle -> {
                    PdfPCell header = cellsProvider.getDividendHeaderCell(columnTitle);
                    table.addCell(header);
                });
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
            table.addCell(cellsProvider.getRowDataCell(df.format(f.getTaxRub())));
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
