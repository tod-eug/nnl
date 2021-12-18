package output.pdf;

import bot.TaxBot;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import dto.DocumentCalculated;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SummaryWriter {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    private static final int numberOfColumns = 5;

    private static final String finalTaxResultColumnName = "Final tax result to pay:";
    private static final String dividendResultColumnName = "Dividends tax:";
    private static final String tradesResultColumnName = "Trades tax:";
    private static final String interestResultColumnName = "Interest received tax:";
    private static final String finalDeductionResultColumnName = "Final tax deduction:";
    private static final String tradesDeductionColumnName = "Trades deduction:";
    private static final String feesColumnName = "Fees:";
    private static final String feesTransactionColumnName = "Transaction fees:";
    private static final String ofThemColumnName = "Of them:";

    public Document writeSummary(DocumentCalculated documentCalculated, Document document, Font font) {

        Chunk chunk = new Chunk("Brief summary", font);

        PdfPTable table = new PdfPTable(new float[] { 4, 3, 1, 4, 3 });
        table.setWidthPercentage(70);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(0);

        addRows(table, documentCalculated);

        try {
            document.add(chunk);
            document.add(new Paragraph(10, "\u00a0"));
            document.add(table);
        } catch (DocumentException e) {
            log.log(Level.SEVERE, "Error while write summary in pdf. Exception: ", e);
        }
        return document;
    }

    private void addRows(PdfPTable table, DocumentCalculated documentCalculated) {
        DecimalFormat df = new DecimalFormat(TPdfWriter.doubleFormatPattern);
        CellsProvider cellsProvider = new CellsProvider();

        table.addCell(finalTaxResultColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getFinalTaxResult())));
        cellsProvider.addCells(table, 1);
        table.addCell(finalDeductionResultColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getFinalDeductionResult())));

        PdfPCell cell = new PdfPCell(new Phrase(ofThemColumnName));
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        cellsProvider.addCells(table, 1);
        table.addCell(cell);

        table.addCell(dividendResultColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getDividendResult())));
        cellsProvider.addCells(table, 1);
        table.addCell(tradesDeductionColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getTradesDeductionResult())));

        table.addCell(tradesResultColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getTradesTaxResult())));
        cellsProvider.addCells(table, 1);
        table.addCell(feesColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getFeesResult())));

        table.addCell(interestResultColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getInterestsTaxResult())));
        cellsProvider.addCells(table, 1);
        table.addCell(feesTransactionColumnName);
        table.addCell(cellsProvider.getRowDataCellWithoutBorder(df.format(documentCalculated.getFeesTransactionsResult())));
    }
}
