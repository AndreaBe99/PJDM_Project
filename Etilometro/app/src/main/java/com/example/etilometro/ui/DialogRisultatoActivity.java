package com.example.etilometro.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.etilometro.AlarmReceiver;
import com.example.etilometro.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

public class DialogRisultatoActivity extends AppCompatActivity {

    private static final String TAG = "ANDREA";
    public static final String ESITO = "ESITO";

    ConstraintLayout cdPositivo, cdNegativo;
    TextView tvGradi, tvOre, tvMinuti;
    ImageView ivCar;
    Button btNotifica, btMessaggio;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_risultato);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        tvGradi = findViewById(R.id.tvAlcolemia);
        tvOre = findViewById(R.id.tvOre);
        tvMinuti = findViewById(R.id.tvMinuti);
        ivCar = findViewById(R.id.ivCar);
        btNotifica = findViewById(R.id.btNotifica);
        btMessaggio = findViewById(R.id.btMessaggio);

        cdNegativo = findViewById(R.id.cdNegativo);
        cdPositivo = findViewById(R.id.cdPositivo);

        Intent startIntent = getIntent();
        if (startIntent.hasExtra(ESITO)) {
            double alcolemia = startIntent.getDoubleExtra(ESITO, 0.0);
            //double alcolemia = Double.parseDouble(stringExtra);
            tvGradi.setText(String.valueOf(alcolemia));
            if (alcolemia >= 0.5) { // Se il risultato è al disopra dei limiti di legge
                double attesa = (alcolemia - 0.5) / 0.10;
                attesa = (Math.floor(attesa * 100) / 100);

                // Divido il double per la conversione in ore e minuti
                String[] arr = String.valueOf(attesa).split("\\.");
                int[] oreMinuti = new int[2];
                oreMinuti[0] = Integer.parseInt(arr[0]); // Ore
                oreMinuti[1] = Integer.parseInt(arr[1]); // Minuti

                if (oreMinuti[1] < 10 && oreMinuti[1] > 0) // Se attesa è nel formato 1.4 moltiplico 4*10 = 40 minuti
                    oreMinuti[1] = oreMinuti[1] * 10;
                if (oreMinuti[1] > 60) {                  // Se i minuti sono maggiori di 60 correggo il formato
                    oreMinuti[1] = oreMinuti[1] - 60;
                    oreMinuti[0]++;
                }
                cdNegativo.setVisibility(View.INVISIBLE);
                cdPositivo.setVisibility(View.VISIBLE);
                tvOre.setText(String.valueOf(oreMinuti[0]));
                tvMinuti.setText(String.valueOf(oreMinuti[1]));
                ivCar.setImageResource(R.drawable.carno);
                btNotifica.setVisibility(View.VISIBLE);
                btMessaggio.setVisibility(View.VISIBLE);
            } else {
                cdNegativo.setVisibility(View.VISIBLE);
                cdPositivo.setVisibility(View.INVISIBLE);
                ivCar.setImageResource(R.drawable.caryes);
                btNotifica.setVisibility(View.INVISIBLE);
                btMessaggio.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void annulla(View view) {
        Intent returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnintent);
        finish();
    }

    public void notifica(View view) {
        try {
            int minuti = Integer.parseInt(tvMinuti.getText().toString());
            int ore = Integer.parseInt(tvOre.getText().toString());
            int secondiTot = (minuti + ore * 60) * 60;

            // Come esempio ho impostato i secondi della notifica uguali a 10 in modo da far vedere il suo funzionamento
            secondiTot = 10;

            Intent intent = new Intent(this, AlarmReceiver.class);
            // Intent lanciato in futuro da qualcun'altro
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1234, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            long delay = System.currentTimeMillis() + secondiTot * 1000;

            // RTC real time clock di android
            // Quindi stiamo dicendo di lanciare questo alarm dopo 'delay' ms e l'applicazione che lo deve gestire
            alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent); // Mandiamo cosi un allarm in futuro

            Snackbar.make(view, "Notifica impostata tra " + ore + " ore e " + minuti + " minuti.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d(TAG, "notifica: Alarm in " + secondiTot + " seconds");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void messaggio(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (!checkLocation()) {
                            Log.d(TAG, "CHECK");
                            return;
                        }
                        if (location != null) {
                            // Logic to handle location object
                            Log.d(TAG, "run:  " + location.getLatitude() + " " + location.getLongitude());
                            String uri = "Vienimi a prendere qui:  http://maps.google.com/maps?daddr=" + location.getLatitude() + "," + location.getLongitude();
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String ShareSub = "La mia posizione";
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                            startActivity(Intent.createChooser(sharingIntent, "Condividi con:"));
                        } else {
                            Log.d(TAG, "LOCATION NULL ASPETTA SYNC GPS");
                            Toast.makeText(DialogRisultatoActivity.this, "GPS non ancora sincronizzato.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("La localizzazione è impostata su 'Off'.\nAttiva la localizzazione per usare l'app.")
                .setPositiveButton("Impostazioni GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }




}
