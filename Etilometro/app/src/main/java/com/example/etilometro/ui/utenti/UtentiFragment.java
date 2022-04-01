package com.example.etilometro.ui.utenti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etilometro.gestioneUtente.AggiungiUtenteActivity;
import com.example.etilometro.MainActivity;
import com.example.etilometro.R;
import com.example.etilometro.adapter.RecyclerAdapterUtenti;
import com.example.etilometro.oggetti.Utente;
import com.example.etilometro.ui.home.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class UtentiFragment extends Fragment {

    private HomeViewModel homeViewModel;
    MainActivity context;

    private static final String TAG = "ANDREA";
    public static final String EXTRA_2 = "EXTRA_2";
    final ColorDrawable background = new ColorDrawable(Color.RED);

    private RecyclerView recyclerView;
    private RecyclerAdapterUtenti recViewAdapter;
    FloatingActionButton fab;
    TextView tvHint;

    private Paint p = new Paint();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_utenti, container, false);
        context = (MainActivity) getActivity();
        recyclerView = root.findViewById(R.id.rvUtenti);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getUserMutableLiveData().observe( getViewLifecycleOwner(), userListUpdateObserver);
        tvHint = root.findViewById(R.id.idHint);

        Log.d(TAG, "onCreateView: SIZE" + homeViewModel.getUserMutableLiveData().getValue().size() );


        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if (direction == ItemTouchHelper.RIGHT) {
                            homeViewModel.removeUtente(viewHolder.getLayoutPosition());
                            Snackbar mySnackbar = Snackbar.make(context.findViewById(R.id.nav_host_fragment),
                                    "Utente eliminato", Snackbar.LENGTH_SHORT);
                            mySnackbar.show();
                        }
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        try{

                            Bitmap icon;
                            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                                View itemView = viewHolder.itemView;
                                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                                float width = height / 3;

                                if(dX > 0){
                                    p.setColor(Color.parseColor("#F44336"));
                                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop() + 16, dX,(float) itemView.getBottom() - 16);
                                    c.drawRect(background,p);
                                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_delete_forever);
                                    RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                                    c.drawBitmap(icon,null,icon_dest,p);
                                }
                            }
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                        }catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        fab = root.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AggiungiUtenteActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    Observer<List<Utente>> userListUpdateObserver = new Observer<List<Utente>>() {
        @Override
        public void onChanged(List<Utente> userArrayList) {
            recViewAdapter = new RecyclerAdapterUtenti(context,userArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(recViewAdapter);
            if(recViewAdapter.getItemCount() == 0){
                tvHint.setVisibility(View.VISIBLE);
            }else {
                tvHint.setVisibility(View.INVISIBLE);
            }
        }
    };


}

