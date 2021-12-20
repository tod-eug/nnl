package output.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;

import static java.awt.Font.BOLD;

public class PhraseProvider {

    private static final String COURIER = "src/main/java/output/pdf/fonts/arial.ttf";
    private static final Integer HEADER_FONT_SIZE = 14;
    private static final Integer ROW_HEADER_FONT_SIZE = 12;
    private static final Integer ROW_FONT_SIZE = 10;

    public Font getFont(int size) {
        Font font = FontFactory.getFont(COURIER, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        font.setSize(size);
        return font;
    }

    public Phrase getPhraseHeader(String content) {
        Font font = getFont(HEADER_FONT_SIZE);
        font.setStyle(BOLD);
        return new Phrase(content, font);
    }

    public Phrase getPhraseRowHeader(String content) {
        return new Phrase(content, getFont(ROW_HEADER_FONT_SIZE));
    }

    public Phrase getPhraseRow(String content) {
        return new Phrase(content, getFont(ROW_FONT_SIZE));
    }
}
