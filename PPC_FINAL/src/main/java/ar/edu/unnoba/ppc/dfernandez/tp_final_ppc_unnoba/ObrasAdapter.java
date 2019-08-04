package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        CardView card;
        ImageView img;
        LinearLayout ll;
        Context context;
        WindowManager windowmanager;
        DisplayMetrics dimension;
        int height;
        boolean expanded;
        final int CLOSED = 945;

        public ObrasViewHolder(View view) {
            super(view);
            /*windowmanager  = (WindowManager)  context.getSystemService(Context.WINDOW_SERVICE);
            dimension  = new DisplayMetrics();

            windowmanager.getDefaultDisplay().getMetrics(dimension);
            height = dimension.heightPixels;*/

            ll = (LinearLayout) view.findViewById(R.id.parentLayout);
            card = (CardView) view.findViewById(R.id.card_view);
            img = (ImageView) view.findViewById(R.id.imgConstruccion);
            descripcion = (TextView) view.findViewById(R.id.descripcion);
            detalle = (TextView) view.findViewById(R.id.detalle);

            distancia = (TextView) view.findViewById(R.id.distancia);
            domicilio = (TextView) view.findViewById(R.id.domicilio);
            latitud = (TextView) view.findViewById(R.id.latitud);
            longitud = (TextView) view.findViewById(R.id.longitud);
            telefono = (TextView) view.findViewById(R.id.telefono);
            valor = (TextView) view.findViewById(R.id.valor);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ViewGroup.LayoutParams llparams = ll.getLayoutParams();
                    ViewGroup.LayoutParams cardParams = card.getLayoutParams();
                    if(!expanded){
                        llparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        ll.setLayoutParams(llparams);
                        cardParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        card.setLayoutParams(cardParams);
                        expanded=true;

                    }else{
                        llparams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        ll.setLayoutParams(llparams);
                        System.out.println(CLOSED);
                        cardParams.height = CLOSED;
                        expanded=false;
                        card.setLayoutParams(cardParams);

                    }


                }
            });
        }
        public void cargarDatosObras(int position){
            Obra obra_mostrar = obras.get(position);
            descripcion.setText(obra_mostrar.getDescripcion());
            detalle.setText(obra_mostrar.getDetalle());

            switch (position) {
                case 0:
                    img.setImageResource(R.drawable.galpon_xs);
                    break;
                case 1:
                    img.setImageResource(R.drawable.edificio_interminable_xs);
                    break;
                case 2:
                    img.setImageResource(R.drawable.casa_xs);
                    break;
            }
            distancia.setText("Distancia:       "+String.format("%.3f",obra_mostrar.getDistancia())+" metros");
            domicilio.setText("Domicilio:       "+obra_mostrar.getDomicilio());
            latitud.setText("Latitud:           "+String.valueOf(obra_mostrar.getLatitud()));
            longitud.setText("Longitud:        "+String.valueOf(obra_mostrar.getLongitud()));
            telefono.setText("Telefono:        +"+String.valueOf(obra_mostrar.getTelefono()));
            valor.setText("Valor:               "+String.valueOf(obra_mostrar.getValor()));

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
