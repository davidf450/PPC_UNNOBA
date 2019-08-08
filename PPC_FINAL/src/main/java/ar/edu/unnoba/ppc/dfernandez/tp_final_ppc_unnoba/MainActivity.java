package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    RequestQueue cola;
    String url = "http://ppc.edit.com.ar:8080/resources/datos/obras/";
    List<Obra> obras;
    AlertDialogManager alert;
    RecyclerView listado_obras;
    ObrasAdapter adapter;
    FusedLocationProviderClient fusedLocationClient;
    ProgressBar pg;
    JsonObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(LoginActivity.S_PREFERENCES, Context.MODE_PRIVATE);
        pg = (ProgressBar) findViewById(R.id.loading_spinner);
        listado_obras = findViewById(R.id.recycler);
        //alert.showAlertDialog(MainActivity.this, "Bienvenido", "Bienvenido "+sharedPreferences.getString("user","invitado")+" al sistema de gestion de obras", false);
        cola = Volley.newRequestQueue(this);
        gson = new Gson();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationFromProvider();
        cargar_obras();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(i);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargar_obras(){
        cola = Volley.newRequestQueue(this);
        JsonArrayRequest json_request = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        llenar_lista(response);
                        pg.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                llenar_lista(null);
                pg.setVisibility(View.GONE);
                /*new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.stat_notify_error)
                        .setTitle("Error")
                        .setMessage("No es posible conectar con el web service en \n ["+url+"]")
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent i = getIntent();
                                startActivity(i);
                                }

                        })
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(i);
                            }

                        })
                        .show();
                System.out.println("ERROR: hubo un error al conectar con el Web Service en [ "+url+" ]");*/
            }
        });

        cola.add(json_request);
    }
    private void llenar_lista(JSONArray source){
        if (source!=null) {
            obras = Arrays.asList(gson.fromJson(source.toString(), Obra[].class));
        }else{
            obras=new ArrayList<>();
            obras.add(new Obra("Galpon descubierto","Reparar columnas",81245.2475,"Florida 345, Junin",-33.887246,-60.5657928,54236445667L,0.0));
            obras.add(new Obra("Edificio interminable","Verificar trabajo",80245.2475,"Liniers 558, Junin",-31.887246,-59.5657928,54236420336L,0.0));
            obras.add(new Obra("Casa de la otra punta","Traer 100g de clavos y martillo",82648.7748,"España 430, Junin",-32.995471,-61.5578615,54236417556L,0.0));

        }
        if (obras != null && !obras.isEmpty()) {
            adapter = new ObrasAdapter(obras);
            listado_obras.setAdapter(adapter);
        } else {
            alert.showAlertDialog(MainActivity.this, "Error!", "No hay obras para mostrar", false);
        }
    }

    private void getLocationFromProvider() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        url = url+location.getLatitude()+"/"+location.getLongitude();
                        System.out.println("***************************************-->"+url);
                        cargar_obras();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationFromProvider();
                }
                break;
            }
        }
    }


    @Override
    public void onBackPressed(){
        finish();
    }
}
