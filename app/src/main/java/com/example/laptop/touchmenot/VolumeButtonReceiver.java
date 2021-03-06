package com.example.laptop.touchmenot;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Laptop on 3/14/2016.
 */
public class VolumeButtonReceiver extends BroadcastReceiver {

    private VolumeDBOpenHelper vdbHelper;
    private PatternDBOpenHelper pdbHelper;
    private SharedPreferences shared;
    private PressedDBOpenHelper pressedbHelper;
    private double latitude;
    private double longitude;

    public VolumeButtonReceiver(){
    }

    public void onReceive(final Context context, Intent intent) {

        LocationManager locMan = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
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
        };

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
        }

        long timePressed = Calendar.getInstance().getTimeInMillis();
        shared = context.getSharedPreferences(ChangePatternActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
        vdbHelper = new VolumeDBOpenHelper(context);
        pdbHelper = new PatternDBOpenHelper(context);
        pressedbHelper = new PressedDBOpenHelper(context);
        if(!shared.getBoolean(ChangePatternActivity.RECORDING_VALUE, false) && shared.getBoolean(MainActivity.DETECTING_VALUE,false)) {
            int prevVolume = vdbHelper.getCurrentVolume();
            int volume = (Integer) intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
            if(prevVolume == -1)
                vdbHelper.newVolume(volume);
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            PressedButton lastPressed = pressedbHelper.getLastPressed();
            PatternKeys currKey;
            if(lastPressed != null)
                currKey = pdbHelper.getKey(lastPressed.getOrder() + 1);
            else currKey = pdbHelper.getKey(1);
            int lastKeyOrder = pdbHelper.lastIndex();
            if (prevVolume > volume || (volume == 0 && prevVolume == volume)) {
                if(currKey.getKey().equals(PatternKeys.VALUE_VOLUME_DOWN)){
                    if(currKey.getOrder() == lastKeyOrder){
                        pressedbHelper.clearPattern();
                        Toast.makeText(context, "SMS should send now",
                                Toast.LENGTH_SHORT).show();
                        shared.edit().putBoolean(PressedButton.DETECTING_PREFERENCE,true).apply();
                        final Timer timer = new Timer();
                        TimerTask timertask = new TimerTask() {
                            @Override
                            public void run() {
                                if(shared.getBoolean(PressedButton.DETECTING_PREFERENCE,false)) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    //get contacts
                                    MessageDBOpenHelper mdbHelper = new MessageDBOpenHelper(context);
                                    String message = mdbHelper.getMessage();
                                    message = message + "\n Last known location of Phone: " + latitude + "," + longitude;
                                    smsManager.sendTextMessage("09158343137", null, message, null, null);
                                    timer.cancel();
                                }else {
                                    timer.cancel();
                                }
                            }
                        };
                        IntervalDBOpenHelper idbHelper = new IntervalDBOpenHelper(context);
                        timer.schedule(timertask, 0, idbHelper.getInterval() * 1000);
                    }else if(currKey.getOrder() < lastKeyOrder){
                        if(lastPressed == null || ((timePressed - lastPressed.getTimePressed()) <= 5000)){
                            PressedButton currPressed = new PressedButton();
                            if(lastPressed == null)
                                currPressed.setOrder(1);
                            else currPressed.setOrder(lastPressed.getOrder()+1);
                            currPressed.setButton(PatternKeys.VALUE_VOLUME_DOWN);
                            currPressed.setTimePressed(timePressed);
                            pressedbHelper.insertPressedButton(currPressed);
                            Toast.makeText(context, currKey.getKey(),
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            System.out.println("too slow sanic is disappoint");
                            pressedbHelper.clearPattern();
                        }
                    }else{
                        System.out.println("ehh? how'd you get here?");
                        pressedbHelper.clearPattern();
                    }
                }else{
                    System.out.println("wrong key yo");
                    pressedbHelper.clearPattern();
                }
            } else if (prevVolume < volume || (volume == mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) && prevVolume == volume)) {
                if(currKey.getKey().equals(PatternKeys.VALUE_VOLUME_UP)){
                    if(currKey.getOrder() == lastKeyOrder){
                        pressedbHelper.clearPattern();
                        Toast.makeText(context, "SMS should send now",
                                Toast.LENGTH_SHORT).show();
                        shared.edit().putBoolean(PressedButton.DETECTING_PREFERENCE,true).apply();
                        final Timer timer = new Timer();
                        TimerTask timertask = new TimerTask() {
                            @Override
                            public void run() {
                                if(shared.getBoolean(PressedButton.DETECTING_PREFERENCE,false)) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    //get contacts
                                    MessageDBOpenHelper mdbHelper = new MessageDBOpenHelper(context);
                                    String message = mdbHelper.getMessage();
                                    message = message + "\n Last known location of Phone: " + latitude + "," + longitude;
                                    smsManager.sendTextMessage("09158343137", null, message, null, null);
                                    timer.cancel();
                                }else {
                                    timer.cancel();
                                }
                            }
                        };
                        IntervalDBOpenHelper idbHelper = new IntervalDBOpenHelper(context);
                        timer.schedule(timertask, 0, idbHelper.getInterval() * 1000);
                    }else if(currKey.getOrder() < lastKeyOrder){
                        if(lastPressed == null || ((timePressed - lastPressed.getTimePressed()) <= 5000)){
                            PressedButton currPressed = new PressedButton();
                            if(lastPressed == null)
                                currPressed.setOrder(1);
                            else currPressed.setOrder(lastPressed.getOrder()+1);
                            currPressed.setButton(PatternKeys.VALUE_VOLUME_UP);
                            currPressed.setTimePressed(timePressed);
                            pressedbHelper.insertPressedButton(currPressed);
                            Toast.makeText(context, currKey.getKey(),
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            System.out.println("too slow sanic is disappoint");
                            pressedbHelper.clearPattern();
                        }
                    }else{
                        System.out.println("ehh? how'd you get here?");
                        pressedbHelper.clearPattern();
                    }
                }else{
                    System.out.println("wrong key yo");
                    pressedbHelper.clearPattern();
                }
            }
            vdbHelper.saveCurrentVolume(volume);
        }
    }
}
