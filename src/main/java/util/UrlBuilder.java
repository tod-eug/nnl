package util;

import okhttp3.HttpUrl;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UrlBuilder {

    public URL getUrlForExchangeRates(String currency, Date startDate, Date endDate) {

        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);

        String start = df.format(startDate);
        String end = df.format(endDate);

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("cbr.ru")
                .addPathSegments("scripts/XML_dynamic.asp")
                .addQueryParameter("date_req1", start)
                .addQueryParameter("date_req2", end)
                .addQueryParameter("VAL_NM_RQ", currency)
                .build().url();
        return url;
    }
}
