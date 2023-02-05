package com.example.fundacionalbornozjimenez.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fundacionalbornozjimenez.Actividades.PaginaPrincipal;
import com.example.fundacionalbornozjimenez.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValidacionUsuario {
    private Context context;
    private View view;
    private String u;
    private String p;
    private ArrayList<String> value=null;


    public ValidacionUsuario(Context context,String usuario,String pss){
        this.context=context;
        this.u=usuario;
        this.p=pss;
        intentarInicioMA();
    }


    //Inicio formulario
    public ValidacionUsuario(Context context,View view,String usuario,String pss){
        this.context=context;
        this.view=view;
        this.u=usuario;
        this.p=pss;
        intentarInicioIF(view);
    }

    public ValidacionUsuario(Context context, byte[] usuario, byte[] pss){
        this.context=context;

        this.u=new EncriptarDecriptar().bytesToHex(usuario);
        this.p=new EncriptarDecriptar().bytesToHex(pss);
        intentarInicioMA();
    }

    public ValidacionUsuario(Context context, View view, byte[] usuario, byte[] pss){
        this.context=context;
        this.view=view;

        this.u=new EncriptarDecriptar().bytesToHex(usuario);
        this.p=new EncriptarDecriptar().bytesToHex(pss);

        intentarInicioIF(view);

    }


    private void intentarInicioIF(View view){

        String URL="https://paginaprueba.com/inicioSesionED.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener <String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Iniciando sesión")){

                    try {
                        guardarDatos(u,p);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    Toast.makeText(context,"Sesión iniciada",Toast.LENGTH_SHORT).show();

                    int value=1;
                    Intent intent = new Intent(context, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    context.startActivity(intent);
                    ((Activity)context).finish();


                }else{
                    EditText cont;
                    cont=(EditText) view.findViewById(R.id.pass);
                    cont.setError("Constraseña  incorrecta");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                if (error==null){
                    Toast.makeText(context,"Error de inicio de sesión",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"Error de inicio de sesión",Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                }

                int value=2;
                Intent intent = new Intent(context, PaginaPrincipal.class);
                intent.putExtra("key",value);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> parametros= new HashMap<String, String>();
                parametros.put("correo",u);
                parametros.put("ppss",p);

                return parametros;
            }
        };


        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



    private void intentarInicioMA(){

        String URL="https://paginaprueba.com/inicioSesionED.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener <String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Iniciando sesión")){

                    try {
                        guardarDatos(u,p);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    int value=1;
                    Intent intent = new Intent(context, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    context.startActivity(intent);
                    ((Activity)context).finish();



                }else{
                    int value=2;
                    Intent intent = new Intent(context, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    context.startActivity(intent);
                    ((Activity)context).finish();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error==null){
                    Toast.makeText(context,"Error de inicio de sesión",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,"Error de inicio de sesión",Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
                int value=2;
                Intent intent = new Intent(context, PaginaPrincipal.class);
                intent.putExtra("key",value);
                context.startActivity(intent);
                ((Activity)context).finish();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> parametros= new HashMap<String, String>();
                parametros.put("correo",u);
                parametros.put("ppss",p);

                return parametros;
            }
        };


        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void guardarDatos( String u, String p) throws Exception {
        new AdministradorPreferencias(context).sharedEncrypted(u,p);
    }


}
