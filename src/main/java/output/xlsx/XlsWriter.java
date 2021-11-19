package output.xlsx;

import dto.DocumentCalculated;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.FilesUtils;

import java.io.File;

public class XlsWriter {

    public static File writeXlsFile(DocumentCalculated documentCalculated, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        DividendWriter dividendWriter = new DividendWriter();
        TradesWriter tradesWriter = new TradesWriter();
        FilesUtils fileUtils = new FilesUtils();

        workbook = dividendWriter.writeDividends(documentCalculated.getDividends(), workbook);
        workbook = tradesWriter.writeTrades(documentCalculated.getTrades(), workbook);

        File file = fileUtils.writeXlsFile(workbook, fileName);
        return file;
    }

    public static void autoSizeColumn(XSSFSheet sheet, int length) {
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i, false);
        }
    }
}
