package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int PERMISSION_CHECK = 1800;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    RequestQueue cola;
    String url = "http://ppc.edit.com.ar:8080/resources/datos/obras/";
    List<Obra> obras;
    AlertDialogManager alert;
    RecyclerView listado_obras;
    ObrasAdapter adapter;
    ProgressBar pg;
    GoogleApiClient mGoogleApiClient;
    public static final String TAG = MainActivity.class.getSimpleName();
    final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    static final int INTERVAL = 10000;
    static final int FAST_INTERVAL = 1000;
    LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(LoginActivity.S_PREFERENCES, Context.MODE_PRIVATE);
        pg = findViewById(R.id.loading_spinner);
        listado_obras = findViewById(R.id.recycler);
        cola = Volley.newRequestQueue(this);
        gson = new Gson();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(INTERVAL)
                .setFastestInterval(FAST_INTERVAL);
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
                        Log.i(TAG,"Se obtuvo una conexion exitosa con el webService en "+url);
                        llenar_lista(response);
                        pg.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error al conectar con el webService, revertiendo a datos locales");
                llenar_lista(null);
                pg.setVisibility(View.GONE);
                /*
                    Si se encuentra un error con la conexion al webservice, se muestra una alerta
                    solicitando al usuario si desea Reintentar la conexion o salir de la aplicacion.
                    TODO: usar un contador de intentos, luego de cierta cantidad revertir a datos locales


                 */
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
                */
            }
        });

        cola.add(json_request);
    }
    private void llenar_lista(JSONArray source){
        /*
            TODO: Base de datos local de Obras
                En cada conexion exitosa comparar lo obtenido con lo existente en una base local. Si hay datos nuevos
           *    actualizar la base. De esta forma, si no se logra una conexion exitosa con el WS, pueden usarse datos
           *    locales, advirtiendo al usuario de que posiblemente no este actualizados.
        */
         if (source!=null) {
            obras = Arrays.asList(gson.fromJson(source.toString(), Obra[].class));
        }else{
            obras=new ArrayList<>();
            obras.add(new Obra("Galpon descubierto","Reparar columnas",81245.2475,"Florida 345, Junin",-33.887246,-60.5657928,54236445667L,0.0));
            obras.add(new Obra("Edificio interminable","Verificar trabajo",80245.2475,"Liniers 558, Junin",-31.887246,-59.5657928,54236420336L,0.0));
            obras.add(new Obra("Casa de la otra punta","Traer 100g de clavos y martillo",82648.7748,"España 430, Junin",-32.995471,-61.5578615,54236417556L,0.0));

        }
        if (obras != null && !obras.isEmpty()) {
            //el json que provee el web service deberia contener un campo con un enlace a un recurso web que ilustre la obra
            obras.get(0).setImage(R.drawable.galpon_xs);
            obras.get(1).setImage(R.drawable.edificio_interminable_xs);
            obras.get(2).setImage(R.drawable.casa_xs);
            adapter = new ObrasAdapter(obras);
            listado_obras.setAdapter(adapter);
        } else {
            alert.showAlertDialog(MainActivity.this, "Error!", "No hay obras para mostrar", false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CHECK: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Conectado a los servicios de geolocalizacion");
        getLocation();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Se suspendio la conexion, por favor reconectar..");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "Error de conexion con los servicios de geolocalizacion, reintentando..");
        if (connectionResult.hasResolution()) {
            try {
                // Iniciamos una actividad que intenta resolver el error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Los servicios de geolocalizacion fallaron con codigo: " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "El dispositivo se encuentra en: ["+location.getLatitude()+","+location.getLongitude());
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        url = url+location.getLatitude()+"/"+location.getLongitude();
        cargar_obras();
    }
}
