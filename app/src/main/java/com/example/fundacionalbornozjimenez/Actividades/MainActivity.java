package com.example.fundacionalbornozjimenez.Actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.AdministradorPreferencias;
import com.example.fundacionalbornozjimenez.model.ValidacionUsuario;

public class MainActivity extends AppCompatActivity {
    private String usuario, pss;
    private int a=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recuperarDatosSesion();

        if (new AdministradorPreferencias(MainActivity.this).checarSesionCerrada()){

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int value=2;
                    Intent intent = new Intent(MainActivity.this, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    startActivity(intent);

                }
            }, 4000);

        }else{
            int value=1;
            Intent intent = new Intent(MainActivity.this, PaginaPrincipal.class);
            intent.putExtra("key",value);
            startActivity(intent);
            finish();
            //validacionUsuario(usuario,pss);

        }

        //startActivity(intent);

    }

    private void recuperarDatosSesion(){
        usuario=new AdministradorPreferencias(MainActivity.this).correo();
        pss= new AdministradorPreferencias(MainActivity.this).pss();

    }


    private void validacionUsuario(String usuario, String pss){
        new ValidacionUsuario(MainActivity.this, usuario,pss);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}