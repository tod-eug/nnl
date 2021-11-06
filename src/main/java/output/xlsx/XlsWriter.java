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

    private static final String fileName = "Taxes.xlsx";

    public static void writeXlsFile(ArrayList<DividendCalculated> list, Map<String, List<Trades>> trades) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        DividendWriter.writeDividends(list, workbook);
        TradesWriter.writeTrades(trades, workbook);

        FileWriter.writeFile(workbook, fileName);
    }

    public static void autoSizeColumn(XSSFSheet sheet, int length) {
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i, false);
        }
    }
}
