package com.example.etilometro.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etilometro.R;
import com.example.etilometro.gestioneUtente.InfoUtenteActivity;
import com.example.etilometro.oggetti.Utente;

import java.util.List;

public class RecyclerAdapterUtenti extends RecyclerView.Adapter<RecyclerAdapterUtenti.ItemViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private LayoutInflater inflater;
    private final List<Utente> listaUtenti;

    public List<Utente> getListRecyclerItem() {
            return listaUtenti;
            }

    public RecyclerAdapterUtenti(Context context, List<Utente> listaUtenti) {
        this.context = context;
        this.listaUtenti = listaUtenti;
        inflater = LayoutInflater.from(context);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nome, sesso, peso;
        private ImageView imgUtente;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvNomeUtente);
            sesso = itemView.findViewById(R.id.tvSessoUtente);
            peso = itemView.findViewById(R.id.tvPesoUtente);
            imgUtente = itemView.findViewById(R.id.ivUtente);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), InfoUtenteActivity.class);
            intent.putExtra(InfoUtenteActivity.EXTRA,  listaUtenti.get(getLayoutPosition()));
            ((Activity) v.getContext()).startActivityForResult(intent, 1);
            notifyDataSetChanged();
        }
    }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View row = inflater.inflate(R.layout.row_utente,parent,false);
            ItemViewHolder vh = new ItemViewHolder(row);
            return vh;

        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

            Utente utente = (Utente) listaUtenti.get(position);
            holder.nome.setText(utente.getNome());
            holder.sesso.setText(utente.getSesso());
            holder.peso.setText(String.valueOf(utente.getPeso()));
            if(utente.getSesso().equals("Maschio")){
                holder.imgUtente.setImageResource(R.drawable.man);
            }else if (utente.getSesso().equals("Femmina")){
                holder.imgUtente.setImageResource(R.drawable.woman);
            }

        }

        @Override
        public int getItemCount() {
            return listaUtenti.size();
        }
}