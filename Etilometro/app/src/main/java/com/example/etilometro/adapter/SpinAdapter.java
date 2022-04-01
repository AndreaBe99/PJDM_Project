package com.example.etilometro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.etilometro.oggetti.Utente;
import com.example.etilometro.R;

import java.util.List;

public class SpinAdapter extends ArrayAdapter<Utente> {

    private final LayoutInflater mInflater;
    private final int mResource;
    private List<Utente> listUtenti;
    private Context context;


    public SpinAdapter(Context context, int resource, List<Utente> listUtenti) {
        super(context, resource, 0, listUtenti);
        this.context = context;
        this.listUtenti = listUtenti;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
    }

    @Override
    public int getCount(){
        return listUtenti.size();
    }

    @Override
    public Utente getItem(int position){
        return listUtenti.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);

    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);
        TextView nome = view.findViewById(R.id.tvSpinnerNome);
        TextView sesso = view.findViewById(R.id.tvSpinnerSesso);
        TextView peso = view.findViewById(R.id.tvSpinnerPeso);

        nome.setText(listUtenti.get(position).getNome());
        sesso.setText(listUtenti.get(position).getSesso());
        String weight = String.valueOf(listUtenti.get(position).getPeso()) + " kg";
        peso.setText(weight);
        return view;
    }
}
