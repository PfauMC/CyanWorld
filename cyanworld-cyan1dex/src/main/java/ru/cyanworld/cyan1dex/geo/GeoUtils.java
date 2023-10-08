package ru.cyanworld.cyan1dex.geo;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

public class GeoUtils {
    public static GeoLocation getIpGeoBaseDataByIp(String ip) {
        GeoLocation geoloc = new GeoLocation();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(GeoUtils.makeGetRequest("http://ipgeobase.ru:7020/geo?ip=" + ip))));
            document.getDocumentElement().normalize();
            geoloc.setCountry(document.getElementsByTagName("country").item(0).getTextContent());
            geoloc.setCity(document.getElementsByTagName("city").item(0).getTextContent());
            geoloc.setRegion(document.getElementsByTagName("region").item(0).getTextContent());
            geoloc.setDistrict(document.getElementsByTagName("district").item(0).getTextContent());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return geoloc;
    }

    public static String makeGetRequest(String urlstring) throws IOException {
        String line;
        URL url = new URL(urlstring);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "windows-1251"));
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
