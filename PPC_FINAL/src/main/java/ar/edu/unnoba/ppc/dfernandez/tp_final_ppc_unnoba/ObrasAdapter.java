package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import android.support.v7.widget.RecyclerView;


public class ObrasAdapter extends RecyclerView.Adapter<ObrasAdapter.ObrasViewHolder> {

    private List<Obra> obras;

    public ObrasAdapter(List<Obra> obras) {
        this.obras = obras;
    }

    public class ObrasViewHolder extends RecyclerView.ViewHolder {
        TextView descripcion,detalle,distancia,domicilio,latitud,longitud,telefono,valor;

        public ObrasViewHolder(View view) {
            super(view);
            descripcion = (TextView) view.findViewById(R.id.descripcion);
            detalle = (TextView) view.findViewById(R.id.detalle);
            distancia = (TextView) view.findViewById(R.id.distancia);
            domicilio = (TextView) view.findViewById(R.id.domicilio);
            latitud = (TextView) view.findViewById(R.id.latitud);
            longitud = (TextView) view.findViewById(R.id.longitud);
            telefono = (TextView) view.findViewById(R.id.telefono);
            valor = (TextView) view.findViewById(R.id.valor);
        }
        public void cargarDatosObras(int position){
            Obra obra_mostrar = obras.get(position);
            descripcion.setText(obra_mostrar.getDescripcion());
            detalle.setText(obra_mostrar.getDetalle());
            distancia.setText(String.valueOf(obra_mostrar.getDistancia()));
            domicilio.setText(obra_mostrar.getDomicilio());
            latitud.setText(String.valueOf(obra_mostrar.getLatitud()));
            longitud.setText(String.valueOf(obra_mostrar.getLongitud()));
            telefono.setText("+"+String.valueOf(obra_mostrar.getTelefono()));
            valor.setText(String.valueOf(obra_mostrar.getValor()));

        }

    }


    @Override
    public ObrasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.obra_individual, parent, false);
        return new ObrasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ObrasViewHolder holder, int position) {
        holder.cargarDatosObras(position);
    }

    @Override
    public int getItemCount() {
        return obras.size();
    }
}
