package com.example.fundacionalbornozjimenez.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;
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
import com.example.fundacionalbornozjimenez.Actividades.FormularioPublicacion;
import com.example.fundacionalbornozjimenez.Actividades.PaginaPrincipal;
import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.AdapterDatosAdopciones;
import com.example.fundacionalbornozjimenez.model.AdministradorPreferencias;
import com.example.fundacionalbornozjimenez.model.PublicacionesModel;
import com.example.fundacionalbornozjimenez.model.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PublicacionesAdmin extends Fragment implements RecyclerViewInterface, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<PublicacionesModel> listaPublicaciones=null;
    private ArrayList<String>idMascotas=new ArrayList<>();
    private ImageView solRecibidas,regresar;
    private Button eliminar,adoptar;

    private TextView textPublicar;
    private RequestQueue requestQueue;
    private String correo;
    private AlertDialog dialog;

    private PublicacionesConSolicitudes publicacionesConSolicitudes= new PublicacionesConSolicitudes();

    public PublicacionesAdmin() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_publicaciones_admin, container, false);

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipePublicaciones);

        solRecibidas=(ImageView) view.findViewById(R.id.imgSolRecibidas);
        textPublicar=(TextView) view.findViewById(R.id.tPublicar);

        correo= new AdministradorPreferencias(getContext()).correo();

        //Ver solicitudes recibidas
        solRecibidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFragmento(publicacionesConSolicitudes,view);

                }
            });

            //Publicar mascotas
       textPublicar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(getContext(), FormularioPublicacion.class);
                    startActivity(intent);
                    ((Activity)getContext()).finish();

                }
            });


            //Cargar recyclerview de publicaciones hechas por admin
            listaPublicaciones=new ArrayList<>();
            recyclerView=view.findViewById(R.id.recylcerPA);
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));

            int spanCount = 2; // 3 columns
            int spacing = 40; // 50px
            boolean includeEdge = false;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);

                    llenarPublicacion(PublicacionesAdmin.this);

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
            String URL="https://paginaprueba.com/publicaciones.php";

            StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Toast.makeText(getContext(),response, Toast.LENGTH_LONG).show();
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject publicacion = array.getJSONObject(i);
                            String edad="años";
                            String raza_tipo;

                            idMascotas.add(publicacion.getString("idMascota"));

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
                                listaPublicaciones.add(new PublicacionesModel(publicacion.getString("fotoMascota"),
                                        R.drawable.ubicacion,
                                        R.drawable.macho, publicacion.getString("nombres"),
                                        publicacion.getString("ciudadLocalidad"), edad,
                                        publicacion.getString("personalidad"), raza_tipo,
                                        "Ver todos los datos"));
                            }else if(publicacion.getString("sexo").equals("Hembra")){
                                listaPublicaciones.add(new PublicacionesModel(publicacion.getString("fotoMascota"), R.drawable.ubicacion,
                                        R.drawable.hembra, publicacion.getString("nombres"),
                                        publicacion.getString("ciudadLocalidad"), edad,
                                        publicacion.getString("personalidad"), raza_tipo,
                                        "Ver todos los datos"));
                            }
                        }

                        AdapterDatosAdopciones adapter = new AdapterDatosAdopciones(listaPublicaciones, recyclerViewInterface,getContext());
                        recyclerView.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),"No se pudieron cargar las publicaciones", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), (CharSequence) e, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"No se pudieron cargar las publicaciones", Toast.LENGTH_LONG).show();
                }}){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros=new HashMap<>();
                    parametros.put("idMascota","");
                    parametros.put("correo",correo);
                    parametros.put("tipo","normal");

                    return parametros;
                }
            };

            Volley.newRequestQueue(getContext()).add(stringRequest);
        }


        public void cargarFragmento(Fragment fragment, View view){

            FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameContainer,fragment,null).addToBackStack(null).commit();
        }


        @Override
        public void onclickElemento(int position) {
            mostrarDialogA(position);
        }

        @Override
        public void onclickElemento(int position, View view) {

        }


        private void mostrarDialogA(int position) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            final View customLayout= getLayoutInflater().inflate(R.layout.dialog_publicacion, null);
            builder.setView(customLayout);

            llenarDialog(customLayout,position);

            dialog = builder.create();
            dialog.show();

            //visibilidad de botones
            visibilidadE(customLayout,dialog);

            regresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmar(position);
                    dialog.dismiss();

                }
            });

        }

        private void confirmar(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Confirmar eliminación");
            builder.setMessage("¿Estas seguro que deseas eliminar publicación?");

            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    eliminarPublicacion(position);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }

        private void confirmarEconSol(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("La publicación seleccionada tiene solicitudes de adopción pendientes");
            builder.setMessage("¿Estas seguro que deseas eliminar publicación?");

            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    eliminarPublicacion2(position);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }

        private void eliminarPublicacion2(int position) {
            String URL="https://paginaprueba.com/eliminarRegistroF.php";

            StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(),response, Toast.LENGTH_SHORT).show();
                    if(response.contains("Eliminado correctamente")){
                        onRefresh();
                    }else{
                        Toast.makeText(getContext(),"Ocurrio un error, intentelo más tarde", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),"No se ha podido eliminar la publicación. Intentalo más tarde",Toast.LENGTH_SHORT).show();
                }
            }){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros=new HashMap<>();
                    parametros.put("idMascota",idMascotas.get(position));
                    parametros.put("tipo","eliminar2");
                    return parametros;
                }
            };
            Volley.newRequestQueue(getContext()).add(stringRequest);

        }


        private void eliminarPublicacion(int position) {

        String URL="https://paginaprueba.com/eliminarRegistroF.php";

            StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.contains("Tiene solicitudes")){
                        Toast.makeText(getContext(),"La publicación tiene solicitudes de adopción,no se puede eliminar", Toast.LENGTH_LONG).show();
                    }else if(response.contains("Eliminado correctamente")){
                        Toast.makeText(getContext(),"Publicación eliminada correctamente", Toast.LENGTH_LONG).show();
                        int value=3;
                        Intent intent = new Intent(getContext(), PaginaPrincipal.class);
                        intent.putExtra("key",value);
                        startActivity(intent);
                        FormularioPublicacion.fp.finish();
                    }else{
                        Toast.makeText(getContext(),"Ocurrio un error, intentelo más tarde", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),"No se ha podido eliminar la publicación. Intentalo más tarde",Toast.LENGTH_SHORT).show();
                }
            }){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros=new HashMap<>();
                    parametros.put("idMascota",idMascotas.get(position));
                    parametros.put("tipo","eliminar1");
                    return parametros;
                }
            };
            Volley.newRequestQueue(getContext()).add(stringRequest);

        }

    private void cambiar() {
    }

    private void visibilidadE(View view, AlertDialog dialog) {
            eliminar=(Button) view.findViewById(R.id.bModificar);
            adoptar=(Button) view.findViewById(R.id.bSolicitud);
            adoptar.setVisibility(View.GONE);
            regresar=(ImageView) view.findViewById(R.id.regresar);
            eliminar.setText("Eliminar publicación");
        }

        private void llenarDialog(View v, int position) {
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



            String URL="https://paginaprueba.com/publicaciones.php";

            StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject publicacion = array.getJSONObject(i);

                            nombre.setText(publicacion.getString("nombres"));
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
                        e.printStackTrace();
                        Toast.makeText(getContext(),"Error, no se puedieron cargar datos de la mascota",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getContext(),"Error, no se pudieron cargar los datos de la mascota",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros=new HashMap<>();
                    parametros.put("idMascota",idMascotas.get(position));
                    parametros.put("correo",correo);
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
                fotoMascota.setImageResource(R.drawable.rcono);

            }
        });
        try {
            requestQueue.add(imageRequest);
        }catch (Exception e){
            e.printStackTrace();
            fotoMascota.setImageResource(R.drawable.macho);
        }
    }


        @NonNull
        @Override
        public CreationExtras getDefaultViewModelCreationExtras() {
            return super.getDefaultViewModelCreationExtras();
        }

        @Override
        public void onRefresh() {
            listaPublicaciones=new ArrayList<>();

            llenarPublicacion(PublicacionesAdmin.this);
            swipeRefreshLayout.setOnRefreshListener(this);

            new Handler().postDelayed(new Runnable() {

                @Override public void run() {

                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);

        }


}
