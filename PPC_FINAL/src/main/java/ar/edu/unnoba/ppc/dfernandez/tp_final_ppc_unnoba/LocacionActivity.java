package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocacionActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private Location currentLocation;
    public static final int PERMISSION_CHECK = 1;


    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locacion);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
            return;
        }else{
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(LocacionActivity.this);
                        // write code here to to make the API call to the weather service and update the UI
                        // this code here is running on the Main UI thread as far as I understand
                    } else {
                        Log.d("GPS ERROR", "No se pudo obtener la locacion actual del usuario");
                    }
                }
            });
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
            return;
        }else{
            currentLocation = fusedLocationClient.getLastLocation().getResult();
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        LatLng obraPosition = new LatLng(getIntent().getDoubleExtra("latitud",0),getIntent().getDoubleExtra("longitud",0));
        mMap.addMarker(new MarkerOptions().position(obraPosition).title(getIntent().getStringExtra("descripcion"))).showInfoWindow();

        LatLng userPosition = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(userPosition).title("Posición actual")).showInfoWindow();
        LatLngBounds AREA;
        if(obraPosition.latitude>userPosition.latitude){
            AREA = new LatLngBounds(userPosition, obraPosition);
        }else{
            AREA = new LatLngBounds(obraPosition, userPosition);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AREA, 100));
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
