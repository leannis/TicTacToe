package com.example.lea.tictactoe;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class BackgroundTask extends AsyncTask<String, String, String> {

    private String add_data_url, json_url, method, user, password;

    public BackgroundTask() {}

    public BackgroundTask(String method)  {
        this.method = method;
    }

    @Override
    protected String doInBackground(String... strings) {
        String query = strings[0];

        if(this.method.equals("addData")) {
            try {
                URL url = new URL(add_data_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = URLEncoder.encode("query", "UTF-8")+"="+URLEncoder.encode(query, "UTF-8");
                System.out.println('#' + data_string);
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();

                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                httpURLConnection.disconnect();

                return "One Row of data instered";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (this.method.equals("getData")) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = URLEncoder.encode("query", "UTF-8")+"="
                        +URLEncoder.encode(query, "UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream, "iso-8859-1"));

                StringBuilder response = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            parseJSON(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (method.equals("addData")) {
            add_data_url = "https://lea.forest4fun.biz/addQuery.php";
        } else if(method.equals("getData")) {
            json_url = "https://lea.forest4fun.biz/getData.php";
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    public void parseJSON(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("server_response");

        for (int i = 0; i < jsonArray.length(); i++) {

            user = jsonArray.getJSONObject(i).getString("user");
            password = jsonArray.getJSONObject(i).getString("password");
        }
        System.out.println("user " + user + "password" + password);
    }

    public Map<String, String> getUser() throws JSONException {

        System.out.println("getUser");

        HashMap<String, String> users = new HashMap<>();

        users.put("user", user);
        users.put("password", password);

        return users;
    }
}
