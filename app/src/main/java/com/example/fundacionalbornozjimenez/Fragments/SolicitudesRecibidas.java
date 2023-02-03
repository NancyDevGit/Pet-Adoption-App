package com.example.fundacionalbornozjimenez.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentManager;
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
import com.example.fundacionalbornozjimenez.model.AdapterSolicitudes;
import com.example.fundacionalbornozjimenez.model.RecyclerViewInterface;
import com.example.fundacionalbornozjimenez.model.SolicitudesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SolicitudesRecibidas extends Fragment implements RecyclerViewInterface,SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView regresar;
    private ArrayList<SolicitudesModel> listaSolicitudes;
    private RecyclerView recycler;
    private ImageView back;
    private String tipo,idMas,correoH;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_solicitudes_recibidas, container, false);

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeSolicitudes);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String i = bundle.getString("key");
            idMas=i;
            solicitudesMascotas(view);
        }else {
            Toast.makeText(getContext(),"No se pueden cargar las solicitudes por el momento", Toast.LENGTH_LONG).show();
           regresar();
        }

        regresar=view.findViewById(R.id.Imgback);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar();
            }
        });

        return view;
    }


    private void solicitudesMascotas(View view) {
        tipo="solXmascota";
        listaSolicitudes=new ArrayList<>();
        recycler=view.findViewById(R.id.recylcerSR);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        AdapterSolicitudes adapter=new AdapterSolicitudes(listaSolicitudes,this);
        recycler.setAdapter(adapter);

        int spanCount = 1; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        recycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                listaSolicitudes=new ArrayList<>();

                llenarSolicitudes(SolicitudesRecibidas.this);

                new Handler().postDelayed(new Runnable() {

                    @Override public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }

        });
    }


    private void llenarSolicitudes(RecyclerViewInterface recyclerViewInterface) {

        String URL="https://fundacionalbornoz.com/solicitudesRecibidas.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    int cont=0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject solicitud = array.getJSONObject(i);
                        if (solicitud.getString("estadoSol").equals("Pendiente")){
                            cont++;
                        }
                    }
                    if (cont==0){
                        regresar();
                    }


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject solicitud = array.getJSONObject(i);
                        String edad="años";

                        if (solicitud.getString("edad").equals("1")){
                            edad=solicitud.getString("edad")+" año";
                        }else{
                            edad=solicitud.getString("edad")+" años";
                        }

                        listaSolicitudes.add(new SolicitudesModel(solicitud.getString("idSolicitante"),
                                solicitud.getString("idSolicitud"),solicitud.getString("fecha"),
                                solicitud.getString("estadoSol"),
                                solicitud.getString("nombres"),solicitud.getString("apellidos"),
                                solicitud.getString("sexo"),edad,solicitud.getString("ocupacion"),
                                solicitud.getString("vivienda"), solicitud.getString("mascotas"),
                                solicitud.getString("ciudad"), solicitud.getString("dele_mun"),
                                solicitud.getString("estado")));


                    }

                    AdapterSolicitudes adapter = new AdapterSolicitudes(listaSolicitudes, recyclerViewInterface);
                    recycler.setAdapter(adapter);

                } catch (JSONException e) {
                    Toast.makeText(getContext(),"No hay solicitudes por mostrar", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    //regresar();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(),"Error al cargar las solicitudes recibidas", Toast.LENGTH_LONG).show();
            }}){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                parametros.put("tipo",tipo);
                parametros.put("idMascota",idMas);
                parametros.put("idSolicitud","");
                parametros.put("correo","");
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



    private void mostrarDialogo(int position){
        Button aceptar,rechazar;

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        final View customLayout= getLayoutInflater().inflate(R.layout.dialog_solicitud_recibida, null);
        builder.setView(customLayout);

        llenarDialogo(customLayout,position);

        AlertDialog dialog = builder.create();
        dialog.show();
        back= customLayout.findViewById(R.id.regresarDS);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        aceptar=customLayout.findViewById(R.id.botonAceptarD);
        rechazar=customLayout.findViewById(R.id.botonRechazarD);
        String estado=listaSolicitudes.get(position).getEstadoSol();
        if (!estado.equals("Pendiente")){
            aceptar.setVisibility(View.GONE);
            rechazar.setVisibility(View.GONE);

        }else{
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean c;
                    c=true;
                    confirmar(position,c);
                    dialog.dismiss();

                }
            });


            rechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean c;
                    c=false;
                    confirmar(position,c);
                    dialog.dismiss();
                }
            });

        }
    }

    private void llenarDialogo(View v, int position) {

        TextView nombres,sexo,edad,ocupacion,vivienda,mascotas,del_mun,ciudad,estado,fecha,estadoSol;
        nombres=(TextView) v.findViewById(R.id.tNombreS2D);
        sexo=(TextView) v.findViewById(R.id.tSexoS2D);
        edad=(TextView) v.findViewById(R.id.tEdadS2D);
        ocupacion=(TextView) v.findViewById(R.id.tOcupacionS2D);
        vivienda=(TextView) v.findViewById(R.id.tViviendaS2D);
        mascotas=(TextView) v.findViewById(R.id.tMascotasS2D);
        del_mun=(TextView) v.findViewById(R.id.tDelegacionS2D);
        ciudad=(TextView) v.findViewById(R.id.tCiudadS2D);
        estado=(TextView) v.findViewById(R.id.tEstadiS2D);
        fecha=(TextView) v.findViewById(R.id.tFechaSD2);
        estadoSol=(TextView) v.findViewById(R.id.tEstadoSolS2D);


        nombres.setText(listaSolicitudes.get(position).getNombre());
        sexo.setText(listaSolicitudes.get(position).getSexo());
        edad.setText(listaSolicitudes.get(position).getEdad());
        ocupacion.setText(listaSolicitudes.get(position).getOcupacion());
        vivienda.setText(listaSolicitudes.get(position).getVivienda());
        mascotas.setText(listaSolicitudes.get(position).getMascotas());
        del_mun.setText(listaSolicitudes.get(position).getDele_mun());
        ciudad.setText(listaSolicitudes.get(position).getCiudad());
        estado.setText(listaSolicitudes.get(position).getEstado());
        fecha.setText(listaSolicitudes.get(position).getFecha());
        estadoSol.setText(listaSolicitudes.get(position).getEstadoSol());

    }



    @Override
    public void onclickElemento(int position) { }

    @Override
    public void onclickElemento(int position, View view) {

        boolean c;
        switch (view.getId()){
            case R.id.tVerMasS:
                mostrarDialogo(position);
                break;
            case R.id.botonAceptar:
                c=true;
                confirmar(position,c);
                break;
            case R.id.botonRechazar:
                c=false;
                confirmar(position,c);
                break;
            default:
                break;
        }

    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    private void confirmar(int position,boolean c) {

        if(c){
            tipo="aceptar";
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Aceptar solicitud");
            builder.setMessage("¿Estás seguro que deseas aceptar la solicitud?");

            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String idSol=listaSolicitudes.get(position).getIdSolicitud();
                    estadoSolicitud(position,idSol);
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
        }else{
            tipo="rechazar";
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Rechazar solicitud");
            builder.setMessage("¿Estás seguro que deseas rechazar la solicitud?");

            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String idSol= listaSolicitudes.get(position).getIdSolicitud();
                    estadoSolicitud(position,idSol);
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

    }



    private void estadoSolicitud(int position,String idSol) {

            String URL="https://fundacionalbornoz.com/solicitudesRecibidas.php";

            StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("Correo enviado!")){
                        Toast.makeText(getContext(),"Solicitud aceptada correctamente. Se envió un correo al" +
                                " refugio o rescatista", Toast.LENGTH_LONG).show();
                        onRefresh();
                    }else if(response.contains("rechazada")){
                        Toast.makeText(getContext(),"Solicitud rechazada correctamente", Toast.LENGTH_LONG).show();
                        onRefresh();
                    }else{
                        Toast.makeText(getContext(),"Error al procesar solicitud, intentelo más tarde", Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getContext(),"Error al procesar solicitud, intentelo más tarde", Toast.LENGTH_LONG).show();
                }}){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros=new HashMap<>();
                    parametros.put("tipo",tipo);
                    parametros.put("idMascota","");
                    parametros.put("idSolicitud",idSol);
                    parametros.put("correo","");
                    return parametros;
                }
            };

            Volley.newRequestQueue(getContext()).add(stringRequest);

    }

    @Override
    public void onRefresh() {
        listaSolicitudes=new ArrayList<>();
        tipo="solXmascota";
        llenarSolicitudes(SolicitudesRecibidas.this);
        new Handler().postDelayed(new Runnable() {

            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }
}