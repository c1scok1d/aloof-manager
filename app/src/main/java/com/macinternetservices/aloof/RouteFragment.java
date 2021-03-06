package com.macinternetservices.aloof;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.macinternetservices.aloof.model.Position;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RouteFragment extends SupportMapFragment implements OnMapReadyCallback {
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        getMapAsync(this);

        Bundle transitionDataBundle = this.getArguments();

        if(transitionDataBundle != null) {

            String deviceId = getArguments().getString("deviceId");
            String lastTransactionEndTime = getArguments().getString("lastTransactionEndTime");
            String stillStartTime = getArguments().getString("stillStartTime");
        } else{
            Log.e("RouteFragment", "transitionDataBundle is null");
        }
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
                return view;
            }
        });
        getRoutes();
    }

    Polyline polyline = null;
    ArrayList<LatLng> dataModels;
    Date lastTransactionEndTime, stillStartTime;

    private void getRoutes(){

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(3);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //String lastTransactionEndTime = getArguments().getString("lastTransactionEndTime");
        try {
            lastTransactionEndTime = fmt.parse(getArguments().getString("lastTransactionEndTime"));
            stillStartTime =  fmt.parse(getArguments().getString("lastTransactionEndTime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long different = stillStartTime.getTime() - lastTransactionEndTime.getTime();

        long secondsInMilli = 1000;
        //long minutesInMilli = secondsInMilli * 60;
        //long hoursInMilli = minutesInMilli * 60;
        //long daysInMilli = hoursInMilli * 24;

        /* long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli; */

        long elapsedSeconds = different / secondsInMilli;
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();

        dataModels= new ArrayList<>();
        final MainApplication application = (MainApplication) getActivity().getApplication();
        application.getServiceAsync(new MainApplication.GetServiceCallback() {
            @Override
            public void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service) {
                service.getPositions(getArguments().getString("deviceId"), getArguments().getString("lastTransactionEndTime"), getArguments().getString("stillStartTime")).enqueue(new WebServiceCallback<List<Position>>(getContext()) {

                    @Override
                    public void onSuccess(Response<List<Position>> response) {
                        Log.e("GetRoutes", "Response: " +response.body());
                        for (Position position : response.body()) {
                            if (position != null && elapsedSeconds >= 30) {
                                latLngBoundsBuilder.include(new LatLng(position.getLatitude(), position.getLongitude()));
                                polylineOptions.add(new LatLng(position.getLatitude(), position.getLongitude()));
                            } else {
                                Toast.makeText(getContext(), "No points to display", Toast.LENGTH_LONG).show();
                            }
                        }
                        polyline = map.addPolyline(polylineOptions);

                        /* LatLngBounds bounds = latLngBoundsBuilder.build();
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(position.getLatitude(), position.getLongitude();
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                        map.moveCamera(center); */
                        map.animateCamera(CameraUpdateFactory.zoomTo(16));
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(),100));
                    }
                });

            }

            @Override
            public boolean onFailure() {
                return false;
            }
        });

    }
}