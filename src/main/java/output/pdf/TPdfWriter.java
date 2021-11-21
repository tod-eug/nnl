package output.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import dto.DocumentCalculated;
import util.FilesUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TPdfWriter {

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
            e.printStackTrace();
        }

        document.setPageSize(PageSize.A4.rotate());
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        document = summaryWriter.writeSummary(documentCalculated, document, font);
        if (documentCalculated.getDividends().size() > 0)
            document = dividendWriter.writeDividends(documentCalculated, document, font);
        if (documentCalculated.getTrades().size() > 0)
            document = tradesWriter.writeTrades(documentCalculated, document, font);
        if (documentCalculated.getInterests().size() > 0)
            document = interestWriter.writeInterest(documentCalculated, document, font);
        if (documentCalculated.getFees().size() > 0)
            document = feesWriter.writeFees(documentCalculated, document, font);
        if (documentCalculated.getFeesTransactions().size() > 0)
            document = feesTransactionsWriter.writeFees(documentCalculated, document, font);
        document.close();
        return file;
    }
}


