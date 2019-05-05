package br.uff.bcnogueira.mapapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import br.uff.bcnogueira.mapapp.directionHelpers.FetchUrl;
import br.uff.bcnogueira.mapapp.directionHelpers.TaskLoadedCallback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    MarkerOptions origem, destino;
    Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        origem = new MarkerOptions().position(new LatLng(2.8235, -60.6758)).title("Roraima");
        destino = new MarkerOptions().position(new LatLng(-30.0277, -51.2287)).title("Porto Alegre");

        String url = getUrl(origem.getPosition(), destino.getPosition(), "driving");
        new FetchUrl(MapsActivity.this).execute(url, "driving");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(origem);
        mMap.addMarker(destino);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origem.getPosition(), 15.0f));
    }

    private String getUrl(LatLng origem, LatLng destino, String directionMode) {
        String str_origem = "origin=" + origem.latitude + "," + origem.longitude;
        String str_destino = "destination=" + destino.latitude + "," + destino.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origem + "&" + str_destino + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters
                + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
