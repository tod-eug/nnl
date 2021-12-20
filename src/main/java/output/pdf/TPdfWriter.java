package output.pdf;

import bot.TaxBot;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import dto.DocumentCalculated;
import util.FilesUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TPdfWriter {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public static final String doubleFormatPattern = "####0.00";
    public static final String datePattern = "dd.MM.yyyy";

    public File writePdfFile(DocumentCalculated documentCalculated, String fileName) {
        DividendWriter dividendWriter = new DividendWriter();
        TradesWriter tradesWriter = new TradesWriter();
        FeesWriter feesWriter = new FeesWriter();
        InterestWriter interestWriter = new InterestWriter();
        FeesTransactionsWriter feesTransactionsWriter = new FeesTransactionsWriter();
        SummaryWriter summaryWriter = new SummaryWriter();

        FilesUtils fileUtils = new FilesUtils();
        File file = fileUtils.writePdfFile(fileName);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (DocumentException | FileNotFoundException e) {
            log.log(Level.SEVERE, "Error while write pdf. Exception: ", e);
        }

        document.setPageSize(PageSize.A4.rotate());
        document.open();
        document = summaryWriter.writeSummary(documentCalculated, document);
        if (documentCalculated.getDividends().size() > 0)
            document = dividendWriter.writeDividends(documentCalculated, document);
        if (documentCalculated.getTrades().size() > 0)
            document = tradesWriter.writeTrades(documentCalculated, document);
        if (documentCalculated.getInterests().size() > 0)
            document = interestWriter.writeInterest(documentCalculated, document);
        if (documentCalculated.getFees().size() > 0)
            document = feesWriter.writeFees(documentCalculated, document);
        if (documentCalculated.getFeesTransactions().size() > 0)
            document = feesTransactionsWriter.writeFees(documentCalculated, document);
        document.close();
        return file;
    }
}


