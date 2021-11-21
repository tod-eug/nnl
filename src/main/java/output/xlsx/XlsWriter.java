package output.xlsx;

import dto.DocumentCalculated;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.FilesUtils;

import java.io.File;

public class XlsWriter {

    public static File writeXlsFile(DocumentCalculated documentCalculated, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        SummaryWriter summaryWriter = new SummaryWriter();
        DividendWriter dividendWriter = new DividendWriter();
        TradesWriter tradesWriter = new TradesWriter();
        FilesUtils fileUtils = new FilesUtils();

        workbook = summaryWriter.writeSummary(documentCalculated, workbook);
        workbook = dividendWriter.writeDividends(documentCalculated, workbook);
        workbook = tradesWriter.writeTrades(documentCalculated, workbook);

        File file = fileUtils.writeXlsFile(workbook, fileName);
        return file;
    }

    public static void autoSizeColumn(XSSFSheet sheet, int length) {
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i, false);
        }
    }
}
