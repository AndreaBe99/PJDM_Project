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

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "ANDREA";
    TextInputEditText emailSignup, usernameSignup, passwordSignup;
    Button vailogin, signup;
    SharedPreferences settings;
    String emSignup, unSignup, pwSignup, md5String;
    boolean erroreUtenteSignup = false, erroreUtenteLogin = false;

    private static String url_login; //= "http://10.0.2.2:8080/ServerEtilometro/AccountServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        settings = getSharedPreferences("Login", 0);

        url_login = getResources().getString(R.string.servlet_Account);

        emailSignup = findViewById(R.id.emailSingup);
        usernameSignup = findViewById(R.id.usernameSingup);
        passwordSignup = findViewById(R.id.passwordSingup);

        vailogin = findViewById(R.id.btVailogin);
        signup = findViewById(R.id.signup);
    }

    public void signup(View view) {
        emSignup = emailSignup.getText().toString();
        unSignup = usernameSignup.getText().toString();
        pwSignup = passwordSignup.getText().toString();

        //Password
        byte[] md5Input = pwSignup.toString().getBytes();
        BigInteger md5Data = null;
        try {
            md5Data = new BigInteger(1, md5.encryptMD5(md5Input));
        }catch (Exception e){
            e.printStackTrace();
        }
        md5String = md5Data.toString();


        try{
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.container).getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        new SignupService().execute(url_login);
    }

    public void vaiLogin(View v) {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }

    class SignupService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            JSONObject jo = null;
            try {
                URL url = new URL(params[0] + "?username="
                        + URLEncoder.encode(unSignup)
                        + "&password=" +  URLEncoder.encode(md5String) //pwSignup
                        + "&email=" +  URLEncoder.encode(emSignup));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = br.readLine();
                Log.d(TAG, "doInBackground: " + data);
                //jo = new JSONObject(data);
                return data;
            }   catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e);
            } catch ( FileNotFoundException e) {
                e.printStackTrace();
                erroreUtenteSignup = true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e);
            } /*catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: " + e);
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(String i) {
            super.onPostExecute(i);
            Log.d(TAG, "onPostExecute: " + i);
            if (i != null) {
                if(i.equals("Tutto OK")){
                    erroreUtenteSignup = false;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("username", unSignup).apply();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                }
            }
            if(erroreUtenteSignup){
                Snackbar.make(findViewById(R.id.containerSignup), "Username gia in uso da un altro utente!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setBackgroundTint(getResources().getColor(R.color.colorError))
                        .show();
            }

        }
    }
}