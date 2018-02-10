package com.example.lea.tictactoe;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, String, String> {

    private String add_data_url;

    @Override
    protected String doInBackground(String... strings) {


        String user, password, highscore;
        user = strings[0];
        password = strings[1];
        highscore = strings[2];

        try {
            System.out.println("Im Alive");
            URL url = new URL(add_data_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            System.out.println("CONNECTED TO DB");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data_string = URLEncoder.encode("user", "UTF-8")+"="+URLEncoder.encode(user, "UTF-8")+"&"+
                    URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&" +
                    URLEncoder.encode("highscore", "UTF-8")+"="+URLEncoder.encode(highscore, "UTF-8");

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();

            httpURLConnection.disconnect();

            return "One Row of data instered";

        } catch (MalformedURLException e) {
            System.out.println("URL");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("YOU FAILED SON");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println("result" +  result);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        add_data_url = "https://lea.forest4fun.biz/addData.php";
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
