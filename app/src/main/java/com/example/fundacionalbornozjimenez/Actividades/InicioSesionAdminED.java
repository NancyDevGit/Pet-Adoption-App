package com.example.fundacionalbornozjimenez.Actividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.AdministradorPreferencias;
import com.example.fundacionalbornozjimenez.model.ValidacionUsuario;

public class InicioSesionAdminED extends AppCompatActivity {
    private EditText user;
    private EditText cont;
    private Button botonIniciar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion_admin);

        //Desactivar captura de pantalla
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        user=findViewById(R.id.user);
        cont=findViewById(R.id.pass);
        botonIniciar=findViewById(R.id.botonInicio);

        recuperarDatosSesion(user,cont);

        botonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u=user.getText().toString().trim();
                String p=cont.getText().toString().trim();

                if (!u.isEmpty() && !p.isEmpty()){
                    validacionUsuario(u,p);


                }else if(u.isEmpty() && !p.isEmpty()){
                    user.setError("Ingresa tu correo");
                    user.requestFocus();
                }else if(!u.isEmpty() && p.isEmpty()){
                    cont.setError("Ingresa tu contraseña");
                    cont.requestFocus();
                }else {
                    user.setError("Ingresa tu correo");
                    cont.setError("Ingresa tu contraseña");
                    Toast.makeText(getApplicationContext(),"Ingresa correo y contraseña",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void validacionUsuario(String usuario, String pss){
        View view=findViewById(android.R.id.content).getRootView();
        new ValidacionUsuario(InicioSesionAdminED.this,view, usuario,pss);
    }

    private void recuperarDatosSesion(EditText usuario,EditText pass){
        usuario.setText(new AdministradorPreferencias(this).correo());
        pass.setText(new AdministradorPreferencias(this).pss());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent=new Intent(InicioSesionAdminED.this, PaginaPrincipal.class);
        startActivity(intent);
    }
}
