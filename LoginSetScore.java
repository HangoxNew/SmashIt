package com.example.smashit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Jannik Adam on 24.02.2017.
 */


// Klasse kann einen Zahlenwert über PHP-Schnittstelle mit Benutzername und Passwort abspeichern
public class LoginSetScore extends AsyncTask<String, Void, String> {

    Context context;    // Platzhalter für Kontext des aktuellen Programms

    public LoginSetScore(Context context){      // Konstruktor, uebergibt Kontext
        this.context = context;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String username = (String) arg0[0];
            String password = (String) arg0[1];
            String score    = (String) arg0[2];

            String link = "http://suplex.bplaced.net/LoginSetScore.php"; // PHP-Schnittstelle
            String data = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("score", "UTF-8") + "=" +
                    URLEncoder.encode(score, "UTF-8");

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
            return String.valueOf(e.getCause());
        }
    }

    @Override
    protected void onPostExecute(String response) {
        if (response.equalsIgnoreCase("ok")){
            Toast.makeText(context, "Wurde gespeichert!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }
    }
}
