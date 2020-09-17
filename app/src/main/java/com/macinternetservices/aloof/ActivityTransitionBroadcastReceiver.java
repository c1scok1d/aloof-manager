package com.macinternetservices.aloof;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.model.LatLng;

import com.macinternetservices.aloof.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import static com.macinternetservices.aloof.MainFragment.trackedDevice;

public class ActivityTransitionBroadcastReceiver extends BroadcastReceiver {

    public static final String INTENT_ACTION = "org.traccar.manager" +
            ".ACTION_PROCESS_ACTIVITY_TRANSITIONS";

    private static LocationManager locManager;
    private static LocationListener locListener;

    //String latitude, longitude;
    public static ArrayList walkPoints = new ArrayList();
    public static ArrayList runPoints = new ArrayList();
    public static ArrayList bikePoints = new ArrayList();
    public static ArrayList drivePoints = new ArrayList<>();
    public static ArrayList walkSpeed = new ArrayList();
    public static ArrayList runSpeed = new ArrayList();
    public static ArrayList bikeSpeed = new ArrayList();
    public static ArrayList driveSpeed = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Transition","Transition Received");
        if (intent != null && INTENT_ACTION.equals(intent.getAction())) {
            if (ActivityTransitionResult.hasResult(intent)) {
                ActivityTransitionResult intentResult = ActivityTransitionResult
                        .extractResult(intent);
                // handle activity transition result ...
                for (ActivityTransitionEvent event : intentResult.getTransitionEvents()) {
                    // chronological sequence of events....
                    if (event.getActivityType() == DetectedActivity.WALKING && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        locListener = new LocationListener() {
                            @Override
                            public void onStatusChanged(String provider, int status,
                                                        Bundle extras) {
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                            @Override
                            public void onLocationChanged(Location location) {
                                walkPoints.add(new LatLng(location.getLatitude(), location.getLongitude()) );
                                walkSpeed.add(location.getSpeed() * 1.15078);
                            }
                        };
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locListener);
                        Date walkStartTime = Calendar.getInstance().getTime();
                        showNoti(context,trackedDevice+" is walking");
                    } else if (event.getActivityType() == DetectedActivity.WALKING && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_EXIT){
                        Date walkEndTime = Calendar.getInstance().getTime();
                        Double min = (Double) Collections.min(walkSpeed);
                        Double max = (Double) Collections.max(walkSpeed);
                        showNoti2(context,trackedDevice+" stopped walking at: "+walkEndTime, "Max Speed: "+String.format("%.2f", max)+"MPH");
                        //showNoti(context,);
                        /*
                        on exit add walkStartTime, walkEndTime and walkPoints array to rooms db named transitions
                        only store data for last 5 transitions in db
                         */
                        //showNoti(context,"Exited from Walking Activity");
                    } else if (event.getActivityType() == DetectedActivity.STILL && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                        String stillStartTime = Calendar.getInstance().getTime().toString();
                        showNoti(context,trackedDevice+" is still");
                    } else if (event.getActivityType() == DetectedActivity.STILL && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_EXIT){
                        //Date stillEndTime = Calendar.getInstance().getTime();
                        //showNoti(context,"Exited from Still Activity");
                    } else if (event.getActivityType() == DetectedActivity.RUNNING && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        locListener = new LocationListener() {
                            @Override
                            public void onStatusChanged(String provider, int status,
                                                        Bundle extras) {
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                            @Override
                            public void onLocationChanged(Location location) {
                                runPoints.add(new LatLng(location.getLatitude(), location.getLongitude()) );
                                runSpeed.add(location.getSpeed() * 1.15078);
                            }
                        };
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locListener);
                        Date runStartTime = Calendar.getInstance().getTime();
                        showNoti(context,trackedDevice+" is running");
                    } else if (event.getActivityType() == DetectedActivity.RUNNING && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_EXIT){
                        Date runEndTime = Calendar.getInstance().getTime();
                        Double min = (Double) Collections.min(runSpeed);
                        Double max = (Double) Collections.max(runSpeed);
                        showNoti2(context,trackedDevice+" stopped running at: "+runEndTime, "Max Speed: "+String.format("%.2f", max) );
                    } else if (event.getActivityType() == DetectedActivity.IN_VEHICLE && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        locListener = new LocationListener() {
                            @Override
                            public void onStatusChanged(String provider, int status,
                                                        Bundle extras) {
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }

                            @Override
                            public void onLocationChanged(Location location) {
                                drivePoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                                driveSpeed.add(location.getSpeed() * 1.15078);
                            }
                        };
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locListener);
                        Date driveStartTime = Calendar.getInstance().getTime();
                        showNoti(context,trackedDevice+" is driving");
                    } else if (event.getActivityType() == DetectedActivity.IN_VEHICLE && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_EXIT){
                        Date driveEndTime = Calendar.getInstance().getTime();
                        Double min = (Double) Collections.min(driveSpeed);
                        Double max = (Double) Collections.max(driveSpeed);
                        showNoti2(context,trackedDevice+" stopped driving at: "+driveEndTime, "Max Speed: "+String.format("%.2f", max));
                    } else if (event.getActivityType() == DetectedActivity.ON_BICYCLE && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        locListener = new LocationListener() {
                            @Override
                            public void onStatusChanged(String provider, int status,
                                                        Bundle extras) {
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                            @Override
                            public void onLocationChanged(Location location) {
                                bikePoints.add(new LatLng(location.getLatitude(), location.getLongitude()) );
                                bikeSpeed.add(location.getSpeed() * 1.15078);
                            }
                        };
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locListener);
                        Date bikeStartTime = Calendar.getInstance().getTime();
                        showNoti(context,trackedDevice+" is biking");
                    } else if (event.getActivityType() == DetectedActivity.ON_BICYCLE && event.getTransitionType() == ActivityTransition.ACTIVITY_TRANSITION_EXIT){
                        Date bikeEndTime = Calendar.getInstance().getTime();
                        Double min = (Double) Collections.min(bikeSpeed);
                        Double max = (Double) Collections.max(bikeSpeed);
                        showNoti2(context,trackedDevice+" stopped biking at: "+bikeEndTime, "Max Speed: "+String.format("%.2f", max));
                    }
                }
            }
        }
    }

    private void showNoti(final Context mContext,final String message){
        createNotificationChannel(mContext);
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle(message)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notifManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(new Random().nextInt(), notification);
    }

    private void showNoti2(final Context mContext,final String message, final String message2){
        createNotificationChannel(mContext);
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle(message)
                .setContentText(message2)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notifManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(new Random().nextInt(), notification);
    }

    public static final String CHANNEL_ID = "Transition Channel";
    private void createNotificationChannel(final Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Transition Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
