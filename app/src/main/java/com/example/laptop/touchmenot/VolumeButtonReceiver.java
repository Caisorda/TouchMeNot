package com.example.laptop.touchmenot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by Laptop on 3/14/2016.
 */
public class VolumeButtonReceiver extends BroadcastReceiver {

    private VolumeDBOpenHelper vdbHelper;
    private PatternDBOpenHelper pdbHelper;

    public VolumeButtonReceiver(){

    }

    public void onReceive(Context context, Intent intent) {
        vdbHelper = new VolumeDBOpenHelper(context);
        pdbHelper = new PatternDBOpenHelper(context);
        int prevVolume = vdbHelper.getCurrentVolume();
        int volume = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        PatternKeys keyPressed = new PatternKeys();
        keyPressed.setOrder(pdbHelper.getButtonPosition()+1);
        if(prevVolume > volume || (volume == 0 && prevVolume == volume)){
            keyPressed.setKey(PatternKeys.VALUE_VOLUME_DOWN);
            pdbHelper.insertKeyPressed(keyPressed);
        }else if(prevVolume < volume || (volume == mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) && prevVolume == volume)){
            keyPressed.setKey(PatternKeys.VALUE_VOLUME_UP);
            pdbHelper.insertKeyPressed(keyPressed);
        }
        vdbHelper.saveCurrentVolume(volume);
    }
}
