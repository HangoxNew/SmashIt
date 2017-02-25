package com.example.smashit;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Jannik Adam on 24.02.2017.
 */

public class LoginGetScore extends AsyncTask<String, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public LoginGetScore(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String username = (String) arg0[0];
            String password = (String) arg0[1];

            String link = "http://suplex.bplaced.net/LoginGetScore.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Warte auf Server Antwort
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result){
        delegate.processFinish(result);
    }
}
