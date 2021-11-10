package output.xlsx;

import dto.DividendCalculated;
import dto.Trades;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.FileWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XlsWriter {

    public static void writeXlsFile(ArrayList<DividendCalculated> list, Map<String, List<Trades>> trades, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        DividendWriter dividendWriter = new DividendWriter();
        TradesWriter tradesWriter = new TradesWriter();
        FileWriter fileWriter = new FileWriter();

        workbook = dividendWriter.writeDividends(list, workbook);
        workbook = tradesWriter.writeTrades(trades, workbook);

        fileWriter.writeFile(workbook, fileName);
    }

    public static void autoSizeColumn(XSSFSheet sheet, int length) {
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i, false);
        }
    }
}
