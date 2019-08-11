package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private static final int PERMISSION_CHECK = 1700;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int INTERVAL = 10000;
    private static final int FAST_INTERVAL = 1000;
    private LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Obtenemos el fragmento correspondiente
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //Asignamos esta actividad como Listener del Callback para saber cuando esta listo el Mapa
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, PERMISSION_CHECK);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Conectado a los servicios de geolocalizacion");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
            return;
        }
        getLocation();
    }
    private void handleNewLocation(Location location) {
        Log.d(TAG, "El dispositivo se encuentra en: ["+location.getLatitude()+","+location.getLongitude());
        LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng obraPosition = new LatLng(getIntent().getDoubleExtra("latitud",0),getIntent().getDoubleExtra("longitud",0));
        MarkerOptions currentPosObra = new MarkerOptions()
                .position(obraPosition)
                .title(getIntent().getStringExtra("descripcion"));
        MarkerOptions currentPosUser = new MarkerOptions()
                .position(userPosition)
                .title("Posicion actual");
        /* TODO: visualizar dos infoWindow al mismo tiempo en el mapa sin requerir pulsar sobre el marcador */
        mMap.addMarker(currentPosObra).showInfoWindow();
        mMap.addMarker(currentPosUser).showInfoWindow();
        LatLngBounds AREA = LatLngBounds.builder().include(userPosition).include(obraPosition).build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AREA, 100));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Se suspendio la conexion, por favor reconectar..");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Los servicios de geolocalizacion fallaron con codigo: " + connectionResult.getErrorCode());
        }
    }
    private void mapSetUp() {
        if (mMap == null) {
            mapFragment.getMapAsync(this);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapSetUp();
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
        handleNewLocation(location);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }
}
