package com.example.laptop.touchmenot;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;

public class ChangePatternActivity extends AppCompatActivity {

    private Button btnAction, btnCancel;
    private ImageView imgInstruction;
    private boolean isRecording;
    private TextView tvInstruction;
    private PowerButtonReceiver powerReceiver;
//    private final VolumeButtonReceiver volumeReceiver = new VolumeButtonReceiver();
    private IntentFilter powerFilter/*, volumeFilter*/;
    PatternDBOpenHelper pdbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pattern);
        isRecording = false;

        btnAction = (Button) findViewById(R.id.btnPatternAction);
        tvInstruction = (TextView) findViewById(R.id.tvInstruction);
        if(!isRecording){
            btnAction.setText("Record");
            tvInstruction.setText("Tap the Record button to start recording the Button Pattern");
        }else{
            btnAction.setText("Save");
            tvInstruction.setText("Press each button in the order you desire.");
        }
        btnCancel = (Button) findViewById(R.id.btnPatternCancel);
        imgInstruction = (ImageView) findViewById(R.id.imgInstruction);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        powerFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        powerFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        volumeFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
        powerReceiver = new PowerButtonReceiver();
        pdbHelper = new PatternDBOpenHelper(getBaseContext());

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;
                if (!isRecording) {
                    
                        btnAction.setText("Record");
                        tvInstruction.setText("Tap the Record button to start recording the Button Pattern");

                        PackageManager volumePM = v.getContext().getPackageManager();
                        ComponentName volumeComponentName = new ComponentName(ChangePatternActivity.this, VolumeButtonReceiver.class);
                        volumePM.setComponentEnabledSetting(volumeComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);

    //                    PackageManager powerPM = v.getContext().getPackageManager();
    //                    ComponentName powerComponentName = new ComponentName(ChangePatternActivity.this, PowerButtonReceiver.class);
    //                    powerPM.setComponentEnabledSetting(powerComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
    //                            PackageManager.DONT_KILL_APP);
                        unregisterReceiver(powerReceiver);

                        String pattern = "";
                        for(PatternKeys pk : pdbHelper.getPattern()){
                            pattern = pattern + pk.getKey() + " " + pk.getOrder() + ", ";
                        }

                        Toast.makeText(getApplicationContext(), pattern,
                                Toast.LENGTH_SHORT).show();
                        System.out.println(pattern);
                        finish();
                } else {
                    btnAction.setText("Save");
                    tvInstruction.setText("Press each button in the order you desire.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Recording Button Pattern");
                    TextView tvInstruct = new TextView(v.getContext());
                    tvInstruct.setText("Press the Power, Volume Up, and Volume Button in the order you desire. Button pattern should have three(3) to six(6) button presses. \n\n\n  Note: If you press the power button, pressing it again to turn on the screen will not be recorded.");
                    builder.setView(tvInstruct);
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                    VolumeDBOpenHelper vdbHelper = new VolumeDBOpenHelper(getBaseContext());
                    AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int volume = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
                    vdbHelper.newVolume(volume);

                    PackageManager volumePM = v.getContext().getPackageManager();
                    ComponentName volumeComponentName = new ComponentName(ChangePatternActivity.this, VolumeButtonReceiver.class);
                    volumePM.setComponentEnabledSetting(volumeComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);

//                    PackageManager powerPM = v.getContext().getPackageManager();
//                    ComponentName powerComponentName = new ComponentName(ChangePatternActivity.this, PowerButtonReceiver.class);
//                    powerPM.setComponentEnabledSetting(powerComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                            PackageManager.DONT_KILL_APP);
                    registerReceiver(powerReceiver,powerFilter);

                    pdbHelper.clearPattern();
                }
            }
        });

    }
}
