package db;

import bot.TaxBot;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentsHelper {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public void createRawDocument(String id, String userId, String filename) {
        String insertQuery = String.format("insert into raw_documents (id, user_id, filename) VALUES ('%s', '%s', '%s');",
                id, userId, filename);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(insertQuery).execute();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error while createRawDocument. Exception: ", e);
        } finally {
            dbHelper.closeConnections();
        }
    }

    public void createProcessedDocument(String id, String userId, String rawDocumentId, String filename) {
        String insertQuery = String.format("insert into processed_documents (id, user_id, raw_document_id, filename) VALUES ('%s', '%s', '%s', '%s');",
                id, userId, rawDocumentId, filename);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(insertQuery).execute();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error while createProcessedDocument. Exception: ", e);
        } finally {
            dbHelper.closeConnections();
        }
    }
}
