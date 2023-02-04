package com.example.fundacionalbornozjimenez.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.AdapterPublicacionesSol;
import com.example.fundacionalbornozjimenez.model.AdministradorPreferencias;
import com.example.fundacionalbornozjimenez.model.PublicacionesModel;
import com.example.fundacionalbornozjimenez.model.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PublicacionesConSolicitudes extends Fragment implements RecyclerViewInterface,SwipeRefreshLayout.OnRefreshListener {
        private SwipeRefreshLayout swipeRefreshLayout;
        private ImageView regresar;
        private ArrayList<PublicacionesModel> listaPubSol;
        private RecyclerView recyclerView;
        private String correo;

        private SolicitudesRecibidas solicitudesRecibidas= new SolicitudesRecibidas();
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.fragment_publicaciones_con_solicitudes, container, false);

            swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipePublicacionesCS);

            correo= new AdministradorPreferencias(getContext()).correo();

                      regresar=view.findViewById(R.id.Imgback1);

            regresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    regresar();

                }
            });


            listaPubSol=new ArrayList<>();
            recyclerView=view.findViewById(R.id.recylcerPAS);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            AdapterPublicacionesSol adapter=new AdapterPublicacionesSol(listaPubSol,this);
            recyclerView.setAdapter(adapter);

            int spanCount = 2; // 3 columns
            int spacing = 40; // 50px
            boolean includeEdge = false;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    listaPubSol=new ArrayList<>();

                    llenarPublicaciones(PublicacionesConSolicitudes.this);

                    new Handler().postDelayed(new Runnable() {

                        @Override public void run() {

                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                }
            });

            return view;
        }




        private void llenarPublicaciones(RecyclerViewInterface recyclerViewInterface) {

            String URL="https://paginaprueba.com/publicaciones.php";

            StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.contains("[]")){
                        Toast.makeText(getContext(),"No hay solicitudes por revisar", Toast.LENGTH_LONG).show();
                        regresar();
                    }else{
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject publicacion = array.getJSONObject(i);
                                String edad;


                                if (publicacion.getString("edad").equals("1")){
                                    edad=publicacion.getString("edad")+" año";
                                }else{
                                    edad=publicacion.getString("edad")+" años";
                                }


                                if (publicacion.getString("sexo").equals("Macho")){
                                    listaPubSol.add(new PublicacionesModel(publicacion.getString("estadoSol"),
                                            publicacion.getString("idMascota"),
                                            publicacion.getString("nombres"),
                                            R.drawable.macho, R.drawable.ubicacion, publicacion.getString("ciudadLocalidad"),
                                            edad, publicacion.getString("personalidad"),
                                            publicacion.getString("razaTipo")));
                                }else if(publicacion.getString("sexo").equals("Hembra")){
                                    listaPubSol.add(new PublicacionesModel(publicacion.getString("estadoSol"),
                                            publicacion.getString("idMascota"),
                                            publicacion.getString("nombres"),
                                            R.drawable.hembra, R.drawable.ubicacion, publicacion.getString("ciudadLocalidad"),
                                            edad, publicacion.getString("personalidad"),
                                            publicacion.getString("razaTipo")));
                                }

                            }

                            AdapterPublicacionesSol adapter = new AdapterPublicacionesSol(listaPubSol, recyclerViewInterface);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error al mostrar publicaciones. No se pudieron cargar", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),"No se pudieron cargar las publicaciones", Toast.LENGTH_LONG).show();
                }}){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros=new HashMap<>();
                    parametros.put("idMascota","");
                    parametros.put("correo",correo);
                    parametros.put("tipo","publicacionConSol");

                    return parametros;
                }
            };

            Volley.newRequestQueue(getContext()).add(stringRequest);
    }



        private void regresar (){
            //Para regresar a fragmento anterior
            FragmentManager fm=getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }


        @Override
        public void onclickElemento(int position) {

            Bundle bundle = new Bundle();
            bundle.putString("key", listaPubSol.get(position).getIdMascota());
            solicitudesRecibidas.setArguments(bundle);
            FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameContainer,solicitudesRecibidas,null).addToBackStack(null).commit();

        }

        @Override
        public void onclickElemento(int position, View view) {
        }


    @NonNull
        @Override
        public CreationExtras getDefaultViewModelCreationExtras() {
            return super.getDefaultViewModelCreationExtras();
        }



    @Override
    public void onRefresh() {
        listaPubSol=new ArrayList<>();
        llenarPublicaciones(PublicacionesConSolicitudes.this);
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }
}