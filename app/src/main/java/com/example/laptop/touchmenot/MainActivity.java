package com.example.laptop.touchmenot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rlCharger, rlMotion, rlSim, rlSettings;
    boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlSettings = (RelativeLayout) findViewById(R.id.rlSettings);
        rlCharger = (RelativeLayout) findViewById(R.id.rlCharger);
        rlMotion = (RelativeLayout) findViewById(R.id.rlMotion);
        rlSim = (RelativeLayout) findViewById(R.id.rlSimCard);

        rlSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getBaseContext(), SnatchedSettingsActivity.class);
                startActivity(i);
            }
        });

        rlCharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BroadcastReceiver receiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        int state = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                        if ((state == BatteryManager.BATTERY_PLUGGED_AC
                                || state == BatteryManager.BATTERY_PLUGGED_USB) && active == false) {
                            Toast.makeText(getApplicationContext(), "Charging and now activating",
                                    Toast.LENGTH_SHORT).show();
                            active = true;
                        }

                        else if ((state == BatteryManager.BATTERY_PLUGGED_AC
                                || state == BatteryManager.BATTERY_PLUGGED_USB) && active == true) {
                            Toast.makeText(getApplicationContext(), "Charging and now deactivating",
                                    Toast.LENGTH_SHORT).show();
                            active = false;
                        }

                        else if (state == 0) {
                            // not charging or on battery power
                            Toast.makeText(getApplicationContext(), "Not charging.",
                                    Toast.LENGTH_SHORT).show();
                            active = false;
                        }
                    }
                };
                IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                registerReceiver(receiver, filter);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
