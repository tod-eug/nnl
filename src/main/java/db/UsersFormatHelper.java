package db;

import bot.enums.Format;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsersFormatHelper {


    public void setFormat(String userId, Format format) {

        String query = "";
        String existingFormat = getFormatByUserId(userId);

        if (existingFormat.equals("")) {
            UUID id = UUID.randomUUID();
            query = String.format("insert into public.users_format (id, user_id, format) VALUES ('%s', '%s', '%s');",
                    id, userId, format.toString());
        } else {
            query = String.format("update public.users_format set format = '%s' where user_id = '%s';", format.toString(), userId);
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

    public String getFormatByUserId(String userId) {
        String selectQuery = String.format("select format from public.users_format where user_id = '%s';", userId);

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
