package com.example.smashit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoginGetScore.AsyncResponse {

    private EditText usernameField, passwordField;  // Eingabefelder Benutzername und Passwort
    private TextView number;                        // Die Zahl, die angezeigt wird
    private LinearLayout login;                     // Das Layout für den Login
    private String username, password;              // speichert für späteren Set Benutzername und Passwort. Unsauber?
    private int score = 0;                       // speichert den Score
    private int old_score;                       // speichert einen Referenzwert


    // Hier passiert alles was beim Starten der App ausgeführt wird
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assoziere Variablen mit ihre entsprechenden Elemente
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        number   = (TextView) findViewById(R.id.number);
        login    = (LinearLayout) findViewById(R.id.layout_login);

        // Bevor nicht eingeloggt wurde soll nichts anderes anklickbar sein
        findViewById(R.id.counter).setClickable(false);
        findViewById(R.id.submit).setClickable(false);

    }

    /* Hier passiert alles nach dem Login, als Antwort
     * @param response ; Antwort der Verbindung
     */
    @Override
    public void processFinish(String response){

        // Ist die Antwort eine Zahl?
        if (isInteger(response)) {

            score = Integer.parseInt(response);     // speichert Antwort als unseren Score
            old_score = score;                      // speichert Referenzwert
            display(score);                         // zeigt den Score an
            findViewById(R.id.counter).setClickable(true);  // jetzt darf man die anderen Knoepfe bedienen
            findViewById(R.id.submit).setClickable(true);
            login.setVisibility(View.GONE);         // Das Login-Layout wird geschlossen
        }
        else {

            // keine Zahl? Dann auch Fehler
            number.setText("Ungültige Anmeldedaten");
            score = 0;
        }
    }

    // Aufgerufen, wenn Login Knopf gedrückt wird
    // Holt Score aus der Datenbank
    public void login(View view) {
        username = usernameField.getText().toString();  // holt Benutzername und speichert global
        password = passwordField.getText().toString();  // holt Passwort und speichert global
        new LoginGetScore(this).execute(username,password); // ruft den momentane Score online ab
    }

    // Zeigt den Score auf dem Bildschirm an
    private void display(long text){
        TextView numberText = (TextView) findViewById(R.id.number);
        numberText.setText(String.valueOf(text));
    }

    // Aufgerufen, wenn irgendwo, ausser Submit und nach Login, gedrueckt wird
    // Erhoeht den Score  um eins und zeigt diesen an
    public void increment(View view) {
        display(++score);
    }

    // Aufgerufen, wenn Submit Knopf gedrueckt wird
    // Speichert den Score in der Datenbank
    public void submit(View view) {

        // speichert nur wenn man mind. 10 mal gedrueckt hat
        if (score-old_score >= 10 ) {
            new LoginSetScore(this).execute(username, password, "" + score);    // speichert den momentanen Score online ab
        }
        else {
            Toast.makeText(this, "Tipp erstmal!", Toast.LENGTH_SHORT).show(); // Warnung, soll mind. 10 mal drücken
        }
        old_score = score; // Muss nach jeder Submit-Knopf betaetigung seinen Score um mind. 10 erhöhen
    }

    /* Überprüft ob ein String, eine Ganzzahl ist
     * @param str ; der zu ueberpruefende String
     */
    private boolean isInteger(String str) {
        if (str == null) {              // str ueberhaupt zugewiesen?
            return false;
        }
        int length = str.length();
        if (length == 0) {              // steht was im String?
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {     // steht ein minus an erster Stelle? Und wenn ja
            if (length == 1) {          // stehen danach noch zahlen?
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {       //ist jedes vorkommende Zeichen eine Zahl?
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
