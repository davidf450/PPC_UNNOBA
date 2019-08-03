package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocacionActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Location location;
    private MarkerOptions marker;
    private LatLng currentPosition;
    private LocationManager locationManager;
    public static final int PERMISSION_CHECK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locacion);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CHECK);
        } else {
            obtainLocation();
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void obtainLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lo = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lo2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location lo3 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if(lo!=null){
            System.out.println("*******************ENTRO POR EL GPS*****************");
            this.location = lo;
        }else if (lo2!=null){
            System.out.println("*******************ENTRO POR EL NETWORK*****************");
            this.location = lo2;
        }else if (lo3!=null){
            System.out.println("*******************ENTRO POR EL PASSIVE*****************");
            this.location = lo3;
        }
        if(this.location==null){
            System.out.println("*******************NINGUN PROVIDER FUNCIONO*****************");
            this.location = new Location(LocationManager.GPS_PROVIDER);
            this.location.setLatitude(-33.892987);
            this.location.setLongitude(-60.572388);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CHECK: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    obtainLocation();
                } else {
                    System.out.println("********//////////PERMISO DENEGADO/////////*********");
                }
            }
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        //marker = new MarkerOptions().position(currentPosition).title("Posicion actual");
        //mMap.addMarker(marker);
        mMap.addMarker(new MarkerOptions().position(currentPosition).title("Posicion actual"));
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPosition).zoom(17).bearing(0).build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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
