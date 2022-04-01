package com.example.etilometro.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.etilometro.MainActivity;
import com.example.etilometro.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "ANDREA";
    TextInputEditText usernameLogin, passwordLogin;
    Button login, vaiSignup;
    SharedPreferences settings;
    String unLogin, pwLogin, md5String;
    boolean erroreUtenteLogin = false, erroreConnessione = false;

    private static String url_login;// = "http://10.0.2.2:8080/ServerEtilometro/AccountServlet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences("Login", 0);

        url_login = getResources().getString(R.string.servlet_Account);

        String username = settings.getString("username", "");
        Log.d(TAG, "onCreate: " + username);
        if (!username.isEmpty()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        usernameLogin = findViewById(R.id.username);
        passwordLogin = findViewById(R.id.password);

        login = findViewById(R.id.login);
        vaiSignup = findViewById(R.id.btVaiSIgnup);

    }


    public void vaiSignup(View v) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        finish();
    }

    public void login(View v) {
        unLogin = usernameLogin.getText().toString();
        pwLogin = passwordLogin.getText().toString();
        //Password
        byte[] md5Input = pwLogin.getBytes();
        BigInteger md5Data = null;
        try {
            md5Data = new BigInteger(1, md5.encryptMD5(md5Input));
        }catch (Exception e){
            e.printStackTrace();
        }
        md5String = md5Data.toString();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.container).getWindowToken(), 0);
        new LoginService().execute(url_login);
    }


    class LoginService extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jo = null;
            try {
                URL url = new URL(params[0] + "?username="
                        + URLEncoder.encode(unLogin)
                        + "&password=" + URLEncoder.encode(md5String)); //pwLogin
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = br.readLine();
                Log.d(TAG, "doInBackground LOGIN: " + data);
                jo = new JSONObject(data);
                return jo;
            } catch ( FileNotFoundException e) {
                e.printStackTrace();
                erroreUtenteLogin = true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject i) {
            super.onPostExecute(i);
            Log.d(TAG, "onPostExecute: " + i);
            if (i != null) {
                try {
                    String username = i.getString("username");
                    String password = i.getString("password");
                    erroreUtenteLogin = false;
                    settings.edit().putString("username", username).apply();
                   // settings.edit().putString("password", password).apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(erroreUtenteLogin){
                Snackbar.make(findViewById(R.id.container), "Utente non registrato o password errata.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setBackgroundTint(getResources().getColor(R.color.colorError))
                        .show();
            }
        }
    }




}