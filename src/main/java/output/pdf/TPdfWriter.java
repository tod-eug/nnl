package output.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import dto.DividendCalculated;
import dto.Trades;
import util.FilesUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TPdfWriter {

    public static final String doubleFormatPattern = "####0.00";
    public static final String datePattern = "dd.MM.yyyy";

    public File writePdfFile(ArrayList<DividendCalculated> list, Map<String, List<Trades>> trades, String fileName) {
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
        document = dividendWriter.writeDividends(list, document, font);
        try {
            document.add(new Paragraph(10, "\u00a0"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document = tradesWriter.writeTrades(trades, document, font);
        document.close();
        return file;
    }
}


