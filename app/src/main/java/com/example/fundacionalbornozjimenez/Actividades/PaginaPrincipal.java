package com.example.fundacionalbornozjimenez.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fundacionalbornozjimenez.Fragments.Adopciones;
import com.example.fundacionalbornozjimenez.Fragments.Fundacion;
import com.example.fundacionalbornozjimenez.Fragments.PublicacionesAdmin;
import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.AdministradorPreferencias;

public class PaginaPrincipal extends AppCompatActivity  {
    private TextView tAdmin;
    public static Button bAdopciones, bFundacion;
    private ImageView iSalir,iFundacion;

    private Fundacion fundacion = new Fundacion();
    private Adopciones adopciones = new Adopciones();
    private PublicacionesAdmin publicacionesAdmin= new PublicacionesAdmin();

    private boolean doubleBackToExitPressedOnce = false;
    private int value,value2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_principal);

        tAdmin=findViewById(R.id.textoAdmin);
        iSalir=findViewById(R.id.imgSalir);
        iFundacion=findViewById(R.id.imgAlbornoz);
        bAdopciones=findViewById(R.id.botonAdopciones);
        bFundacion=findViewById(R.id.botonFundacion);

        //Mostrar fragmento de adopciones
        //cargarFragmento(adopciones);


        tAdmin.setVisibility(View.GONE);
        iSalir.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getInt("key");
            switch (value){
                //Administrador adopciones
                case 1:
                    tAdmin.setVisibility(View.VISIBLE);
                    iSalir.setVisibility(View.VISIBLE);
                    cargarFragmento(adopciones);
                    paginaAdmin();
                    break;
                //Visitante adopciones//
                case 2:
                    tAdmin.setVisibility(View.GONE);
                    iSalir.setVisibility(View.GONE);
                    cargarFragmento(adopciones);
                    paginaVisitante();
                    break;

                //Administrador publicaciones
                case 3:
                    //Toast.makeText(PaginaPrincipal.this,"caso 3", Toast.LENGTH_LONG).show();
                    tAdmin.setVisibility(View.VISIBLE);
                    iSalir.setVisibility(View.VISIBLE);
                    cargarFragmento(publicacionesAdmin);
                    paginaAdmin();
                    break;
            }

        }else{

            tAdmin.setVisibility(View.GONE);
            iSalir.setVisibility(View.GONE);
            cargarFragmento(adopciones);
            paginaVisitante();
        }


        bAdopciones=findViewById(R.id.botonAdopciones);
        bFundacion=findViewById(R.id.botonFundacion);


    }


    private void cargarFragmento(Fragment fragment){

        if(fragment.equals(adopciones) || fragment.equals(fundacion)){
            value2=1;
        }else if (fragment.equals(publicacionesAdmin)){
            //Toast.makeText(this, "Prueba fragmento", Toast.LENGTH_SHORT).show();
            value2=2;
        }

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer,fragment);
        transaction.commit();
    }

    private void paginaAdmin(){
        Bundle bundle = new Bundle();
        String mostrar="admin";
        bundle.putString("mostrar",mostrar);
        adopciones.setArguments(bundle);
        fundacion.setArguments(bundle);

        iSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaginaPrincipal.this,"Cerrando sesión", Toast.LENGTH_LONG).show();
                String p="";
                new AdministradorPreferencias(PaginaPrincipal.this).guardarSoloCorreo(p);
                Intent intent=new Intent(PaginaPrincipal.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        bAdopciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFragmento(adopciones);
            }
        });

        bFundacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFragmento(fundacion);

            }
        });

        tAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFragmento(publicacionesAdmin);

            }
        });

        iFundacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFragmento(adopciones);
            }
        });

    }

    private void paginaVisitante(){
        Bundle bundle = new Bundle();
        String mostrar="visitante";
        bundle.putString("mostrar",mostrar);
        adopciones.setArguments(bundle);
        fundacion.setArguments(bundle);

         bAdopciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bAdopciones.requestFocusFromTouch();
                tAdmin.setVisibility(View.GONE);
                cargarFragmento(adopciones);
            }
        });

        bFundacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tAdmin.setVisibility(View.VISIBLE);
                tAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(PaginaPrincipal.this, InicioSesionAdminED.class);
                        startActivity(intent);
                        finish();
                    }
                });
                cargarFragmento(fundacion);

            }
        });


        iFundacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFragmento(adopciones);
            }
        });
    }


    @Override
    public void onBackPressed() {

        switch (value2){

            case 1:
                if (doubleBackToExitPressedOnce) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Presiona nuevamente para salir", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
                break;
            case 2:
                cargarFragmento(fundacion);
                break;

        }
    }
}