package me.krutikov.schedule.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@UtilityClass
public class RequestUtils {

    public String postRequest(String urlString, String requestBody) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if (responseCode != 200) {
            throw new Exception();
        }

        return response.toString();
    }

}
