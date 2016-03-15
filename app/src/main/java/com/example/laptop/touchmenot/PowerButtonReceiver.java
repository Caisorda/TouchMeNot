package com.example.laptop.touchmenot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Laptop on 3/14/2016.
 */
public class PowerButtonReceiver extends BroadcastReceiver {

    private PatternDBOpenHelper pdbHelper;

    public PowerButtonReceiver(){

    }

    public void onReceive(Context context, Intent intent) {
//        System.out.println(intent.getExtras().get("Wow"));
//        int volume = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
//        System.out.println(volume);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            pdbHelper = new PatternDBOpenHelper(context);
            PatternKeys keyPressed = new PatternKeys();
            keyPressed.setOrder(pdbHelper.getButtonPosition()+1);
            keyPressed.setKey(PatternKeys.VALUE_POWER);
            pdbHelper.insertKeyPressed(keyPressed);
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            System.out.println("screen on");
        }
//        if (ke .getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
//            System.out.println("I got volume up event");
//        }else if (ke .getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            System.out.println("I got volume key down event");
//        }
    }
}
