package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;

public class ObrasDetail extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView descripcion,detalle,distancia,domicilio,latitud,longitud,telefono,valor;
    ImageView imgConstruccion;
    Obra obra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obra_details);
        sharedPreferences = getSharedPreferences(LoginActivity.S_PREFERENCES, Context.MODE_PRIVATE);
        descripcion = findViewById(R.id.descripcion);
        detalle = findViewById(R.id.detalle);
        distancia = findViewById(R.id.distancia);
        domicilio = findViewById(R.id.domicilio);
        latitud = findViewById(R.id.latitud);
        longitud = findViewById(R.id.longitud);
        telefono = findViewById(R.id.telefono);
        valor = findViewById(R.id.valor);
        imgConstruccion = findViewById(R.id.imgConstruccion);
        Gson gson = new Gson();
        obra = gson.fromJson(getIntent().getStringExtra("myjson"), Obra.class);

        imgConstruccion.setImageResource(obra.getImageReference());
        descripcion.setText(obra.getDescripcion());
        detalle.setText(obra.getDetalle());
        distancia.setText(String.valueOf(obra.getDistancia()));
        domicilio.setText(obra.getDomicilio());
        latitud.setText(String.valueOf(obra.getLatitud()));
        longitud.setText(String.valueOf(obra.getLongitud()));
        telefono.setText("+"+obra.getTelefono());
        valor.setText(String.valueOf(obra.getValor()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Esta seguro de que desea cerrar la sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent i = new Intent(ObrasDetail.this, LoginActivity.class);
                                startActivity(i);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            case R.id.MapsView:
                String uri = "http://maps.google.com/maps?daddr=" + obra.getLatitud() + "," + obra.getLongitud() + " (" + obra.getDescripcion() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                return true;
            case R.id.PpcMapView:
                Intent i = new Intent(this,MapsActivity.class);
                i.putExtra("latitud",obra.getLatitud());
                i.putExtra("longitud",obra.getLongitud());
                i.putExtra("descripcion",obra.getDescripcion());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
