package ru.cyanworld.cyan1dex.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

// Кушать же надо было на что-то в то время...
public class UrlToMoney {
    public static String getUrl(String text) throws IOException {
        return UrlToMoney.getJSONResponse(UrlToMoney.getTranslateUrl(text)).getAsJsonObject().get("shortenedUrl").getAsString();
    }

    public static JsonElement getJSONResponse(String url) throws IOException {
        return new JsonParser().parse(UrlToMoney.getResponse(url));
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, "UTF-8");
        s.useDelimiter("\\A");
        try {
            String st = s.hasNext() ? s.next() : "";
            s.close();
            return st;
        } catch (Exception ex) {
            s.close();
            return "";
        }
    }

    public static String getResponse(String url) throws IOException {
        URLConnection con = new URL(url).openConnection();
        con.setUseCaches(true);
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        con.connect();
        return UrlToMoney.convertStreamToString(con.getInputStream());
    }

    public static String getTranslateUrl(String text) {
        return "https://api.shorte.st/s/7baca5b034ff9876ca24dd0753717bf5/" + text;
    }
}
