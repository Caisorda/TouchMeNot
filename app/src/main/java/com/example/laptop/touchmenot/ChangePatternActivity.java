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
                if(isRecording){
                    final View view = v;
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Discard Changes");
                    TextView tvInstruct = new TextView(v.getContext());
                    tvInstruct.setText("Cancel button pattern recording and go back to settings menu?");
                    builder.setView(tvInstruct);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unregisterReceiver(powerReceiver);
                            PackageManager volumePM = view.getContext().getPackageManager();
                            ComponentName volumeComponentName = new ComponentName(ChangePatternActivity.this, VolumeButtonReceiver.class);
                            volumePM.setComponentEnabledSetting(volumeComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);
                            pdbHelper.clearPattern();
                            dialog.cancel();
                            finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else{
                    finish();
                }
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
                if (isRecording) {
                    if(pdbHelper.getButtonPosition() >= 3) {
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
                        for (PatternKeys pk : pdbHelper.getPattern()) {
                            pattern = pattern + pk.getKey() + " " + pk.getOrder() + ", ";
                        }

                        Toast.makeText(getApplicationContext(), pattern,
                                Toast.LENGTH_SHORT).show();
                        System.out.println(pattern);
                        isRecording = !isRecording;
                        finish();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Not enough buttons");
                        TextView tvInstruct = new TextView(v.getContext());
                        tvInstruct.setText("Less than three (3) buttons were pressed. Please press more buttons.");
                        builder.setView(tvInstruct);
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                } else {
                    isRecording = !isRecording;
                    btnAction.setText("Save");
                    tvInstruction.setText("Press each button in the order you desire.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Recording Button Pattern");
                    TextView tvInstruct = new TextView(v.getContext());
                    tvInstruct.setText("Press the Power, Volume Up, and Volume Down buttons in the order you desire. Button pattern should have three(3) to six(6) button presses. \n\n\nNote: If you press the power button, pressing it again to turn on the screen will not be recorded.");
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
