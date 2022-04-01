package com.example.etilometro.gestioneUtente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etilometro.adapter.RecyclerAdapterAcquisizioni;
import com.example.etilometro.oggetti.Acquisizioni;
import com.example.etilometro.oggetti.Drink;
import com.example.etilometro.oggetti.Utente;
import com.example.etilometro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class InfoUtenteActivity extends AppCompatActivity {

    public static final String EXTRA = "EXTRA";
    private TextView tvNome, tvSesso, tvPeso;
    private ImageView ivUtente;

    private RecyclerView recyclerView;
    private RecyclerAdapterAcquisizioni recViewAdapter;

    SharedPreferences settings;

    private static String url_Acquisizione;// = "http://10.0.2.2:8080/ServerEtilometro/AcquisizioneServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_utente);
        tvNome = findViewById(R.id.tvNomeUtente2);
        tvSesso = findViewById(R.id.tvSessoUtente2);
        tvPeso = findViewById(R.id.tvPesoUtente2);
        ivUtente = findViewById(R.id.ivUtente2);
        recyclerView = findViewById(R.id.rvAcquisizioni);

        settings = getSharedPreferences("Login", 0);
        url_Acquisizione = getResources().getString(R.string.servlet_Acquisizione);

         Intent startIntent = getIntent();
        if(startIntent.hasExtra(EXTRA)) {
            Utente utente = ((Utente) getIntent().getSerializableExtra(EXTRA));
            tvNome.setText((((Utente) getIntent().getSerializableExtra(EXTRA)).getNome()));
            tvPeso.setText(String.valueOf(((Utente) getIntent().getSerializableExtra(EXTRA)).getPeso()));
            tvSesso.setText(((Utente) getIntent().getSerializableExtra(EXTRA)).getSesso());

            if(utente.getSesso().equals("Maschio")){
                ivUtente.setImageResource(R.drawable.man);
            }else if (utente.getSesso().equals("Femmina")){
                ivUtente.setImageResource(R.drawable.woman);
            }
        }

        new AcquisizioneGETService().execute(tvNome.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void modifica(MenuItem item) {
        Intent intent = new Intent(InfoUtenteActivity.this, AggiungiUtenteActivity.class);
        intent.putExtra(AggiungiUtenteActivity.EXTRA_1, ((Utente) getIntent().getSerializableExtra(EXTRA)) );
        startActivityForResult(intent, 1);

    }

    private Observer<List<Acquisizioni>> userListUpdateObserver = new Observer<List<Acquisizioni>>() {
        @Override
        public void onChanged(List<Acquisizioni> acquisizioniArrayList) {
            recViewAdapter = new RecyclerAdapterAcquisizioni(InfoUtenteActivity.this, acquisizioniArrayList);;
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(recViewAdapter);
        }
    };


    class AcquisizioneGETService extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jo = null;
            URL url;
            String utente = params[0];
            String username = settings.getString("username", "");
            try {
                url = new URL(url_Acquisizione
                        + "?utente=" + URLEncoder.encode(utente)
                        + "&account=" + URLEncoder.encode(username));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = br.readLine();
                jo = new JSONArray(data);
                return jo;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            if (jsonArray != null) {
              List<Acquisizioni> listAcquisizioni = new ArrayList<>();
                List<Drink> drinkList = new ArrayList<>();
              try {
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject rec = jsonArray.getJSONObject(i);
                        double alcol = rec.getDouble("alcol");
                        String data = rec.getString("data");
                        double pesoAttuale = rec.getDouble("peso");
                        String stringListaDrink = rec.getString("listaDrink");
                        Acquisizioni acquisizioni = new Acquisizioni(alcol, data, stringListaDrink, pesoAttuale);
                       listAcquisizioni.add(acquisizioni);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recViewAdapter = new RecyclerAdapterAcquisizioni(InfoUtenteActivity.this, listAcquisizioni);;
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(recViewAdapter);
            }
        }
    }
}
