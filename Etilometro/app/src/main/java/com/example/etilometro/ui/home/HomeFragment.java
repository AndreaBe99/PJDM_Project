package com.example.etilometro.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etilometro.adapter.RecyclerAdapterDrink;
import com.example.etilometro.adapter.SpinAdapter;
import com.example.etilometro.ui.DialogRisultatoActivity;
import com.example.etilometro.MainActivity;
import com.example.etilometro.oggetti.Acquisizioni;
import com.example.etilometro.oggetti.Drink;
import com.example.etilometro.oggetti.Utente;
import com.example.etilometro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    MainActivity context;

    private RecyclerView recyclerView;
    private RecyclerAdapterDrink recViewAdapter;

    private Spinner spinner;
    private SpinAdapter spinAdapter;

    private RadioGroup radioGroupPasto;
    private RadioButton radioPasto;
    private double alcolemia;
    private boolean selezionatoRadioGroup = false;

    FloatingActionButton fab;

    private  HomeViewModel homeViewModel;

    private String drinkList = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = (MainActivity) getActivity();
        recyclerView = root.findViewById(R.id.rvBevande);
        spinner = root.findViewById(R.id.spinnerUtente);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getDrinkMutableLiveData().observe( getViewLifecycleOwner(), drinkListUpdateObserver);
        homeViewModel.getUserMutableLiveData().observe( getViewLifecycleOwner(), userListUpdateObserver);

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcolaAlcolemia();
            }
        });

        radioGroupPasto = root.findViewById(R.id.rgQuando);
        radioGroupPasto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedID) {
                radioPasto = root.findViewById(checkedID);
                selezionatoRadioGroup = true;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < recViewAdapter.getListRecyclerItem().size(); j++) // Resetta le quantità se cambia utente
                    recViewAdapter.getListRecyclerItem().get(j).setQuantita(0);
                recViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return root;
    }

   private Observer<List<Utente>> userListUpdateObserver = new Observer<List<Utente>>() {
        @Override
        public void onChanged(List<Utente> userArrayList) {
            spinAdapter = new SpinAdapter(context, R.layout.spinner_item, userArrayList);
            spinner.setAdapter(spinAdapter); // Set the custom adapter to the spinner
        }
    };

   private Observer<List<Drink>> drinkListUpdateObserver = new Observer<List<Drink>>() {
        @Override
        public void onChanged(List<Drink> drinkArrayList) {
            recViewAdapter = new RecyclerAdapterDrink(context,drinkArrayList);;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(recViewAdapter);
        }
    };


    private void calcolaAlcolemia(){
        int countAlcol, gradi;
        double grammiAlcol = 0, litri;
        boolean diversiDaZero = false;
        for (int i = 0; i < recViewAdapter.getListRecyclerItem().size(); i++){
            countAlcol = recViewAdapter.getListRecyclerItem().get(i).getQuantita();
            if(countAlcol != 0) {
                diversiDaZero = true;   // Nella riga successiva metto da parte i nomi degli alcolici che ho bevuto
                drinkList = drinkList + " " + recViewAdapter.getListRecyclerItem().get(i).getQuantita() + " " + recViewAdapter.getListRecyclerItem().get(i).getName() + ",";
                recViewAdapter.getListRecyclerItem().get(i).setQuantita(0);
            }
            litri = (double)recViewAdapter.getListRecyclerItem().get(i).getLitri()/1000;
            gradi = recViewAdapter.getListRecyclerItem().get(i).getGradi();
            grammiAlcol += countAlcol*(8*gradi*litri);
        }
        if (spinner.getSelectedItem() == null){
            Snackbar.make(getView(), "Inserire prima un utente.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setBackgroundTint(getResources().getColor(R.color.colorError))
                    .show();
        } else if(!selezionatoRadioGroup){
            Snackbar.make(getView(), "Inserire prima quando hai bevuto.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setBackgroundTint(getResources().getColor(R.color.colorError))
                    .show();
        } else if(!diversiDaZero) {
            Snackbar.make(getView(), "Inserire prima cosa hai bevuto.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setBackgroundTint(getResources().getColor(R.color.colorError))
                    .show();
        } else {
            Utente utente = (Utente) spinner.getSelectedItem();
            if(utente.getSesso().equals("Maschio")){
                if(radioPasto.getId() == R.id.rbDurante) alcolemia = grammiAlcol / (utente.getPeso() * 1.2);    // *costanti
                else if(radioPasto.getId() == R.id.rbUnoOra) alcolemia = grammiAlcol / (utente.getPeso() * 1);
                else if(radioPasto.getId() == R.id.rbDueOra) alcolemia = grammiAlcol / (utente.getPeso() * 0.7);
            } else if(utente.getSesso().equals("Femmina")){
                if(radioPasto.getId() == R.id.rbDurante) alcolemia = grammiAlcol / (utente.getPeso() * 0.9);
                if(radioPasto.getId() == R.id.rbUnoOra) alcolemia = grammiAlcol / (utente.getPeso() * 0.7);
                if(radioPasto.getId() == R.id.rbDueOra) alcolemia = grammiAlcol / (utente.getPeso() * 0.5);
            }
            alcolemia = Math.floor(alcolemia * 100) / 100;
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            String formattedDate = df.format(date);

            drinkList = drinkList.substring(0, drinkList.length() - 1); // Tolgo eventuali spazi e/o virgole dalla stringa degll'elenco degli alcolici
            drinkList = drinkList.substring(1);

            Acquisizioni acquisizioni = new Acquisizioni( alcolemia, formattedDate, drinkList, utente.getPeso());
            homeViewModel.addAcquisizione(utente, acquisizioni);
            drinkList = "";

            Intent intent = new Intent(getContext(), DialogRisultatoActivity.class);
            intent.putExtra(DialogRisultatoActivity.ESITO, alcolemia);
            startActivityForResult(intent, 1);
        }
    }
}
