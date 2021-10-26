package util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class CentralBankDataProvider {

    public static InputStream getExchangeRatesRange(String currency, Date start, Date end) {

        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UrlBuilder.getUrlForExchangeRates(currency, start, end))
                    .build(); // defaults to GET

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.body().byteStream();
    }
}
