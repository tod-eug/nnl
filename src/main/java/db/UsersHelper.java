package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsersHelper {

    public String createUser(String tgId, String userName, String firstName, String lastName, boolean isBot, String languageCode) {

        UUID id = UUID.randomUUID();

        String insertQuery = String.format("insert into users (id, tg_id, user_name, first_name, last_name, is_bot, language_code) VALUES ('%s', '%s', '%s', '%s', '%s', %s, '%s');",
                id, tgId, userName, firstName, lastName, isBot, languageCode);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(insertQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
        return id.toString();
    }

    public String findUserByTgId(String tgId) {
        String selectQuery = String.format("select id from public.users where tg_id = '%s';", tgId);

        DatabaseHelper dbHelper = new DatabaseHelper();
        String id = "";
        try {
            ResultSet st = dbHelper.getPreparedStatement(selectQuery).executeQuery();
            if (st.next()) {
                id = st.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
        return id;
    }
}
