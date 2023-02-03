package com.example.fundacionalbornozjimenez.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fundacionalbornozjimenez.Actividades.PaginaPrincipal;
import com.example.fundacionalbornozjimenez.R;

public class Fundacion extends Fragment {
    private View root=getView();
    private TextView texto1,texto2;
    private ImageView iconoF;

    public Fundacion() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_fundacion, container, false);


        PaginaPrincipal.bFundacion.setBackgroundColor(PaginaPrincipal.bAdopciones.
                getContext().getResources().getColor(R.color.albornoz));

        PaginaPrincipal.bAdopciones.setBackgroundColor(PaginaPrincipal.bAdopciones.
                getContext().getResources().getColor(R.color.cBotonSoft));


        texto1=(TextView) root.findViewById(R.id.txtFundacion);
        texto2=(TextView) root.findViewById(R.id.txtFundacion2);

        iconoF=(ImageView) root.findViewById(R.id.fFace);

        iconoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(abrirFace());

            }
        });

        return root;

    }

    private Intent abrirFace() {
        String url="https://www.facebook.com/Fundaci%C3%B3n-Albornoz-Jim%C3%A9nez-1000658333438569";
        Uri uri=Uri.parse(url);

        try {
            getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/426253597411506"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, uri);
        }
    }

}