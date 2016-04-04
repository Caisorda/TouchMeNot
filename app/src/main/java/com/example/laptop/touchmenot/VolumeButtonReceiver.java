package com.example.laptop.touchmenot;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.widget.TextView;

/**
 * Created by Laptop on 3/14/2016.
 */
public class VolumeButtonReceiver extends BroadcastReceiver {

    private VolumeDBOpenHelper vdbHelper;
    private PatternDBOpenHelper pdbHelper;
    private SharedPreferences shared;
    public VolumeButtonReceiver(){
    }

    public void onReceive(Context context, Intent intent) {
        shared = context.getSharedPreferences(ChangePatternActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
        vdbHelper = new VolumeDBOpenHelper(context);
        pdbHelper = new PatternDBOpenHelper(context);
        int order = pdbHelper.getButtonPosition()+1;
        if(!shared.getBoolean(ChangePatternActivity.RECORDING_VALUE,false) && shared.getBoolean(MainActivity.DETECTING_VALUE,false)) {
            if (order <= 6) {
                int prevVolume = vdbHelper.getCurrentVolume();
                int volume = (Integer) intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
                AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                PatternKeys keyPressed = new PatternKeys();
                keyPressed.setOrder(order);
                if (prevVolume > volume || (volume == 0 && prevVolume == volume)) {
                    keyPressed.setKey(PatternKeys.VALUE_VOLUME_DOWN);
                    pdbHelper.insertKeyPressed(keyPressed);
                } else if (prevVolume < volume || (volume == mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) && prevVolume == volume)) {
                    keyPressed.setKey(PatternKeys.VALUE_VOLUME_UP);
                    pdbHelper.insertKeyPressed(keyPressed);
                }
                vdbHelper.saveCurrentVolume(volume);
            } else {
                //            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //            builder.setTitle("Button Pattern Limit Exceeded");
                //            TextView tvInstruct = new TextView(context);
                //            tvInstruct.setText("Number of buttons pressed exceeded the limit of six(6) buttons. Please tap \"Save\" save the current pattern or press \"Cancel\" to discard changes and go back to the menu.");
                //            builder.setView(tvInstruct);
                //            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                //                @Override
                //                public void onClick(DialogInterface dialog, int which) {
                //                    dialog.cancel();
                //                }
                //            });
                //            builder.show();
            }
        }
    }
}
