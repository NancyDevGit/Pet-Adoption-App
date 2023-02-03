package com.example.fundacionalbornozjimenez.Actividades;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.myVolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class RegistroRefugio extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerReRes;
    private EditText alias, correo, nombre,apellidos,telefono,ciudad,estado,calle,numero,colonia,delegacion_m;
    private String aliasS,correoS,nombreS,apellidosS,telefonoS,ciudadS,estadoS,calleS,numeroS,coloniaS,delegacion_mS;
    private String seleccionado;
    private Button registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_refugio);

        spinnerReRes=findViewById(R.id.spinnerTipoR);
        spinnerReRes.setPrompt("Elige si es regufio o rescatista");
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.refugioORescatista,
                R.layout.spinner_text);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerReRes.setAdapter(adapter2);
        spinnerReRes.setOnItemSelectedListener(this);

        alias=findViewById(R.id.cAlias);
        correo=findViewById(R.id.cCorreoR);
        nombre=findViewById(R.id.cNombresR);
        apellidos=findViewById(R.id.cApellidosR);
        telefono=findViewById(R.id.cTelefonoR);
        ciudad=findViewById(R.id.cCiudadR);
        estado=findViewById(R.id.cEstadoR);
        calle=findViewById(R.id.cCalleR);
        numero=findViewById(R.id.cNumeroR);
        colonia=findViewById(R.id.cColoniaR);
        delegacion_m=findViewById(R.id.cDel_MunR);

        registrar=findViewById(R.id.buttonEnviarR);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aliasS=alias.getText().toString();
                correoS=correo.getText().toString();
                nombreS=nombre.getText().toString();
                apellidosS=apellidos.getText().toString();
                telefonoS=telefono.getText().toString();
                ciudadS=ciudad.getText().toString();
                estadoS=estado.getText().toString();
                calleS=calle.getText().toString();
                numeroS=numero.getText().toString();
                coloniaS=colonia.getText().toString();
                delegacion_mS=delegacion_m.getText().toString();


                if (seleccionado.equals("Refugio")){
                    if (TextUtils.isEmpty(aliasS) || TextUtils.isEmpty(correoS) || TextUtils.isEmpty(ciudadS)
                            || TextUtils.isEmpty(estadoS) || TextUtils.isEmpty(telefonoS)){
                        Toast.makeText(getApplicationContext(), "Completa los campos obligatorios marcados con *", Toast.LENGTH_SHORT).show();
                        if(TextUtils.isEmpty(aliasS)){
                            alias.setError("Ingresa el alias del refugio");
                        }
                        if(TextUtils.isEmpty(correoS)){
                            correo.setError("Ingresa el correo del refugio");
                        }
                        if(TextUtils.isEmpty(ciudadS)){
                            ciudad.setError("Ingresa la ciudad");
                        }
                        if(TextUtils.isEmpty(estadoS)){
                            estado.setError("Ingresa el estado");
                        }
                        if(TextUtils.isEmpty(telefonoS)){
                            telefono.setError("Ingresa el telefono");
                        }



                    }else{
                        enviarDatos();
                    }


                }else if(seleccionado.equals("Rescatista")){
                    if (TextUtils.isEmpty(aliasS) || TextUtils.isEmpty(correoS) || TextUtils.isEmpty(ciudadS)
                            || TextUtils.isEmpty(estadoS) || TextUtils.isEmpty(telefonoS) ||
                            TextUtils.isEmpty(nombreS) || TextUtils.isEmpty(apellidosS)){
                        Toast.makeText(getApplicationContext(), "Completa los campos obligatorios marcados con *", Toast.LENGTH_SHORT).show();

                        if(TextUtils.isEmpty(aliasS)){
                            alias.setError("Ingresa el alias del rescatista");
                        }
                        if(TextUtils.isEmpty(correoS)){
                            correo.setError("Ingresa el correo del rescatista");
                        }
                        if(TextUtils.isEmpty(ciudadS)){
                            ciudad.setError("Ingresa la ciudad");
                        }
                        if(TextUtils.isEmpty(estadoS)){
                            estado.setError("Ingresa el estado");
                        }
                        if(TextUtils.isEmpty(telefonoS)){
                            telefono.setError("Ingresa el telefono");
                        }
                        if(TextUtils.isEmpty(nombreS)){
                            nombre.setError("Ingresa el nombre del rescatista");
                        }
                        if(TextUtils.isEmpty(apellidosS)){
                            apellidos.setError("Ingresa los apellidos del rescatista");
                        }

                    }else{
                        enviarDatos();
                    }
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        seleccionado = adapterView.getItemAtPosition(i).toString();

        if (seleccionado.equals("Refugio")){
            alias.setHint("*Nombre del refugio");
            correo.setHint("*Correo del refugio");
            nombre.setVisibility(View.GONE);
            nombreS="";
            apellidos.setVisibility(View.GONE);
            apellidosS="";
            telefono.setHint("*Telefono del refugio");

        }else if(seleccionado.equals("Rescatista")){
            alias.setHint("*Alias del rescatista");
            correo.setHint("*Correo del rescatista");
            nombre.setVisibility(View.VISIBLE);
            apellidos.setVisibility(View.VISIBLE);
            telefono.setHint("*Telefono del rescatista");
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void enviarDatos(){

        String url="https://fundacionalbornoz.com/registroRefugio.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.trim().equalsIgnoreCase("Correo ya registrado")){
                    Toast.makeText(getApplicationContext(),"Este correo ya fue utilizado por otro regufio o rescatista "
                            ,Toast.LENGTH_SHORT).show();
                }else if(response.trim().equalsIgnoreCase("Alias coincide con refugio ya registrado")){
                    Toast.makeText(getApplicationContext(),"Alias coincide con refugio ya registrado"
                            ,Toast.LENGTH_SHORT).show();
                }else if(response.trim().equalsIgnoreCase("Nombre, apellidos y alias coinciden con rescatista ya registrado")){
                    Toast.makeText(getApplicationContext(),"Nombre, apellidos y alias coinciden con rescatista ya registrado",Toast.LENGTH_SHORT).show();
                }else if(response.trim().equalsIgnoreCase("Error1") || response.trim().equalsIgnoreCase("Error2")){
                    Toast.makeText(getApplicationContext(),"Ocurrio un error al enviar la solicitud de registro",Toast.LENGTH_SHORT).show();
                }else if(response.trim().equalsIgnoreCase("Registro nuevo")){
                    Toast.makeText(getApplicationContext(),"El refugio fue registrado exitosamente",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido enviar la solicitud, intentelo más tarde",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parametros=new HashMap<>();
                parametros.put("tipo",seleccionado);
                parametros.put("alias",aliasS);
                parametros.put("correo",correoS);
                parametros.put("telefono",telefonoS);
                parametros.put("ciudad",ciudadS);
                parametros.put("estado",estadoS);
                parametros.put("calle",calleS);
                parametros.put("numero",numeroS);
                parametros.put("colonia",coloniaS);
                parametros.put("delegacion_m",delegacion_mS);
                parametros.put("nombre",nombreS);
                parametros.put("apellidos",apellidosS);


                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myVolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

}