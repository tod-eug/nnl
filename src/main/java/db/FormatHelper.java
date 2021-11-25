package db;

import bot.enums.Format;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

public class FormatHelper {

    public Format getFormat(long chatId, Format format) {
        String f = getFormatByChatId(chatId);
        if (f.equals("")) {
            setFormat(chatId, format);
        } else {
            switch (f.toLowerCase(Locale.ROOT)) {
                case "pdf":
                    format = Format.pdf;
                    break;
                case "xlsx":
                    format = Format.xlsx;
                    break;
            }
        }
        return format;
    }

    public void setFormat(long chatIdLong, Format format) {

        String query = "";
        String chatId = Long.toString(chatIdLong);
        String existingFormat = getFormatByChatId(chatIdLong);

        if (existingFormat.equals("")) {
            UUID id = UUID.randomUUID();
            query = String.format("insert into public.format (id, chat_id, format) VALUES ('%s', '%s', '%s');",
                    id, chatId, format.toString());
        } else {
            query = String.format("update public.format set format = '%s' where chat_id = '%s';", format.toString(), chatId);
        }

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(query).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
    }

    private String getFormatByChatId(long chatIdLong) {
        String chatId = Long.toString(chatIdLong);
        String selectQuery = String.format("select format from public.format where chat_id = '%s';", chatId);

        DatabaseHelper dbHelper = new DatabaseHelper();
        String format = "";
        try {
            ResultSet st = dbHelper.getPreparedStatement(selectQuery).executeQuery();
            if (st.next()) {
                format = st.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
        return format;
    }
}
