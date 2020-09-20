package com.macinternetservices.aloof;

import com.macinternetservices.aloof.model.Device;
import com.macinternetservices.aloof.model.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.macinternetservices.aloof.R;
import com.macinternetservices.aloof.model.Route;

import java.util.HashMap;
import java.util.Map;

import okhttp3.WebSocket;


public class RouteFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private WebSocket webSocket;
    Polyline line;
    Context context;

    private Handler handler = new Handler();
    private ObjectMapper objectMapper = new ObjectMapper();



    private Map<Long, Device> devices = new HashMap<>();
    private Map<Long, Position> positions = new HashMap<>();
    private Map<Long, Marker> markers = new HashMap<>();

    // Static LatLng
    LatLng startLatLng = new LatLng(30.707104, 76.690749);
    LatLng endLatLng = new LatLng(30.721419, 76.730017);

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater(null).inflate(R.layout.view_info, null);
                ((TextView) view.findViewById(R.id.title)).setText(marker.getTitle());
                ((TextView) view.findViewById(R.id.details)).setText(marker.getSnippet());
                //((TextView) view.findViewById(R.id.showRoute)).setText("Route");
                //((TextView) view.findViewById(R.id.addGeofence)).setText("GeoFence");
                return view;
            }
        });

        getRoutes();
    }

    boolean flag = true;
    LatLng prev = null;
    Polyline polyline = null;

    LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
    private void getRoutes(){
        //Polyline polyline;

        PolylineOptions polylineOptions = new PolylineOptions();
        //JSONArray arr = response.getJSONArray("result");
        /*for (int i = 0; i < arr.length(); i++)
        {
            //JSONObject obj = arr.getJSONObject(i);
            String latitude = obj.getString("latitude");
            String longitude = obj.getString("longitude");

            polylineOptions.color(Color.RED);

            polylineOptions.width(3);

            Double lat = Double.parseDouble(latitude);
            Double Longitude = Double.parseDouble(longitude);

            polylineOptions.add(new LatLng(lat, Longitude));
        } */
        polyline=map.addPolyline(polylineOptions);
        //});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.cancel();
        }
    }
}