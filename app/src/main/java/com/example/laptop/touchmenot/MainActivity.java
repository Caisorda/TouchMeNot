package com.example.laptop.touchmenot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rlCharger, rlMotion, rlSim, rlSettings, rlDetectMode, rlAlarmSettings;
//    public static boolean active = false;
    ToggleButton tglbtnDetectMode;
    public static final String DETECTING_VALUE = "detecting";
    public static boolean chargerActive;
    private ChargerDetectionReceiver chargerReceiver;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = this.getSharedPreferences(ChangePatternActivity.PREFERENCES_NAME,MODE_PRIVATE);
        tglbtnDetectMode = (ToggleButton) findViewById(R.id.tglbtnDetectMode);
        tglbtnDetectMode.setChecked(preferences.getBoolean(DETECTING_VALUE,false));
        tglbtnDetectMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    preferences.edit().putBoolean(DETECTING_VALUE,true).apply();
                    Toast.makeText(getApplicationContext(), "Now detecting for Button Pattern press",
                            Toast.LENGTH_SHORT).show();
                }else{
                    preferences.edit().putBoolean(DETECTING_VALUE,false).apply();
                    Toast.makeText(getApplicationContext(), "No longer detecting for Button Pattern press",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        chargerActive = false;
        rlDetectMode = (RelativeLayout) findViewById(R.id.rlDetectMode);
        rlSettings = (RelativeLayout) findViewById(R.id.rlSettings);
        rlCharger = (RelativeLayout) findViewById(R.id.rlCharger);
        rlMotion = (RelativeLayout) findViewById(R.id.rlMotion);
        rlSim = (RelativeLayout) findViewById(R.id.rlSimCard);
        rlAlarmSettings = (RelativeLayout) findViewById(R.id.rlAlarmSettings);

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
                if (!chargerActive) {
                    System.out.println("not charger active");
                    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    chargerReceiver = new ChargerDetectionReceiver();
                    registerReceiver(chargerReceiver, filter);
                } else {
                    Intent i = new Intent();
                    i.setClass(view.getContext(), CheckPasswordActivity.class);
                    if (!chargerActive)
                        unregisterReceiver(chargerReceiver);
                }
            }
        });

        rlAlarmSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getBaseContext(), AlarmSettingsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
