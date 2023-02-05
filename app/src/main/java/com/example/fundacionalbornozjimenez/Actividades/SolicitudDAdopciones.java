package com.example.fundacionalbornozjimenez.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SolicitudDAdopciones extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button enviarSolicitud;
    private EditText cnombres, capellidos,cedad,ccorreo,ctelefono,cocupacion,cmascotas,ccalle,cncasa,
            ccolonia,cdelmun,cciuloc,cestado;;
    private String idAdministrador,idMascota,nombres,apellidos,edad,correo,telefono,ocupacion,sexo,
            nMascotas, vivienda,calle, nCasa,colonia,delMun,ciuLoc,estado,fecha;
    private int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_dadopciones);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAdministrador= extras.getString("idAdministrador");
            idMascota= extras.getString("idMascota");

        }else{
            Toast.makeText(getApplicationContext(),"No se puede enviar solicictud por el momento", Toast.LENGTH_LONG).show();
            int value=2;
            Intent intent = new Intent(SolicitudDAdopciones.this, PaginaPrincipal.class);
            intent.putExtra("key",value);
            startActivity(intent);
            finish();
        }

        Calendar date= Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        fecha = formato.format(date.getTime());

        cnombres=findViewById(R.id.cNombres);
        capellidos=findViewById(R.id.cApellidos);
        radioGroup=findViewById(R.id.radioGroup);
        cedad=findViewById(R.id.cEdad);
        ccorreo=findViewById(R.id.cCorreo);
        ctelefono=findViewById(R.id.cTelefono);
        cocupacion=findViewById(R.id.cOcupacion);
        cmascotas=findViewById(R.id.cMascotas);
        ccalle=findViewById(R.id.cCalle);
        cncasa=findViewById(R.id.cNumero);
        ccolonia=findViewById(R.id.cColonia);
        cdelmun=findViewById(R.id.cDel_Mun);
        cciuloc=findViewById(R.id.cCiu_Loc);
        cestado=findViewById(R.id.cEstado);
        enviarSolicitud=findViewById(R.id.buttonSolicitud2);

        enviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombres=cnombres.getText().toString();
                apellidos=capellidos.getText().toString();
                edad=cedad.getText().toString();
                correo=ccorreo.getText().toString();
                telefono=ctelefono.getText().toString();

                if (TextUtils.isEmpty(telefono)){
                    telefono="0";
                }

                ocupacion=cocupacion.getText().toString();

                int id=radioGroup.getCheckedRadioButtonId();
                try {
                    RadioButton seleccionS=radioGroup.findViewById(id);
                    sexo=seleccionS.getText().toString();
                }catch (Exception exception){
                    sexo="Desconocido";
                }


                nMascotas=cmascotas.getText().toString();

                if (TextUtils.isEmpty(nMascotas)){
                    nMascotas="0";
                }

                int id2=radioGroup.getCheckedRadioButtonId();

                try {
                    RadioButton seleccionS=radioGroup.findViewById(id);
                    vivienda=seleccionS.getText().toString();
                }catch (Exception exception){

                    RadioButton seleccionS=radioGroup.findViewById(id);
                    seleccionS.setError("Ingresa datos de tu vivienda");
                    //Toast.makeText(Solicitud2.this, "Selecciona el tipo de vivienda".toString(), Toast.LENGTH_SHORT).show();
                }
                calle=ccalle.getText().toString();

                nCasa=cncasa.getText().toString();
                if (TextUtils.isEmpty(nCasa)){
                    nCasa="0";
                }
                colonia=ccolonia.getText().toString();
                if (TextUtils.isEmpty(colonia)){
                    colonia="Desconocido";
                }
                delMun=cdelmun.getText().toString();

                ciuLoc=cciuloc.getText().toString();

                estado=cestado.getText().toString();
                if (TextUtils.isEmpty(estado)){
                    estado="Desconocido";
                }


                //Checar que campos no esten vacios
                if(TextUtils.isEmpty(nombres) || TextUtils.isEmpty(apellidos) ||
                        TextUtils.isEmpty(edad) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(ocupacion)
                ||TextUtils.isEmpty(vivienda) || TextUtils.isEmpty(calle) || TextUtils.isEmpty(delMun) ||
                            TextUtils.isEmpty(ciuLoc)){
                    if(TextUtils.isEmpty(nombres)) cnombres.setError("Ingresa tu nombre");
                    if(TextUtils.isEmpty(apellidos)) capellidos.setError("Ingresa tus apellido");
                    if(TextUtils.isEmpty(edad)) cedad.setError("Ingresa tu edad");
                    if(TextUtils.isEmpty(correo)) ccorreo.setError("Ingresa tu correo");
                    if(TextUtils.isEmpty(ocupacion)) cocupacion.setError("Ingresa tu ocupacións");
                    if(TextUtils.isEmpty(calle)) ccalle.setError("Ingresa tu calle");
                    if(TextUtils.isEmpty(delMun)) cdelmun.setError("Ingresa tu delegación o municipio");
                    if(TextUtils.isEmpty(ciuLoc)) cciuloc.setError("Ingresa tu ciudad o localidad");
                    //Toast.makeText(SolicitudAdopcion.this, "Completa los campos obligatorios *", Toast.LENGTH_SHORT).show();

                }else{
                    enviarDatos();
                }

            }
        });
    }

    private void enviarDatos() {
        String url="https://paginaprueba.com/solicitudAdopcionED.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.contains("Solicitud de adopción enviada correctamente")) {
                    Toast.makeText(getApplicationContext(),"La solicitud fue enviada exitosamente, revise su bandeja de entrada del" +
                            " correo que ingresoó",Toast.LENGTH_SHORT).show();
                    int value=2;
                    Intent intent = new Intent(SolicitudDAdopciones.this, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    startActivity(intent);
                    finish();
                }else if(response.contains("Correo ya registrado")){
                    Toast.makeText(getApplicationContext(),"El correo que ingresó ya fue utilizado para enviar una " +
                            "solicitud. Utilice otro",Toast.LENGTH_SHORT).show();
                    ccorreo.setError("Ingresa otro correo");

                }else {
                    Toast.makeText(getApplicationContext(),"Ocurrió un error al enviar la solicitud. Intentelo más tarde",Toast.LENGTH_SHORT).show();
                    int value=2;
                    Intent intent = new Intent(SolicitudDAdopciones.this, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    startActivity(intent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"No se ha podido enviar la solicitud",Toast.LENGTH_SHORT).show();
                int value=2;
                Intent intent = new Intent(SolicitudDAdopciones.this, PaginaPrincipal.class);
                intent.putExtra("key",value);
                startActivity(intent);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parametros=new HashMap<>();
                parametros.put("idAdministrador",idAdministrador);
                parametros.put("idMascota",idMascota);
                parametros.put("nombres",nombres);
                parametros.put("apellidos",apellidos);
                parametros.put("edad",edad);
                parametros.put("correo",correo);
                parametros.put("telefono",telefono);
                parametros.put("ocupacion",ocupacion);
                parametros.put("sexo",sexo);
                parametros.put("nMascotas",nMascotas);
                parametros.put("vivienda",vivienda);
                parametros.put("calle",calle);
                parametros.put("numero",nCasa);
                parametros.put("colonia",colonia);
                parametros.put("deleMuni",delMun);
                parametros.put("ciuLoc",ciuLoc);
                parametros.put("estado",estado);
                parametros.put("fechaEnvio",fecha);

                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myVolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int value=2;
        Intent intent = new Intent(SolicitudDAdopciones.this, PaginaPrincipal.class);
        intent.putExtra("key",value);
        startActivity(intent);
        finish();
    }
}