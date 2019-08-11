package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

public class ObrasAdapter extends RecyclerView.Adapter<ObrasAdapter.ObrasViewHolder> {

    private List<Obra> obras;
    private Location loc;
    public ObrasAdapter(List<Obra> obras) {
        this.obras = obras;
    }
    public  ObrasAdapter(){};
    public class ObrasViewHolder extends RecyclerView.ViewHolder {
        TextView descripcion,detalle;
        CardView card;
        ImageView img;
        LinearLayout ll;
        Obra obra_mostrar;

        public ObrasViewHolder(View view) {
            super(view);
            ll = view.findViewById(R.id.parentLayout);
            card = view.findViewById(R.id.card_view);
            img = view.findViewById(R.id.imgConstruccion);
            descripcion = view.findViewById(R.id.descripcion);
            detalle = view.findViewById(R.id.detalle);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(img.getContext(),ObrasDetail.class);
                    Gson gson = new Gson();
                    String obra_json = gson.toJson(obra_mostrar);
                    intent.putExtra("myjson", obra_json);
                    intent.putExtra("location",loc);
                    img.getContext().startActivity(intent);
                }
            });
        }
        public void setObra(Obra obra){
            this.obra_mostrar = obra;
        }

        public void updateUI(){
            descripcion.setText(obra_mostrar.getDescripcion());
            detalle.setText(obra_mostrar.getDetalle());
            img.setImageResource(obra_mostrar.getImageReference());
        }

    }

    @NonNull
    @Override
    public ObrasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.obra_individual, parent, false);
        return new ObrasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ObrasViewHolder holder, int position) {
        holder.setObra(obras.get(position));
        holder.updateUI();
    }

    @Override
    public int getItemCount() {
        return obras.size();
    }
    public void setLocation(Location location){
        this.loc = location;
    }
}
