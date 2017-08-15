package com.example.chint.googlemaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class MList extends AppCompatActivity {

    //My location, San Francisco
    double lat = 39.000089;
    double lng = -76.863842;
    LatLng latLng = new LatLng(lat, lng);

    TextView Before, After;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Before = (TextView)findViewById(R.id.before);
        After = (TextView) findViewById(R.id.after);

        //set up list
        ArrayList<Place> places = new ArrayList<Place>();

        places.add(new Place(new LatLng(40.7128,-74.0059)));
        places.add(new Place(new LatLng(38.13455657705411,-105.8203125)));
        places.add(new Place(new LatLng(17.3850,-78.4867)));

        for (Place p: places){
            //Before.append("\n"+p.name+"\n");
        }

        Collections.sort(places, new SortPlaces(latLng));

        for (Place p: places){
            //After.append("\n"+p.name+"\n");
        }
    }
}
