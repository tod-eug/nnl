package output.xlsx;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellStylesProvider {

    private static final String dateFormat = "dd.mm.yyyy";
    private static final String doubleFormat = "0.00";
    private static final String exchangeRateFormat = "0.0000";

    public CellStyle getDateCellStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle dateCell = sheet.getWorkbook().createCellStyle();
        dateCell.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
        return dateCell;
    }

    public CellStyle getDoubleCellStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle doubleCell = sheet.getWorkbook().createCellStyle();
        doubleCell.setDataFormat(createHelper.createDataFormat().getFormat(doubleFormat));
        return doubleCell;
    }

    public CellStyle getExchangeRateCellStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle doubleCell = sheet.getWorkbook().createCellStyle();
        doubleCell.setDataFormat(createHelper.createDataFormat().getFormat(exchangeRateFormat));
        return doubleCell;
    }
}
