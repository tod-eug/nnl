package output.xlsx;

import dto.DocumentCalculated;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.FilesUtils;

import java.io.File;

public class XlsWriter {

    private static final String listName = "Interests and fees";

    public static File writeXlsFile(DocumentCalculated documentCalculated, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        SummaryWriter summaryWriter = new SummaryWriter();
        DividendWriter dividendWriter = new DividendWriter();
        TradesWriter tradesWriter = new TradesWriter();
        InterestsWriter interestsWriter = new InterestsWriter();
        FeesWriter feesWriter = new FeesWriter();
        FeesTransactionWriter feesTransactionWriter = new FeesTransactionWriter();
        FilesUtils fileUtils = new FilesUtils();
        XSSFSheet interestsAndFeesSheet;

        if (documentCalculated.getFinalTaxResult() > 0.0)
            workbook = summaryWriter.writeSummary(documentCalculated, workbook);
        if (documentCalculated.getDividends().size() > 0)
            workbook = dividendWriter.writeDividends(documentCalculated, workbook);
        if (documentCalculated.getTrades().size() > 0)
            workbook = tradesWriter.writeTrades(documentCalculated, workbook);
        if (documentCalculated.getInterests().size() > 0 || documentCalculated.getFees().size() > 0 || documentCalculated.getFeesTransactions().size() > 0) {
            interestsAndFeesSheet = workbook.createSheet(listName);
            if (documentCalculated.getInterests().size() > 0)
                workbook = interestsWriter.writeInterests(documentCalculated, workbook, interestsAndFeesSheet);
            if (documentCalculated.getFees().size() > 0)
                workbook = feesWriter.writeFees(documentCalculated, workbook, interestsAndFeesSheet);
            if (documentCalculated.getFeesTransactions().size() > 0)
                workbook = feesTransactionWriter.writeFeesTransactions(documentCalculated, workbook, interestsAndFeesSheet);
        }

        File file = fileUtils.writeXlsFile(workbook, fileName);
        return file;
    }

    public static void autoSizeColumn(XSSFSheet sheet, int length) {
        for (int i = 0; i < length; i++) {
            sheet.autoSizeColumn(i, false);
        }
    }
}
