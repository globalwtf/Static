package com.minexd.zoot.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class APIUtil {

    public static String getUsername(UUID uuid) {
        try {

            URL obj = new URL("https://api.minetools.eu/profile/" + uuid.toString().replace("-", ""));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setInstanceFollowRedirects(true);
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            con.connect();
            if (con.getResponseCode() != 200) {
                return null;
            }
            InputStream input = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            con.disconnect();

            JsonObject jsonObject = new JsonParser().parse(result.toString()).getAsJsonObject();

            try {
                return jsonObject.get("decoded").getAsJsonObject().get("profileName").getAsString();
            } catch (Exception ex) {
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NONE";
    }

    public static UUID getUUID(String name) {
        try {

            URL obj = new URL("https://api.minetools.eu/uuid/" + name);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setInstanceFollowRedirects(true);
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            con.connect();
            if (con.getResponseCode() != 200) {
                return null;
            }
            InputStream input = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            con.disconnect();

            JsonObject jsonObject = new JsonParser().parse(result.toString()).getAsJsonObject();

            try {
                return UUID.fromString(jsonObject.get("id").getAsString().replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5"));
            } catch (Exception ex) {
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
