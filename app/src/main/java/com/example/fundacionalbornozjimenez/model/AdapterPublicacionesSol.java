package com.example.fundacionalbornozjimenez.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundacionalbornozjimenez.R;

import java.util.ArrayList;

public class AdapterPublicacionesSol extends RecyclerView.Adapter<AdapterPublicacionesSol.ViewHolderPubSol>{

    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<PublicacionesModel> listaPublicaciones;

    public AdapterPublicacionesSol(ArrayList<PublicacionesModel> listaPublicaciones,RecyclerViewInterface recyclerViewInterface){

        this.recyclerViewInterface = recyclerViewInterface;
        this.listaPublicaciones=listaPublicaciones;

    }


    @NonNull
    @Override
    public ViewHolderPubSol onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.publicacion_sol,null,false);
        return new ViewHolderPubSol(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPubSol holder, int position) {
        holder.nombre.setText(listaPublicaciones.get(position).getNombre());
        holder.sexo.setImageResource(listaPublicaciones.get(position).getIconoSexo());
        holder.ciudadI.setImageResource(listaPublicaciones.get(position).getIconoCiudad());
        holder.ciudad.setText(listaPublicaciones.get(position).getCiudad());
        holder.edad.setText(listaPublicaciones.get(position).getEdad());
        holder.personalidad.setText(listaPublicaciones.get(position).getPersonalidad());
        holder.raza.setText(listaPublicaciones.get(position).getRaza());


    }


    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public static class ViewHolderPubSol extends RecyclerView.ViewHolder {
        TextView nombre,ciudad,edad,personalidad,raza;
        ImageView sexo,ciudadI;
        TextView verSol;



        public ViewHolderPubSol(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            nombre=itemView.findViewById(R.id.tNombrePS);
            sexo=itemView.findViewById(R.id.mSexPS);
            ciudadI=itemView.findViewById(R.id.mUbicacionPS);
            ciudad=itemView.findViewById(R.id.tUbicacionPS);
            edad=itemView.findViewById(R.id.tEdadPS);
            personalidad=itemView.findViewById(R.id.tPersonalidadPS);
            raza=itemView.findViewById(R.id.tRazaPS);
            verSol=itemView.findViewById(R.id.tVerMasPS);




            verSol.setOnClickListener(new View.OnClickListener() {
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
