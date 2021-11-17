package output.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

public class CellsProvider {
    /**
     * Where to select BaseColor: https://www.rapidtables.com/web/color/RGB_Color.html
     */

    public PdfPCell getRowDataCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        return cell;
    }

    public PdfPCell getDividendHeaderCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(2);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setFixedHeight(35f);
        return cell;
    }

    public PdfPCell getTradeHeaderCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    public PdfPCell getResultHeaderCell(String content, int colSpan) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setColspan(colSpan);
        cell.setBackgroundColor(new BaseColor(226, 226, 226));
        return cell;
    }

    public PdfPCell getInstrumentHeaderCell(String content, int numberOfColumns) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBackgroundColor(new BaseColor(14, 71, 255));
        cell.setColspan(numberOfColumns);
        return cell;
    }

    public PdfPCell getTicketHeaderCell(String content, int numberOfColumns) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBackgroundColor(new BaseColor(110, 144, 255));
        cell.setColspan(numberOfColumns);
        return cell;
    }
}
