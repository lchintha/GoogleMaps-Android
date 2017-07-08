package com.example.chint.googlemaps;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(apiAvailable()){
            setContentView(R.layout.activity_main);
            initmaps();
        }else{
            Toast.makeText(this, "ntng", Toast.LENGTH_SHORT).show();
        }
    }

    private void initmaps() {
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
        mf.getMapAsync(this);
    }

    public boolean apiAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if(api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        }else
            Toast.makeText(this, "Con't connect to play services", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gm = googleMap;
        goToLocation(27.1750, 78.0422, 15);
    }

    private void goToLocation(double v, double v1, float zoom) {
        LatLng ll = new LatLng(v, v1);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        gm.moveCamera(update);
    }

    public void gotoLocation(View view) {
        EditText et = (EditText)findViewById(R.id.location);
        String location = et.getText().toString().trim();
        Geocoder geo = new Geocoder(this);
        try {
            List<Address> li = geo.getFromLocationName(location, 1);
            Address a = li.get(0);
            String locality = a.getLocality();
            Toast.makeText(this, locality, Toast.LENGTH_SHORT).show();
            double lat = a.getLatitude();
            double lng = a.getLongitude();
            goToLocation(lat, lng, 15);
            gm.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
