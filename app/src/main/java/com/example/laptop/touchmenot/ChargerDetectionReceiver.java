package com.example.laptop.touchmenot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ChargerDetectionReceiver extends BroadcastReceiver {

    int state;


    @Override
    public void onReceive(Context context, Intent intent) {

                state = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                Log.i("charger", intent.getAction());
                Log.i("CHARGER", Intent.ACTION_BATTERY_CHANGED);


                //boolean active = getIntent().getExtras().getBoolean("active");
                PasswordDBOpenHelper pdbo = new PasswordDBOpenHelper(context);
                if ((state == BatteryManager.BATTERY_PLUGGED_AC
                        || state == BatteryManager.BATTERY_PLUGGED_USB) && MainActivity.chargerActive == false) {
                    if(pdbo.getPassword(1) != null){
                        Toast.makeText(context, "Charging and now activating",
                                Toast.LENGTH_SHORT).show();
                        MainActivity.chargerActive = true;

                    }
                    else{
                        Toast.makeText(context, "Set password first",
                                Toast.LENGTH_SHORT).show();
                    }

                }

                else if ((state == BatteryManager.BATTERY_PLUGGED_AC
                        || state == BatteryManager.BATTERY_PLUGGED_USB) && MainActivity.chargerActive == true) {


                    Toast.makeText(context, "Charging and now deactivating",
                            Toast.LENGTH_SHORT).show();/*
                    MainActivity.active = false;
                    finish();*/
                }

                else if (state == 0) {
                    // not charging or on battery power
                    Toast.makeText(context, "Not charging.",
                            Toast.LENGTH_SHORT).show();
//                    MainActivity.active = false;
                }

        };
//        if(!MainActivity.active) {
//        }else{
//            unregisterReceiver(receiver);
//        }

}
