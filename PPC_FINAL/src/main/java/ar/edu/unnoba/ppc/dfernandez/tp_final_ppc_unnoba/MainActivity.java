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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
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
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private static final int REQUEST_CHECK_SETTINGS = 1900 ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    RequestQueue cola;
    String url;
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
        checkLocationServices(mLocationRequest);

    }

    private void checkLocationServices(LocationRequest request){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            //las configuracion no son correctas, mostrar un dialogo al usuario
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                //mostramos el dialogo con startResolutionForResult() y verificamos la respuesta en onActivityResult()
                                resolvable.startResolutionForResult(
                                        MainActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                            } catch (ClassCastException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //no es posible cambiar las configuraciones actuales, no hay necesidad de mostrar el dialogo
                            break;
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        this.recreate();
                        break;
                    case Activity.RESULT_CANCELED:
                        new AlertDialog.Builder(this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Error")
                                .setMessage("No es posible obtener datos precisos sin servicios de geolocalizacion")
                                .setPositiveButton("Salir", new DialogInterface.OnClickListener()
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
                        break;
                    default:
                        break;
                }
                break;
        }
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
            case R.id.reconectar:
                recreate();
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
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.stat_notify_error)
                        .setTitle("Error")
                        .setMessage("No es posible conectar con el web service, se utilizaran datos locales (podrian no estar actualizados) \n La distancia NO puede calcularse en base a datos locales")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                llenar_lista(null);
                                pg.setVisibility(View.GONE);
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
            if(PPCDatabase.getInstance(this).obraDao().getAll().isEmpty()) {
                for(Obra o:obras){
                    PPCDatabase.getInstance(this).obraDao().insertAll(o);
                }
            }else {
                for (Obra o : obras) {
                    PPCDatabase.getInstance(this).obraDao().update(o);
                }
            }
        }else{
             Log.e(TAG,"Error de conexion - USANDO LA BASE DE DATOS LOCAL");
             try{
                 obras = PPCDatabase.getInstance(this).obraDao().getAll();
                 if(obras.isEmpty()){throw new NullPointerException();}
                 for(Obra ob:obras){
                     Log.e(TAG,ob.toString());
                 }
             }catch (Exception ex){
                 new AlertDialog.Builder(MainActivity.this)
                         .setIcon(android.R.drawable.stat_notify_error)
                         .setTitle("Error")
                         .setMessage("No existen datos locales disponibles, por favor intente mas tarde")
                         .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
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
             }
        }
        if (obras != null && !obras.isEmpty()) {
            //el json que provee el web service deberia contener un campo con un enlace a un recurso web que ilustre la obra
            obras.get(0).setReferenceImage(R.drawable.galpon_xs);
            obras.get(1).setReferenceImage(R.drawable.edificio_interminable_xs);
            obras.get(2).setReferenceImage(R.drawable.casa_xs);
            adapter = new ObrasAdapter(obras);
            listado_obras.setAdapter(adapter);
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        url = "http://ppc.edit.com.ar/resources/datos/obras/"+location.getLatitude()+"/"+location.getLongitude();
        //url = url+location.getLatitude()+"/"+location.getLongitude();
        cargar_obras();
    }
}
