package ru.cyanworld.cyan1dex.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class YandexTranslator {
    public List<String> apiKeys;
    Random random;

    public YandexTranslator(List<String> apiKeys) {
        this.apiKeys = apiKeys;
        this.random = new Random();
    }

    public String translate(String text, String toLang) {
        String fianltext = "null";
        try {
            fianltext = this.getJSONResponse(this.getTranslateUrl(text, toLang)).getAsJsonObject().get("text").getAsString();
            System.out.println("Переводим: " + text + " Получаем: " + fianltext);
        } catch (IOException iOException) {
            // empty catch block
        }
        return fianltext;
    }

    public JsonElement getJSONResponse(String url) throws IOException {
        return new JsonParser().parse(this.getResponse(url));
    }

    public String convertStreamToString(InputStream is) {
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

    public String getResponse(String url) throws IOException {
        URLConnection con = new URL(url).openConnection();
        con.setUseCaches(true);
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        con.connect();
        return this.convertStreamToString(con.getInputStream());
    }

    public String getTranslateUrl(String text, String toLang) {
        return "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + this.apiKeys.get(this.random.nextInt(this.apiKeys.size())) + "&lang=" + toLang + "&text=" + text.replace(" ", "%20");
    }
}
