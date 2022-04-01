package com.example.etilometro.ui.home;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.etilometro.JsonActivity;
import com.example.etilometro.R;
import com.example.etilometro.oggetti.Acquisizioni;
import com.example.etilometro.oggetti.Drink;
import com.example.etilometro.oggetti.Utente;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeViewModel extends AndroidViewModel {

    private static final int MODE_APPEND = 0 ;
    MutableLiveData<List<Drink>> drinkLiveData;
    MutableLiveData<List<Utente>> utenteLiveData;
    MutableLiveData<List<Acquisizioni>> acquisizioniLiveData;
    //In your network successfull response

    private static String url_Utente;// = "http://10.0.2.2:8080/ServerEtilometro/UtenteServlet";
    private static String url_Acquisizione;// = "http://10.0.2.2:8080/ServerEtilometro/AcquisizioneServlet";

    SharedPreferences settings;
    List<Drink> drinkArrayList;
    List<Utente> userArrayList;
    List<Acquisizioni> acquisizioniArrayList;

    List<Acquisizioni> listaAcquisizioni = new ArrayList<>();



    public HomeViewModel(@NonNull Application application) {
        super(application);

        url_Utente = application.getResources().getString(R.string.servlet_Utente);
        url_Acquisizione = application.getResources().getString(R.string.servlet_Acquisizione);

        drinkLiveData = new MutableLiveData<>();
        utenteLiveData = new MutableLiveData<>();
        acquisizioniLiveData = new MutableLiveData<>();
        settings = application.getSharedPreferences("Login", 0);
        // call your Rest API in init method
        init();
    }

    public MutableLiveData<List<Acquisizioni>> getAcquisizioniLiveData() {
        return acquisizioniLiveData;
    }

    public MutableLiveData<List<Drink>> getDrinkMutableLiveData() {
        return drinkLiveData;
    }

    public MutableLiveData<List<Utente>> getUserMutableLiveData() {
        return utenteLiveData;
    }

    public void init(){
        populateList();
        drinkLiveData.setValue(drinkArrayList);
        utenteLiveData.setValue(userArrayList);
        acquisizioniLiveData.setValue(acquisizioniArrayList);
    }



    public void addAcquisizione(Utente utente, Acquisizioni acquisizione){
        int indiceUtente = userArrayList.indexOf(utente);

        if(userArrayList.get(indiceUtente).getAcquisizioniList() != null){
            listaAcquisizioni =  userArrayList.get(indiceUtente).getAcquisizioniList();
        }
        listaAcquisizioni.add(acquisizione);
        userArrayList.get(indiceUtente).setAcquisizioniList(listaAcquisizioni);
        utenteLiveData.postValue(userArrayList);

        String jsonInputString = "{" +
                    "\"alcol\":"+String.valueOf(acquisizione.getGradi())+", " +
                    "\"data\":\"" + acquisizione.getData()+ "\", " +
                    "\"peso\":" + String.valueOf(utente.getPeso())+ ", " +
                    "\"utente\":\"" + utente.getNome()+ "\", " +
                    "\"account\":\"" + utente.getAccount()+ "\", " +
                    "\"listaDrink\":\"" + acquisizione.getTvdrink() + "\"" +
                    "}";
       // String parametri = "?json=" + URLEncoder.encode(jsonInputString);
        new ListService().execute(url_Acquisizione, "json" , "POST", jsonInputString);
    }

    public void populateList() {
        drinkArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();

        String username = settings.getString("username", "");
        String parametri = "?account=" + URLEncoder.encode(username);
        new ListService().execute(url_Utente, parametri , "GET");

        // JSON DRINK
        JsonActivity jsonActivity = new JsonActivity();
        List<Drink> listDrink = null;
        try {
            InputStream inputStream = getApplication().getResources().openRawResource(R.raw.drink);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            listDrink = jsonActivity.readJsonStreamDrink(inputStreamReader);
            drinkLiveData.postValue(listDrink);
            drinkArrayList.addAll(listDrink);
            Log.d("ANDREA", "read UTENTI: Buon Esito ");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ANDREA", "read DRINK: ERRORE ");
        }
    }

    public void removeUtente(int layoutPosition) {
        Utente utente = userArrayList.get(layoutPosition);
        userArrayList.remove(layoutPosition);
        utenteLiveData.postValue(userArrayList);
        //new UtenteDELETEService().execute(utente);
        String username = settings.getString("username", "");
        String parametri = "?nome=" + URLEncoder.encode(utente.getNome()) + "&account=" + URLEncoder.encode(username);
        new ListService().execute(url_Utente, parametri , "DELETE");
    }

    class ListService extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jo = null;
            URL url = null;
            try {
                if(params[1].equals("json")){
                    url = new URL(params[0]);
                }else{
                    url = new URL(params[0] + params[1]);
                }

                String method = params[2];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);

                StringBuilder response = null;
                if(params[1].equals("json")){
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    String jsonInputString = params[3];
                    try(OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }

                    }
                    Log.d(TAG, "doInBackground CODE: " + response.toString());
                    jo = new JSONArray(response.toString());
                    return jo;
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = br.readLine();
                Log.d(TAG, "doInBackground: " + data);
                jo = new JSONArray(data);
                return jo;
            }  catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            if (jsonArray != null) {
                Gson gson = new Gson();
                Type userListType = new TypeToken<List<Utente>>(){}.getType();
                List<Utente> listUtente = gson.fromJson(String.valueOf(jsonArray), userListType);
                userArrayList.addAll(listUtente);
                utenteLiveData.postValue(userArrayList);
            }
        }
    }

}

