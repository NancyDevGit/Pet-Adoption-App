package com.example.fundacionalbornozjimenez.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.fundacionalbornozjimenez.R;

import java.util.ArrayList;


public class AdapterDatosAdopciones extends RecyclerView.Adapter<AdapterDatosAdopciones.ViewHolderPublicacion> {


    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<PublicacionesModel> listaPublicaciones;
    private RequestQueue requestQueue;

    public AdapterDatosAdopciones(ArrayList<PublicacionesModel> listaPublicaciones, RecyclerViewInterface recyclerViewInterface, Context context) {
        this.listaPublicaciones = listaPublicaciones;
        this.recyclerViewInterface=recyclerViewInterface;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolderPublicacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.publicacion_small,null,false);
        return new ViewHolderPublicacion(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPublicacion holder, int position) {
        holder.iconoCiudad.setImageResource(listaPublicaciones.get(position).getIconoCiudad());
        holder.iconoSexo.setImageResource(listaPublicaciones.get(position).getIconoSexo());
        holder.nombre.setText(listaPublicaciones.get(position).getNombre());
        holder.ciudad.setText(listaPublicaciones.get(position).getCiudad());
        holder.edad.setText(listaPublicaciones.get(position).getEdad());
        holder.personalidad.setText(listaPublicaciones.get(position).getPersonalidad());
        holder.raza.setText(listaPublicaciones.get(position).getRaza());
        holder.masInformacion.setText(listaPublicaciones.get(position).getMasInformacion());

        if (listaPublicaciones.get(position).getFotoS()!=null) {
            cargarImagen(listaPublicaciones.get(position).getFotoS(),holder);


        }else{
            holder.foto.setImageResource(R.drawable.hembra);
        }



    }

    private void cargarImagen(String fotoS,ViewHolderPublicacion holder) {
        requestQueue= Volley.newRequestQueue(context);
        ImageRequest imageRequest =new ImageRequest(fotoS, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                    holder.foto.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        try {
            requestQueue.add(imageRequest);
        }catch (Exception e){
            holder.foto.setImageResource(R.drawable.macho);
        }
    }


    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public static class ViewHolderPublicacion extends RecyclerView.ViewHolder {

        ImageView foto,iconoCiudad,iconoSexo,solicitud;
        TextView nombre, ciudad,edad,personalidad,raza,masInformacion;


        public ViewHolderPublicacion(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            foto=itemView.findViewById((R.id.fotoMascota));
            iconoCiudad=itemView.findViewById(R.id.mUbicacion);
            iconoSexo=itemView.findViewById(R.id.mSex);
            nombre=itemView.findViewById(R.id.tNombreM);
            ciudad=itemView.findViewById(R.id.tUbicacionM);
            edad=itemView.findViewById(R.id.tEdadM);
            personalidad=itemView.findViewById(R.id.tPersonalidadM);
            raza=itemView.findViewById(R.id.tRazaD);
            masInformacion=itemView.findViewById(R.id.tVerMas);
            solicitud=itemView.findViewById(R.id.imgSolicitud);


            solicitud.setVisibility(View.GONE);

            masInformacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onclickElemento(position);
                        }
                    }
                }
            });
        }

    }
}
