package com.example.laptop.touchmenot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;

public class ChangePatternActivity extends AppCompatActivity {

    public static final String RECORDING_VALUE = "recording";
    public static final String PREFERENCES_NAME = "preference";
    private Button btnAction, btnCancel;
    private ImageButton btnPower;
    private ImageView imgInstruction;
    private SharedPreferences shared;
    private boolean isRecording;
    private TextView tvInstruction;
    private PowerButtonReceiver powerReceiver;
//    private final VolumeButtonReceiver volumeReceiver = new VolumeButtonReceiver();
//    private IntentFilter powerFilter/*, volumeFilter*/;
    private PatternDBOpenHelper pdbHelper;
    private VolumeDBOpenHelper vdbHelper;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        while(activity.getParent() != null) {
            activity = activity.getParent();
        }
        setContentView(R.layout.change_pattern);

        btnPower = (ImageButton) findViewById(R.id.btnChangePower);
        btnPower.setClickable(false);
        btnPower.setVisibility(View.INVISIBLE);
        btnPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = shared.getBoolean(RECORDING_VALUE, false);
                if (isRecording) {
                    int order = pdbHelper.getButtonPosition() + 1;
                    if (order <= 6) {
                        PatternKeys keyPressed = new PatternKeys();
                        keyPressed.setOrder(order);
                        keyPressed.setKey(PatternKeys.VALUE_POWER);
                        pdbHelper.insertKeyPressed(keyPressed);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Button Pattern Limit Exceeded");
                        TextView tvInstruct = new TextView(activity);
                        tvInstruct.setText("Number of buttons pressed exceeded the limit of six(6) buttons. Please tap \"Save\" save the current pattern or press \"Cancel\" to discard changes and go back to the menu.");
                        builder.setView(tvInstruct);
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
        isRecording = false;
        shared = this.getSharedPreferences(PREFERENCES_NAME,MODE_PRIVATE);
        shared.edit().putBoolean(RECORDING_VALUE,isRecording).apply();
        btnAction = (Button) findViewById(R.id.btnPatternAction);
        tvInstruction = (TextView) findViewById(R.id.tvInstruction);
        btnAction.setText("Record");
        tvInstruction.setText("Tap the Record button to start recording the Button Pattern");
        btnCancel = (Button) findViewById(R.id.btnPatternCancel);
        imgInstruction = (ImageView) findViewById(R.id.imgInstruction);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = shared.getBoolean(RECORDING_VALUE,false);
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
//                            unregisterReceiver(powerReceiver);
//                            PackageManager volumePM = view.getContext().getPackageManager();
//                            ComponentName volumeComponentName = new ComponentName(ChangePatternActivity.this, VolumeButtonReceiver.class);
//                            volumePM.setComponentEnabledSetting(volumeComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                                    PackageManager.DONT_KILL_APP);
                            shared.edit().putBoolean(RECORDING_VALUE,false).apply();
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

//        powerFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        powerFilter.addAction(Intent.ACTION_SCREEN_OFF);
////        volumeFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
//        powerReceiver = new PowerButtonReceiver();
        pdbHelper = new PatternDBOpenHelper(getBaseContext());

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = shared.getBoolean(RECORDING_VALUE,false);
                if (isRecording) {
                    if(pdbHelper.getButtonPosition() >= 3) {
                        isRecording = false;
                        btnPower.setClickable(false);
                        btnPower.setVisibility(View.INVISIBLE);
                        shared.edit().putBoolean(RECORDING_VALUE,isRecording).apply();
                        btnAction.setText("Record");
                        tvInstruction.setText("Tap the Record button to start recording the Button Pattern");

//                        PackageManager volumePM = v.getContext().getPackageManager();
//                        ComponentName volumeComponentName = new ComponentName(ChangePatternActivity.this, VolumeButtonReceiver.class);
//                        volumePM.setComponentEnabledSetting(volumeComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                                PackageManager.DONT_KILL_APP);
//
//                        //                    PackageManager powerPM = v.getContext().getPackageManager();
//                        //                    ComponentName powerComponentName = new ComponentName(ChangePatternActivity.this, PowerButtonReceiver.class);
//                        //                    powerPM.setComponentEnabledSetting(powerComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                        //                            PackageManager.DONT_KILL_APP);
//                        unregisterReceiver(powerReceiver);

                        String pattern = "";
                        for (PatternKeys pk : pdbHelper.getPattern()) {
                            pattern = pattern + pk.getKey() + " " + pk.getOrder() + ", ";
                        }

                        Toast.makeText(getApplicationContext(), pattern,
                                Toast.LENGTH_SHORT).show();
                        System.out.println(pattern);
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
                    isRecording = true;
                    btnPower.setClickable(true);
                    btnPower.setVisibility(View.VISIBLE);
                    shared.edit().putBoolean(RECORDING_VALUE,isRecording).apply();
                    btnAction.setText("Save");
                    tvInstruction.setText("Press each button in the order you desire.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Recording Button Pattern");
                    TextView tvInstruct = new TextView(v.getContext());
                    tvInstruct.setText("Press the Power, Volume Up, and Volume Down buttons in the order you desire. Button pattern should have three(3) to six(6) button presses. \n\n\nNote: The Power Button presented on screen will act as the phone's Power Button for recording purposes");
                    builder.setView(tvInstruct);
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

//                    VolumeDBOpenHelper vdbHelper = new VolumeDBOpenHelper(getBaseContext());
//                    AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                    int volume = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
//                    vdbHelper.newVolume(volume);
//
//                    PackageManager volumePM = v.getContext().getPackageManager();
//                    ComponentName volumeComponentName = new ComponentName(ChangePatternActivity.this, VolumeButtonReceiver.class);
//                    volumePM.setComponentEnabledSetting(volumeComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                            PackageManager.DONT_KILL_APP);
//
////                    PackageManager powerPM = v.getContext().getPackageManager();
////                    ComponentName powerComponentName = new ComponentName(ChangePatternActivity.this, PowerButtonReceiver.class);
////                    powerPM.setComponentEnabledSetting(powerComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
////                            PackageManager.DONT_KILL_APP);
//                    registerReceiver(powerReceiver,powerFilter);

                    pdbHelper.clearPattern();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(event.getKeyCode() + " " + KeyEvent.KEYCODE_POWER);
        isRecording = shared.getBoolean(RECORDING_VALUE,false);
        if(isRecording) {
//            if (keyCode == KeyEvent.KEYCODE_POWER) {
//                int order = pdbHelper.getButtonPosition() + 1;
//                if (order <= 6) {
//                    PatternKeys keyPressed = new PatternKeys();
//                    keyPressed.setOrder(order);
//                    keyPressed.setKey(PatternKeys.VALUE_POWER);
//                    pdbHelper.insertKeyPressed(keyPressed);
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                    builder.setTitle("Button Pattern Limit Exceeded");
//                    TextView tvInstruct = new TextView(getBaseContext());
//                    tvInstruct.setText("Number of buttons pressed exceeded the limit of six(6) buttons. Please tap \"Save\" save the current pattern or press \"Cancel\" to discard changes and go back to the menu.");
//                    builder.setView(tvInstruct);
//                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    builder.show();
//                }
//                Toast.makeText(getApplicationContext(), "Power",
//                        Toast.LENGTH_SHORT).show();
//            } else if (keyCode == KeyEvent.KEYCODE_WAKEUP) {
//                return super.onKeyDown(keyCode, event);
//            } else
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                int order = pdbHelper.getButtonPosition() + 1;
                if (order <= 6) {
                    PatternKeys keyPressed = new PatternKeys();
                    keyPressed.setOrder(order);
                    keyPressed.setKey(PatternKeys.VALUE_VOLUME_DOWN);
                    pdbHelper.insertKeyPressed(keyPressed);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                    builder.setTitle("Button Pattern Limit Exceeded");
                    TextView tvInstruct = new TextView(getBaseContext());
                    tvInstruct.setText("Number of buttons pressed exceeded the limit of six(6) buttons. Please tap \"Save\" save the current pattern or press \"Cancel\" to discard changes and go back to the menu.");
                    builder.setView(tvInstruct);
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
//                    Toast.makeText(getApplicationContext(), "Number of buttons pressed exceeded the limit of six(6) buttons. Please tap \"Save\" save the current pattern or press \"Cancel\" to discard changes and go back to the menu.",
//                        Toast.LENGTH_SHORT).show();
                }
//
//                Toast.makeText(getApplicationContext(), "Volume Down",
//                        Toast.LENGTH_SHORT).show();
            }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
                int order = pdbHelper.getButtonPosition() + 1;
                if (order <= 6) {
                    PatternKeys keyPressed = new PatternKeys();
                    keyPressed.setOrder(order);
                    keyPressed.setKey(PatternKeys.VALUE_VOLUME_UP);
                    pdbHelper.insertKeyPressed(keyPressed);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Button Pattern Limit Exceeded");
                    TextView tvInstruct = new TextView(activity);
                    tvInstruct.setText("Number of buttons pressed exceeded the limit of six(6) buttons. Please tap \"Save\" save the current pattern or press \"Cancel\" to discard changes and go back to the menu.");
                    builder.setView(tvInstruct);
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
//                    Toast.makeText(getApplicationContext(), "Number of buttons pressed exceeded the limit of six(6) buttons. Please tap \"Save\" save the current pattern or press \"Cancel\" to discard changes and go back to the menu.",
//                            Toast.LENGTH_SHORT).show();
                }
//
//                Toast.makeText(getApplicationContext(), "Volume Up",
//                        Toast.LENGTH_SHORT).show();
            }

        }
        if(keyCode == KeyEvent.KEYCODE_BACK){
            pdbHelper.clearPattern();
            isRecording = false;
            isRecording = shared.getBoolean(RECORDING_VALUE,false);
            Toast.makeText(getApplicationContext(), "Back pressed",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
