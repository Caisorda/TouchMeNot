package com.example.laptop.touchmenot;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Laptop on 3/14/2016.
 */
public class PowerButtonReceiver extends BroadcastReceiver {

    private PatternDBOpenHelper pdbHelper;
    private SharedPreferences shared;

    public PowerButtonReceiver(){

    }

    public void onReceive(Context context, Intent intent) {
//        System.out.println(intent.getExtras().get("Wow"));
//        int volume = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
//        System.out.println(volume);
        shared = context.getSharedPreferences(ChangePatternActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
        pdbHelper = new PatternDBOpenHelper(context);
        int order = pdbHelper.getButtonPosition()+1;
        if(!shared.getBoolean(ChangePatternActivity.RECORDING_VALUE,false) && shared.getBoolean(MainActivity.DETECTING_VALUE,false)) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (order <= 6) {
                    PatternKeys keyPressed = new PatternKeys();
                    keyPressed.setOrder(order);
                    keyPressed.setKey(PatternKeys.VALUE_POWER);
                    pdbHelper.insertKeyPressed(keyPressed);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Button Pattern Limit Exceeded");
                    TextView tvInstruct = new TextView(context);
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
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                System.out.println("screen on");
            }
            //        if (ke .getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            //            System.out.println("I got volume up event");
            //        }else if (ke .getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //            System.out.println("I got volume key down event");
            //        }
        }
    }
}
