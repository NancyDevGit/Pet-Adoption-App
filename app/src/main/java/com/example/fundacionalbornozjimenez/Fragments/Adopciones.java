package com.example.fundacionalbornozjimenez.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fundacionalbornozjimenez.Actividades.PaginaPrincipal;
import com.example.fundacionalbornozjimenez.Actividades.SolicitudDAdopciones;
import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.AdapterDatosAdopciones;
import com.example.fundacionalbornozjimenez.model.PublicacionesModel;
import com.example.fundacionalbornozjimenez.model.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Adopciones extends Fragment implements RecyclerViewInterface, SwipeRefreshLayout.OnRefreshListener {
    private Button enviarS,modificar;
    private ImageView regresar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<PublicacionesModel> listaPublicaciones;
    private ArrayList<String>idMascotas=new ArrayList<>();
    private ArrayList<String>idAdministrador=new ArrayList<>();
    private RecyclerView recycler;
    private String mostrar;
    private RequestQueue requestQueue;
    private AlertDialog dialog;

    public Adopciones() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String mostrar = getArguments().getString("mostrar");
        this.mostrar=mostrar;
        View view=inflater.inflate(R.layout.fragment_adopciones, container, false);

        PaginaPrincipal.bAdopciones.setBackgroundColor(PaginaPrincipal.bAdopciones.
                getContext().getResources().getColor(R.color.albornoz));

        PaginaPrincipal.bFundacion.setBackgroundColor(PaginaPrincipal.bAdopciones.
                getContext().getResources().getColor(R.color.cBotonSoft));


        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeAdopciones);

        listaPublicaciones=new ArrayList<>();
        recycler= view.findViewById(R.id.recylcerID);
        recycler.setLayoutManager(new GridLayoutManager(view.getContext(),2));

        int spanCount = 2; // 3 columns
        int spacing = 40; // 50px
        boolean includeEdge = false;
        recycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                listaPublicaciones=new ArrayList<>();

                llenarPublicacion(Adopciones.this);

                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        return view;

    }


    private void llenarPublicacion(RecyclerViewInterface recyclerViewInterface) {

        String URL="https://paginaprueba.com/posts.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject publicacion = array.getJSONObject(i);
                        String edad="años";
                        String raza_tipo;

                        idMascotas.add(publicacion.getString("idMascota"));
                        idAdministrador.add(publicacion.getString("idAdministrador"));


                        if (publicacion.getString("edad").equals("1")){
                            edad=publicacion.getString("edad")+" año";
                        }else{
                            edad=publicacion.getString("edad")+" años";
                        }

                        if (publicacion.getString("razaTipo").equals("Desconocido")){
                            raza_tipo="Raza desconocida";
                        }else{
                            raza_tipo=publicacion.getString("razaTipo");
                        }

                        if (publicacion.getString("sexo").equals("Macho")){
                            listaPublicaciones.add(new PublicacionesModel(publicacion.getString("fotoMascota"), R.drawable.ubicacion,
                                    R.drawable.macho, publicacion.getString("nombres"),
                                    publicacion.getString("ciudadLocalidad"), edad,
                                    publicacion.getString("personalidad"),
                                    raza_tipo,"Más información"));
                        }else if(publicacion.getString("sexo").equals("Hembra")){
                            listaPublicaciones.add(new PublicacionesModel(publicacion.getString("fotoMascota"), R.drawable.ubicacion,
                                    R.drawable.hembra, publicacion.getString("nombres"),
                                    publicacion.getString("ciudadLocalidad"), edad,
                                    publicacion.getString("personalidad"),
                                    raza_tipo,"Más información"));
                        }

                    }

                    AdapterDatosAdopciones adapter = new AdapterDatosAdopciones(listaPublicaciones, recyclerViewInterface,getContext());
                    recycler.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al cargar publicaciones", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error al cargar publicaciones", Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                parametros.put("idMascota","");
                parametros.put("correo","");
                parametros.put("tipo","publicacionesAdopciones");

                return parametros;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
    private void mostrarDialogA(int posicion){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        final View customLayout= getLayoutInflater().inflate(R.layout.dialog_publicacion, null);
        builder.setView(customLayout);

        llenarDialog(customLayout,posicion);

        dialog = builder.create();
        dialog.show();
        regresar=(ImageView) customLayout.findViewById(R.id.regresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        visibilidadE(customLayout,dialog);

        modificar.setVisibility(View.GONE);


    }

    private void mostrarDialogV(int posicion){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());


        final View customLayout= getLayoutInflater().inflate(R.layout.dialog_publicacion, null);
        builder.setView(customLayout);
        llenarDialog(customLayout,posicion);



        dialog = builder.create();
        dialog.show();
        regresar=(ImageView) customLayout.findViewById(R.id.regresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        visibilidadE(customLayout,dialog);

        enviarS.setVisibility(View.VISIBLE);

        enviarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idAd=idAdministrador.get(posicion);
                String idMas=idMascotas.get(posicion);
                Intent intent = new Intent(getContext(), SolicitudDAdopciones.class);
                intent.putExtra("idAdministrador",idAd);
                intent.putExtra("idMascota",idMas);
                startActivity(intent);
                dialog.dismiss();

            }
        });
    }


    private void llenarDialog(View v, int posicion){
        TextView nombre,ciudad,edad,size,personalidad,ciudad2,sexo,estado,raza,esterilizado,vacunado,
                desparacitado,enfermedades,amigableSN,extra;
        ImageView sex,fotoMascota;

        nombre=(TextView) v.findViewById(R.id.tNombreM);
        ciudad=(TextView) v.findViewById(R.id.tUbicacionM);
        edad=(TextView) v.findViewById(R.id.tEdadMD);
        size=(TextView) v.findViewById(R.id.tSizeD);
        personalidad=(TextView) v.findViewById(R.id.tPersonalidadD);
        ciudad2=(TextView) v.findViewById(R.id.tCiudadD);
        sexo=(TextView) v.findViewById(R.id.tSexoD);
        estado=(TextView) v.findViewById(R.id.tEstadoD);
        raza=(TextView) v.findViewById(R.id.tRazaD);
        esterilizado=(TextView) v.findViewById(R.id.tEsterilizacionD);
        vacunado=(TextView) v.findViewById(R.id.tVacunacionD);
        desparacitado=(TextView) v.findViewById(R.id.tDesparacitadoD);
        enfermedades=(TextView) v.findViewById(R.id.tEnfermedadesD);
        amigableSN=(TextView) v.findViewById(R.id.tAmigableSN);
        extra=(TextView) v.findViewById(R.id.tExtra);
        sex=(ImageView) v.findViewById(R.id.mSex);
        fotoMascota=(ImageView) v.findViewById(R.id.fotoMascota);


        String URL="https://paginaprueba.com/posts.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject publicacion = array.getJSONObject(i);

                        nombre.setText(publicacion.getString("nombres"));;
                        ciudad.setText(publicacion.getString("ciudadLocalidad"));
                        edad.setText(publicacion.getString("edad"));
                        size.setText(publicacion.getString("size"));
                        personalidad.setText(publicacion.getString("personalidad"));
                        ciudad2.setText(publicacion.getString("ciudadLocalidad"));
                        sexo.setText(publicacion.getString("sexo"));
                        estado.setText(publicacion.getString("estado"));
                        raza.setText(publicacion.getString("razaTipo"));
                        esterilizado.setText(publicacion.getString("esterilizacion"));
                        vacunado.setText(publicacion.getString("vacunacion"));
                        desparacitado.setText(publicacion.getString("desparacitacion"));
                        enfermedades.setText(publicacion.getString("enfermedades"));

                        if (publicacion.getString("amigableP").equals("Si") &&
                                publicacion.getString("amigableK").equals("Si")){
                            amigableSN.setText("Es amigable con niños y con otras mascotas");

                        }else if(publicacion.getString("amigableP").equals("No") &&
                                publicacion.getString("amigableK").equals("No")){
                            amigableSN.setText("No es amigable con niños ni con otras mascotas");
                        }else if(publicacion.getString("amigableP").equals("No") &&
                                publicacion.getString("amigableK").equals("Si")){
                            amigableSN.setText("Es migable con niños pero NO con otras mascotas");
                        }else if(publicacion.getString("amigableP").equals("Si") &&
                                publicacion.getString("amigableK").equals("No")){
                            amigableSN.setText("Es migable con otras mascotas pero NO con niños");
                        }

                            extra.setText(publicacion.getString("extra"));
                        if(publicacion.getString("extra").equals("null")){
                            extra.setVisibility(v.GONE);
                        }

                        if (publicacion.getString("sexo").equals("Macho")){
                            sex.setImageResource(R.drawable.macho);
                        }else if(publicacion.getString("sexo").equals("Hembra")){
                            sex.setImageResource(R.drawable.hembra);
                        }

                        if (publicacion.getString("fotoMascota")!=null){
                            cargarImagen(fotoMascota,publicacion.getString("fotoMascota"));



                        }else{
                            fotoMascota.setImageResource(R.drawable.rcono);
                        }

                    }

                } catch (JSONException e) {
                    //e.printStackTrace();
                    Toast.makeText(getContext(),"Error, no se puedieron cargar datos de la mascota", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(),"Error, no se puedieron cargar datos de la mascota",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                parametros.put("correo","");
                parametros.put("idMascota",idMascotas.get(posicion));
                parametros.put("tipo","dialogo");

                return parametros;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }

    private void cargarImagen(ImageView fotoMascota,String fotoS) {
        requestQueue= Volley.newRequestQueue(getContext());
        ImageRequest imageRequest =new ImageRequest(fotoS, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                fotoMascota.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        try {
            requestQueue.add(imageRequest);
        }catch (Exception e){
            fotoMascota.setImageResource(R.drawable.macho);
        }
    }

    private void visibilidadE(View v,AlertDialog dialog){
        enviarS=(Button)v.findViewById(R.id.bSolicitud);
        modificar=(Button)v.findViewById(R.id.bModificar);


        //Visibilidad de elementos
        enviarS.setVisibility(View.GONE);
        modificar.setVisibility(View.GONE);
    }


    @Override
    public void onclickElemento(int position) {

        if (mostrar.equals("admin")){
            mostrarDialogA(position);
        }else if(mostrar.equals("visitante")){
            mostrarDialogV(position);
        }

    }

    @Override
    public void onclickElemento(int position, View view) {

    }


    @Override
    public void onRefresh() {
        listaPublicaciones=new ArrayList<>();
        llenarPublicacion(Adopciones.this);
        new Handler().postDelayed(new Runnable() {

            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }

}