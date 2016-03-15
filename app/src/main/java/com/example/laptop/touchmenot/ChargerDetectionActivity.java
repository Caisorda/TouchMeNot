package com.example.laptop.touchmenot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ChargerDetectionActivity extends AppCompatActivity {

    int state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_detection);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                state = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                //boolean active = getIntent().getExtras().getBoolean("active");
                if ((state == BatteryManager.BATTERY_PLUGGED_AC
                        || state == BatteryManager.BATTERY_PLUGGED_USB) && MainActivity.active == false) {
                    if(ChangePasswordActivity.status == true){
                        Toast.makeText(getApplicationContext(), "Charging and now activating",
                                Toast.LENGTH_SHORT).show();
                        MainActivity.active = true;
                        finish();

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Set password first",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                else if ((state == BatteryManager.BATTERY_PLUGGED_AC
                        || state == BatteryManager.BATTERY_PLUGGED_USB) && MainActivity.active == true) {

                    Intent i = new Intent();
                    i.setClass(getBaseContext(), CheckPasswordActivity.class);
                    startActivity(i);
                    /*Toast.makeText(getApplicationContext(), "Charging and now deactivating",
                            Toast.LENGTH_SHORT).show();
                    MainActivity.active = false;
                    finish();*/
                    finish();
                }

                else if (state == 0) {
                    // not charging or on battery power
                    Toast.makeText(getApplicationContext(), "Not charging.",
                            Toast.LENGTH_SHORT).show();
                    MainActivity.active = false;
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

    }


}
