package util;

import bot.TaxBot;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CentralBankDataProvider {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public InputStream getExchangeRatesRange(String currency, Date start, Date end) {
        UrlBuilder urlBuilder = new UrlBuilder();

        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlBuilder.getUrlForExchangeRates(currency, start, end))
                    .build(); // defaults to GET

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Error while requesting exchange rates from central bank. Exception: ", e);
            }
            return response.body().byteStream();
    }
}
