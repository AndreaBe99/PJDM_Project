package com.example.etilometro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etilometro.oggetti.Acquisizioni;
import com.example.etilometro.R;

import java.util.List;

public class RecyclerAdapterAcquisizioni extends RecyclerView.Adapter<RecyclerAdapterAcquisizioni.ItemViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private LayoutInflater inflater;
    private final List<Acquisizioni> listaAcquisizioni;

    public List<Acquisizioni> getListRecyclerItem() {
        return listaAcquisizioni;
    }

    public RecyclerAdapterAcquisizioni(Context context, List<Acquisizioni> listaAcquisizioni) {
        this.context = context;
        this.listaAcquisizioni = listaAcquisizioni;
        inflater = LayoutInflater.from(context);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView peso, gradi, data, listaAlcolici, tvAlcolemia, tvGl;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            gradi = itemView.findViewById(R.id.tvAlcolemiaUtente);
            data = itemView.findViewById(R.id.tvDataUtente);
            peso = itemView.findViewById(R.id.tvPesoUtente);
            listaAlcolici = itemView.findViewById(R.id.tvAlcolici);

            tvAlcolemia = itemView.findViewById(R.id.tvAlcolemia);
            tvGl = itemView.findViewById(R.id.tvGl);
        }

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View row = inflater.inflate(R.layout.row_acquisizione,parent,false);
        ItemViewHolder vh = new ItemViewHolder(row);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position) {
        Acquisizioni acquisizione = (Acquisizioni) listaAcquisizioni.get(position);
        viewHolder.gradi.setText(String.valueOf(acquisizione.getGradi()));
        if (acquisizione.getGradi() >= 0.5){
            viewHolder.gradi.setTextColor(context.getResources().getColor(R.color.colorError));
            viewHolder.tvAlcolemia.setTextColor(context.getResources().getColor(R.color.colorError));
            viewHolder.tvGl.setTextColor(context.getResources().getColor(R.color.colorError));
        } else{
            viewHolder.gradi.setTextColor(Color.rgb(0,255,0));
            viewHolder.tvAlcolemia.setTextColor(Color.rgb(0,255,0));
            viewHolder.tvGl.setTextColor(Color.rgb(0,255,0));
        }

        viewHolder.data.setText(acquisizione.getData());
        viewHolder.peso.setText(String.valueOf(acquisizione.getPeso()));
        viewHolder.listaAlcolici.setText(acquisizione.getTvdrink());
    }

    @Override
    public int getItemCount() {
        return listaAcquisizioni.size();
    }
}