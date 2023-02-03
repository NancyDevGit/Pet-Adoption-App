package com.example.fundacionalbornozjimenez.model;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundacionalbornozjimenez.R;

import java.util.ArrayList;

public class AdapterSolicitudes extends RecyclerView.Adapter<AdapterSolicitudes.ViewHolderSolicitud> {
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<SolicitudesModel> listaSolicitudes;

    public AdapterSolicitudes(ArrayList<SolicitudesModel> listaSolicitudes,RecyclerViewInterface recyclerViewInterface){

        this.recyclerViewInterface = recyclerViewInterface;
        this.listaSolicitudes=listaSolicitudes;

    }


    @NonNull
    @Override
    public ViewHolderSolicitud onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.solicitud_detalles,null,false);
        return new ViewHolderSolicitud(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSolicitud holder, int position) {
        holder.estadoSol.setText(listaSolicitudes.get(position).getEstadoSol());
        holder.nombre2.setText(listaSolicitudes.get(position).getNombre());
        holder.edad2.setText(listaSolicitudes.get(position).getEdad());
        holder.ocupacion2.setText(listaSolicitudes.get(position).getOcupacion());
        holder.ciudad2.setText(listaSolicitudes.get(position).getCiudad());
        holder.fecha2.setText(listaSolicitudes.get(position).getFecha());


        String es=holder.estadoSol.getText().toString();
        if (!es.equals("Pendiente")){
            holder.aceptar.setEnabled(false);
            holder.aceptar.getBackground().setColorFilter(holder.aceptar.getContext().getResources().getColor(R.color.cBotonnotEnabled), PorterDuff.Mode.MULTIPLY);
            holder.aceptar.setClickable(false);

            holder.rechazar.setEnabled(false);
            holder.rechazar.getBackground().setColorFilter(holder.rechazar.getContext().getResources().getColor(R.color.cBotonnotEnabled), PorterDuff.Mode.MULTIPLY);
            holder.rechazar.setClickable(false);
        }



    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public static class ViewHolderSolicitud extends RecyclerView.ViewHolder {
        Button aceptar, rechazar;
        TextView nombre,sexo,edad,ocupacion,ciudad,fecha;
        TextView nombre2,sexo2,edad2,ocupacion2,ciudad2,fecha2;
        TextView verMas,estadoSol;



        public ViewHolderSolicitud(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            nombre2=itemView.findViewById(R.id.tNombreS2);
            sexo2=itemView.findViewById(R.id.tSexoS2);
            edad2=itemView.findViewById(R.id.tEdadS2);
            ocupacion2=itemView.findViewById(R.id.tOcupacionS2);
            ciudad2=itemView.findViewById(R.id.tCiudadS2);
            fecha2=itemView.findViewById(R.id.tFechaS2);
            aceptar=itemView.findViewById(R.id.botonAceptar);
            rechazar=itemView.findViewById(R.id.botonRechazar);
            verMas=itemView.findViewById(R.id.tVerMasS);
            estadoSol=itemView.findViewById(R.id.tEstadoSolS2);

            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onclickElemento(position,view);
                        }
                    }
                }
            });
           
            rechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onclickElemento(position,view);
                        }
                    }
                }
            });
            
            verMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onclickElemento(position,view);
                        }
                    }
                }
            });
        }

    }

}
