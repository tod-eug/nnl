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
        if (documentCalculated.getDividends().size() > 0)
            document = dividendWriter.writeDividends(documentCalculated, document, font);
        try {
            document.add(new Paragraph(10, "\u00a0"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (documentCalculated.getTrades().size() > 0)
            document = tradesWriter.writeTrades(documentCalculated, document, font);
        document.close();
        return file;
    }
}


