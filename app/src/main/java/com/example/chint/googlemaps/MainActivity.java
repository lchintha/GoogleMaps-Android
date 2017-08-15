package com.example.chint.googlemaps;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gm;
    Button btn;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(apiAvailable()){
            setContentView(R.layout.activity_main);
            initmaps();
        }else{
            Toast.makeText(this, "ntng", Toast.LENGTH_SHORT).show();
        }

        btn = (Button) findViewById(R.id.activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MList.class));
            }
        });
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

        Bitmap bitmap = null;
        try {
            bitmap = new MyTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        gm.addMarker(new MarkerOptions()
                    .position(ll)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .title("TajMahal"));
    }

    class MyTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... params) {
            String s = "https://image.flaticon.com/icons/png/512/33/33622.png";
            URL url = null;
            try {
                url = new URL(s);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    public void gotoLocation(View view) {
        EditText et = (EditText) findViewById(R.id.location);
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
            gm.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
