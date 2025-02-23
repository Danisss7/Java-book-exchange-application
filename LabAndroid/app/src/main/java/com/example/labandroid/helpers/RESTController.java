package com.example.labandroid.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class RESTController {
    //logika kurios reikes apdorojant requests ir responses
    private static BufferedWriter bufferedWriter;
    private static OutputStream outputStream;

    public static String sendGet(String urlGet) throws IOException {
        URL url = new URL(urlGet);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        int code = httpURLConnection.getResponseCode();
        System.out.println("Response code: " + code);

        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
                break;
            }
            in.close();
            return response.toString();

        } else {
            return "";
        }
    }

    public static String sendPost(String urlPost, String postDataParams) throws IOException {
        URL url = new URL(urlPost);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        System.out.println(postDataParams);
        //Reikia papildomu connectiono nustatymu, jie bus bendri su Put metodu
        setConnectionParameters(httpURLConnection, "POST");
        outputStream = httpURLConnection.getOutputStream();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        bufferedWriter.write(postDataParams);
        //? bufferedWriter.flush();
        bufferedWriter.close();
        outputStream.close();

        int code = httpURLConnection.getResponseCode();
        System.out.println("Response code: " + code);

        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
                break;
            }
            in.close();
            return response.toString();

        } else {
            return "";
        }
    }

    public static String sendPut(String url, String postDataParams) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlObj.openConnection();

            // Set the connection parameters
            setConnectionParameters(httpURLConnection, "PUT");

            // Write the request body to the output stream
            outputStream = httpURLConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(postDataParams);
            bufferedWriter.flush();  // Make sure the data is sent
            bufferedWriter.close();
            outputStream.close();

            // Get the response code
            int code = httpURLConnection.getResponseCode();
            System.out.println("Response code: " + code);

            // If the response is OK, read the response body
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sendDelete(String urlDelete) throws IOException {
        URL url = new URL(urlDelete);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("DELETE");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        int code = httpURLConnection.getResponseCode();
        System.out.println("Response code: " + code);

        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
                break;
            }
            in.close();
            return response.toString();

        } else {
            return "";
        }
    }

    private static void setConnectionParameters(HttpURLConnection httpURLConnection, String type) throws ProtocolException {
        httpURLConnection.setRequestMethod(type);
        httpURLConnection.setReadTimeout(20000);
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
    }
}
