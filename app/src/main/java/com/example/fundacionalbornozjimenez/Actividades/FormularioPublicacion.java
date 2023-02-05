package com.example.fundacionalbornozjimenez.Actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.FormularioPublicacionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormularioPublicacion extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView siguiente,nombreT,ciudadT,estadoT,edadT,raza_tipoT;
    private RadioGroup radioGroup;
    private Spinner spinnerTM,spinnerRR, spinnerNR,spinnerT,spinnerP;
    private TextView instrucciones1,nuevo;
    private String tipo="Refugio";

    private JSONArray result;
    private ArrayList<String> listaRR= new ArrayList<>();
    private ArrayList<FormularioPublicacionModel> listaRR2=new ArrayList<>();;
    private ArrayList<String> datos= new ArrayList<>();
    private EditText nombre,ciudad,estado,edad,raza_tipo;
    private String idRR,tipoA,nombreRR,nombreS,ciudadS,estadoS,edadS,raza_tipoS,sizeS,sexoS,personalidadS;
    public static Activity fp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_publicacion);
        fp=this;

        siguiente=findViewById(R.id.tSiguienteF);
        spinnerTM=findViewById(R.id.spinnerTipoA);
        spinnerRR=findViewById(R.id.spinnerRR);
        spinnerNR=findViewById(R.id.spinnerNombresRR);
        spinnerT=findViewById(R.id.spinnerSize);
        spinnerP=findViewById(R.id.spinnerPersonalidad);

        radioGroup=findViewById(R.id.radioGroupM);
        nuevo=findViewById(R.id.Nuevo);
        instrucciones1=findViewById(R.id.instruccionesSeleccionR);

        nombre=findViewById(R.id.cNombreM);
        ciudad=findViewById(R.id.cCiudadM);
        estado=findViewById(R.id.cEstadoM);
        edad=findViewById(R.id.cEdadM);
        raza_tipo=findViewById(R.id.cRazaM);

        nombreT=findViewById(R.id.tNombreFPM);
        ciudadT=findViewById(R.id.tCiudadFPM);
        estadoT=findViewById(R.id.tEstadoFPM);
        edadT=findViewById(R.id.tEdadFPM);
        raza_tipoT=findViewById(R.id.tRazaFPM);

        nombreT.setVisibility(View.GONE);
        ciudadT.setVisibility(View.GONE);
        estadoT.setVisibility(View.GONE);
        edadT.setVisibility(View.GONE);
        raza_tipoT.setVisibility(View.GONE);

        nombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    nombreT.setVisibility(View.VISIBLE);
                }
                else {
                    nombreT.setVisibility(View.GONE);
                }
            }
        });


        ciudad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ciudadT.setVisibility(View.VISIBLE);
                }
                else {
                    ciudadT.setVisibility(View.GONE);
                }
            }
        });

        estado.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    estadoT.setVisibility(View.VISIBLE);
                }
                else {
                    estadoT.setVisibility(View.GONE);
                }
            }
        });


        edad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edadT.setVisibility(View.VISIBLE);
                }
                else {
                    edadT.setVisibility(View.GONE);
                }
            }
        });

        raza_tipo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    raza_tipoT.setVisibility(View.VISIBLE);
                }
                else {
                    raza_tipoT.setVisibility(View.GONE);
                }
            }
        });



        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "abriendo formulario", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FormularioPublicacion.this, RegistroRefugio.class);
                startActivity(intent);

            }
        });


        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id=radioGroup.getCheckedRadioButtonId();
                try {
                    RadioButton seleccionS=radioGroup.findViewById(id);
                    sexoS=seleccionS.getText().toString();
                }catch (Exception exception){
                    exception.printStackTrace();
                    /*Toast.makeText(FormularioPublicacion.this, exception.getMessage().toString(),
//                            Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(FormularioPublicacion.this, "Selecciona el sexo de la mascota",
                            Toast.LENGTH_SHORT).show();
                }

                nombreS=nombre.getText().toString();
                ciudadS=ciudad.getText().toString();
                //Toast.makeText(FormularioPublicacion.this, "ciudad: "+ciudadS,Toast.LENGTH_SHORT).show();
                estadoS=estado.getText().toString();
                edadS=edad.getText().toString();
                raza_tipoS=raza_tipo.getText().toString();


                if (TextUtils.isEmpty(nombreS) || TextUtils.isEmpty(ciudadS) ||
                        TextUtils.isEmpty(edadS) || TextUtils.isEmpty(sizeS) || TextUtils.isEmpty(sexoS)){
                    if(TextUtils.isEmpty(nombreS)){
                        nombre.setError("Ingresa el nombre de la mascota");
                    }
                    if(TextUtils.isEmpty(ciudadS)){
                        ciudad.setError("Ingresa la ciudad en la que se encuentra la mascota");
                    }
                    if(TextUtils.isEmpty(edadS)){
                        edad.setError("Ingresa la edad aproximada de la  mascota");
                    }

                }else{
                    datos.add(idRR);
                    datos.add(tipoA);
                    datos.add(nombreS);
                    datos.add(ciudadS);
                    datos.add(estadoS);
                    datos.add(edadS);
                    datos.add((raza_tipoS));
                    datos.add(sizeS);
                    datos.add(sexoS);
                    datos.add(personalidadS);
                    Intent intent = new Intent(FormularioPublicacion.this, FormularioPub2.class);
                    intent.putExtra("datos", datos);
                    startActivity(intent);
                }
            }

        });


        spinnerTM.setPrompt("Elige tipo de animal");
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.tipoAnimal,
                R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerTM.setAdapter(adapter);
        spinnerTM.setOnItemSelectedListener(this);

        spinnerRR.setPrompt("Elige si es regufio o rescatista");
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.refugioORescatista,
                R.layout.spinner_text);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerRR.setAdapter(adapter2);
        spinnerRR.setOnItemSelectedListener(this);

        spinnerNR.setOnItemSelectedListener(this);

        spinnerT.setPrompt("Elige el tamaño de la mascota");
        ArrayAdapter<CharSequence> adapter4=ArrayAdapter.createFromResource(this,R.array.Size,
                R.layout.spinner_text);
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerT.setAdapter(adapter4);
        spinnerT.setOnItemSelectedListener(this);

        spinnerP.setPrompt("Elige la personaldiad de la mascota");
        ArrayAdapter<CharSequence> adapter5=ArrayAdapter.createFromResource(this,R.array.Personalidad,
                R.layout.spinner_text);
        adapter5.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerP.setAdapter(adapter5);
        spinnerP.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
            case R.id.spinnerTipoA:
                tipoA = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.spinnerRR:
                String seleccion2 = adapterView.getItemAtPosition(i).toString();

                if(seleccion2.equals("Refugio")){
                    instrucciones1.setText("Escoge el refugio");
                    tipo="Refugio";
                    listaRR.clear();
                    listaRR2.clear();
                    recibirDatosRR();

                }else if(seleccion2.equals("Rescatista")){
                    instrucciones1.setText("Escoge al rescatista");
                    tipo="Rescatista";
                    listaRR.clear();
                    listaRR2.clear();
                    recibirDatosRR();
                }
                break;
            case R.id.spinnerNombresRR:
                nombreRR = adapterView.getItemAtPosition(i).toString();
                idRR=listaRR2.get(i).getId();
                break;
            case R.id.spinnerSize:
                sizeS= adapterView.getItemAtPosition(i).toString();
                break;

            case R.id.spinnerPersonalidad:
                personalidadS = adapterView.getItemAtPosition(i).toString();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    private void recibirDatosRR(){
        String url="https://paginaprueba.com/refugiosConsulta.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");
                            datosRR(result);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"No se puede registrar a la mascota" +
                                    "por el momento. Intentelo más tarde",Toast.LENGTH_SHORT).show();
                            int value=3;
                            Intent intent = new Intent(FormularioPublicacion.this, PaginaPrincipal.class);
                            intent.putExtra("key",value);
                            startActivity(intent);
                            finish();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"No se puede registrar a la mascota" +
                                "por el momento. Intentelo más tarde",Toast.LENGTH_SHORT).show();
                        int value=3;
                        Intent intent = new Intent(FormularioPublicacion.this, PaginaPrincipal.class);
                        intent.putExtra("key",value);
                        startActivity(intent);
                        finish();
                        error.printStackTrace();
                    }
                }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                parametros.put("tipo",tipo);

                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void datosRR(JSONArray jsonArray){
        for(int i=0;i<jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                listaRR.add(jsonObject.getString("alias"));
                listaRR2.add(new FormularioPublicacionModel(jsonObject.getString("alias"), jsonObject.getString("id_ref_res")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //spinnerNR.setAdapter(new ArrayAdapter<String>(FormularioPublicacion.this, R.layout.spinner_text, students));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_text, listaRR);
        spinnerNR.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int value=3;
        Intent intent = new Intent(FormularioPublicacion.this, PaginaPrincipal.class);
        intent.putExtra("key",value);
        startActivity(intent);
        FormularioPublicacion.fp.finish();
        finish();
    }
}