package com.example.etilometro.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etilometro.oggetti.Drink;
import com.example.etilometro.R;

import java.util.List;

public class RecyclerAdapterDrink extends RecyclerView.Adapter<RecyclerAdapterDrink.ItemViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private LayoutInflater inflater;
    private final List<Drink> listRecyclerItem;

    public List<Drink> getListRecyclerItem() {
        return listRecyclerItem;
    }

    public RecyclerAdapterDrink(Context context, List<Drink> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
        inflater = LayoutInflater.from(context);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name, gradi, litri, quantita;
        private ImageView imgDrink;
        private Button btPiu, btMeno;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNomeBevanda);
            gradi = itemView.findViewById(R.id.tvAlcolemia);
            litri = itemView.findViewById(R.id.tvLitri);

            quantita = itemView.findViewById(R.id.tvQuantita);

            imgDrink = itemView.findViewById(R.id.ivUtente);

            btPiu = itemView.findViewById(R.id.btPiu);
            btPiu.setOnClickListener(this);

            btMeno = itemView.findViewById(R.id.btMeno);
            btMeno.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int num = listRecyclerItem.get(getLayoutPosition()).getQuantita();
            if (v.getId() == btPiu.getId() && num < 30) {
                num = num + 1;
                listRecyclerItem.get(getLayoutPosition()).setQuantita(num);
            } else if (v.getId() == btMeno.getId() && num != 0) { // Se la quantita è uguale a 0 non va decrementata
                    num = num -1;
                    listRecyclerItem.get(getLayoutPosition()).setQuantita(num);
            }
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View row = inflater.inflate(R.layout.row_drink,parent,false);
        ItemViewHolder vh = new ItemViewHolder(row);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Drink drink = (Drink) listRecyclerItem.get(position);

        // Imposta imageView partendo da una stringa attraverso la quale "ricava" la risorsa
        Resources res = context.getResources();
        String mDrawableName = drink.getImg();
        int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName()); // ID della risrorsa

        holder.name.setText(drink.getName());
        holder.gradi.setText(String.valueOf(drink.getGradi()));
        holder.litri.setText(String.valueOf(drink.getLitri()));
        holder.imgDrink.setImageResource(resID);
        holder.quantita.setText(String.valueOf(listRecyclerItem.get(position).getQuantita()));

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}