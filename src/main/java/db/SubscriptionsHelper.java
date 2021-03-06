package db;

import bot.TaxBot;
import util.DateUtil;
import util.PropertiesProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubscriptionsHelper {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());
    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    public void setSubscriptionEndDate(long tgIdLong, long chatIdLong) {

        String query = "";
        String tgId = Long.toString(tgIdLong);
        String chatId = Long.toString(chatIdLong);
        Date existingEndDate = getSubscriptionEndDateByTgId(tgIdLong);

        if (existingEndDate.before(new Date())) {
            String deleteQuery = String.format("delete from public.subscriptions where tg_id = '%s';", tgId);
            DatabaseHelper dbHelper = new DatabaseHelper();
            try {
                dbHelper.getPreparedStatement(deleteQuery).execute();
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Error while deleteSubscriptionByTgId. Exception: ", e);
            } finally {
                dbHelper.closeConnections();
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        UUID id = UUID.randomUUID();
        String createdDate = formatter.format(new Date());
        String endDate = formatter.format(adjustFinalDate(new Date()));

        query = String.format("insert into public.subscriptions (id, tg_id, chat_id, created_date, end_date) VALUES ('%s', '%s', '%s', '%s', '%s');",
                id, tgId, chatId, createdDate, endDate);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(query).execute();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error while setSubscriptionEndDate. Exception: ", e);
        } finally {
            dbHelper.closeConnections();
        }
    }

    public Date getSubscriptionEndDateByTgId(long tgIdLong) {
        String tgId = Long.toString(tgIdLong);
        String selectQuery = String.format("select end_date from public.subscriptions where tg_id = '%s';", tgId);

        DatabaseHelper dbHelper = new DatabaseHelper();
        DateUtil dateUtil = new DateUtil();
        Date endDate = dateUtil.increaseDate(new Date(), -1);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            ResultSet st = dbHelper.getPreparedStatement(selectQuery).executeQuery();
            if (st.next()) {
                endDate = formatter.parse(st.getString(1));
            }
        } catch (SQLException | ParseException e) {
            log.log(Level.SEVERE, "Error while getSubscriptionEndDateByTgId. Exception: ", e);
        } finally {
            dbHelper.closeConnections();
        }
        return endDate;
    }

    private Date adjustFinalDate(Date sourceDate) {
        DateUtil dateUtil = new DateUtil();
        return dateUtil.increaseDate(sourceDate, Integer.parseInt(PropertiesProvider.configurationProperties.get("days")));
    }
}
