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

// Klasse kann einen Zahlenwert aus PHP-Schnittstelle mit Benutzername und Passwort anfragen
public class LoginGetScore extends AsyncTask<String, Void, String> {

    // Schnittstelle für Weitergabe der Server-Antwort an Hauptklasse
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    // Konstruktor für Uebergabe des Activity-Objekts
    public LoginGetScore(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    /*  Methode zum Anfragen der PHP-Schnittstelle
     *  @param arg0[0] ; Benutzername
     *  @param arg0[1] ; Passwort
     */
    @Override
    protected String doInBackground(String... arg0) {
        try {
            // speichern der Parameter
            String username = (String) arg0[0];
            String password = (String) arg0[1];
            
            String link = "http://suplex.bplaced.net/LoginGetScore.php"; // PHP-Schnittstelle
            String data = URLEncoder.encode("username", "UTF-8") + "=" + // Vorbereitung des POST-Body / Daten
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection(); // repräsentiert eine Verbindung, aber noch nicht wirklich verbunden

            // Vorbereitung für Übertragung der Daten
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); // Die Verbindung wird hier implizit hergestellt

            wr.write(data); // PHP-Schnittstelle verwendet die POST-Methode, deswegen müssen wir unsere Daten im "Körper"
            wr.flush();     // senden

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream())); // Hier wird die eigentliche Anfrage erst verschickt und die Antwort eingelesen

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Warte auf Server Antwort
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString(); // Die Antwort wird an onPostExecute weitergegeben

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    // Methode kümmert sich um die Server-Antwort
    // @param result ; Server-Antwort, bzw. Resultat von doInBackground
    @Override
    protected void onPostExecute(String result){
        delegate.processFinish(result); // Die Server-Antwort wird nun an das Activity-Objekt weitergeleiten
    }
}
