package com.example.etilometro.gestioneUtente;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.etilometro.MainActivity;
import com.example.etilometro.R;
import com.example.etilometro.oggetti.Utente;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AggiungiUtenteActivity extends AppCompatActivity {

    public static final String EXTRA_1 = "EXTRA_1";
    private Utente utente = null;

    private TextInputEditText etNome, etPeso;
    private Button btAggiornaModifica;
    private ImageButton ibUtente;

    private static String url_Utente; // = "http://10.0.2.2:8080/ServerEtilometro/UtenteServlet";
    SharedPreferences settings;
    boolean erroreUtente = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_utente);

        settings = getSharedPreferences("Login", 0);
        url_Utente = getResources().getString(R.string.servlet_Utente);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        etNome = findViewById(R.id.username);
        etPeso = findViewById(R.id.etPeso);
        ibUtente = findViewById(R.id.ibUtente);

        btAggiornaModifica = findViewById(R.id.btAggiungiModifica);
        btAggiornaModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getSerializableExtra(EXTRA_1) == null){      // Se riceviamo un utente allora lo modifichiamo altrimenti lo inseriamo
                    utente = new Utente( etNome.getText().toString(), Double.parseDouble(etPeso.getText().toString()),
                            spinner.getSelectedItem().toString(), settings.getString("username", ""));
                    if(utente.getPeso()>=200){
                        Snackbar.make(findViewById(R.id.idAggiungiUtenteLayout), "Inserire un peso inferiore a 200kg.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .setBackgroundTint(getResources().getColor(R.color.colorError))
                                .show();
                    } else {
                        String username = settings.getString("username", "");
                        String jsonInputString = "{" +
                                "\"nome\":\""+ utente.getNome()+"\", " +
                                "\"sesso\":\"" + utente.getSesso()+ "\", " +
                                "\"peso\":" + String.valueOf(utente.getPeso())+ ", " +
                                "\"account\":\"" + username+ "\"" +
                                "}";
                        String parametri = "?json=" + URLEncoder.encode(jsonInputString);
                        new AggiungiModificaService().execute(url_Utente, parametri, "POST");
                    }
                } else {
                    Utente utente = (Utente) getIntent().getSerializableExtra(EXTRA_1);
                    utente.setPeso(Double.parseDouble(etPeso.getText().toString()));
                    if(utente.getPeso()>=200){
                        Snackbar.make(findViewById(R.id.idAggiungiUtenteLayout), "Inserire un peso inferiore a 200kg.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .setBackgroundTint(getResources().getColor(R.color.colorError))
                                .show();
                    } else {
                        String username = settings.getString("username", "");
                        String parametri = "?nome=" + URLEncoder.encode(utente.getNome())
                                + "&account=" + URLEncoder.encode(username)
                                + "&peso=" + URLEncoder.encode(String.valueOf(utente.getPeso()));
                        new AggiungiModificaService().execute(url_Utente, parametri, "POST");
                    }
                }
            }
        });

        Intent startIntent = getIntent();
        if(startIntent.hasExtra(EXTRA_1)){
            utente = ((Utente) getIntent().getSerializableExtra(EXTRA_1));
            etNome.setText((((Utente) getIntent().getSerializableExtra(EXTRA_1)).getNome()));
            etPeso.setText(String.valueOf(((Utente) getIntent().getSerializableExtra(EXTRA_1)).getPeso()));
            if(((Utente) getIntent().getSerializableExtra(EXTRA_1)).getSesso().equals("Maschio"))
                spinner.setSelection(0);
            else
                spinner.setSelection(1);
            if(utente.getSesso().equals("Maschio")){
                ibUtente.setImageResource(R.drawable.man);
            }else if (utente.getSesso().equals("Femmina")){
                ibUtente.setImageResource(R.drawable.woman);
            }
            btAggiornaModifica.setText(getResources().getString(R.string.modifica));
            etNome.setFocusable(false);
            spinner.setEnabled(false);
            spinner.setClickable(false);
            etNome.setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }

    class AggiungiModificaService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0] + params[1]);
                String method = params[2];

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod(method);
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = br.readLine();
                return data;
            }  catch ( FileNotFoundException e) {
                e.printStackTrace();
                erroreUtente = true;
            }catch ( IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(erroreUtente){
                Snackbar.make(findViewById(R.id.idAggiungiUtenteLayout), "Utente con lo stesso nome gia presente!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setBackgroundTint(getResources().getColor(R.color.colorError))
                        .show();
            }else{
                if(s.equals("AGGIUNTO") || s.equals("MODIFICATO")){
                    Intent intent = new Intent(AggiungiUtenteActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(findViewById(R.id.idAggiungiUtenteLayout), "C'è stato un errore lato Server.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setBackgroundTint(getResources().getColor(R.color.colorError))
                            .show();
                }
            }

        }
    }


}

