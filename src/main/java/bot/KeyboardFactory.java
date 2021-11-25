package bot;

import bot.enums.Format;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboard withPdfXlsButtons() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton pdf = new InlineKeyboardButton();
        pdf.setText(Format.pdf.toString());
        pdf.setCallbackData(Format.pdf.toString());
        rowInline.add(pdf);
        InlineKeyboardButton xlsx = new InlineKeyboardButton();
        xlsx.setText(Format.xlsx.toString());
        xlsx.setCallbackData(Format.xlsx.toString());
        rowInline.add(xlsx);
        rowsInline.add(rowInline);
        keyboardMarkup.setKeyboard(rowsInline);
        return keyboardMarkup;
    }
}
