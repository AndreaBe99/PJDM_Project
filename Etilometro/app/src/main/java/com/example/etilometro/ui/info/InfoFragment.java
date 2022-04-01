package com.example.etilometro.ui.info;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.etilometro.ui.LoginActivity;
import com.example.etilometro.NetActivity;
import com.example.etilometro.R;

public class InfoFragment extends Fragment {

    private TextView tvNumeroVersionName, tvNumberVersionCode;
    private  String version;
    private  int verCode;

    ImageButton ministero, aci;
    Button logout;
    WebView webView;
    SharedPreferences settings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_info, container, false);

        settings = this.getActivity().getSharedPreferences("Login", 0);
        tvNumeroVersionName = root.findViewById(R.id.tvNumeroVersionName);
        tvNumberVersionCode = root.findViewById(R.id.tvNumberVersionCode);
        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.edit().putString("username", "").apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            version = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvNumeroVersionName.setText(version);
        tvNumberVersionCode.setText(String.valueOf(verCode));

        ministero = root.findViewById(R.id.ibMinistero);
        ministero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), NetActivity.class);
                //startActivity(intent);

                String url = "http://www.salute.gov.it/portale/temi/p2_6.jsp?lingua=italiano&id=2349&area=alcol&menu=vuoto";
                Intent intent = new Intent(getActivity(), NetActivity.class);
                intent.putExtra(NetActivity.EXTRA_NET,  url);
                startActivityForResult(intent, 1);
            }
        });

        aci = root.findViewById(R.id.ibAci);
        aci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), NetActivity.class);
                //startActivity(intent);

                String url = "http://www.aci.it/laci/sicurezza-stradale/alcool-e-guida/cosa-dice-la-legge.html";
                Intent intent = new Intent(getActivity(), NetActivity.class);
                intent.putExtra(NetActivity.EXTRA_NET,  url);
                startActivityForResult(intent, 1);
            }
        });



        return root;
    }

}

