package com.example.laptop.touchmenot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by isabeltm on 3/16/2016.
 */
public class AlarmSettingsActivity extends AppCompatActivity {

    private RelativeLayout rlGracePeriod, rlChooseTone;
    private TextView tvGracePeriod;
    private GracePeriodDBOpenHelper gpdbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_settings);

        gpdbHelper = new GracePeriodDBOpenHelper(this);
        int setTime = gpdbHelper.getGracePeriod();
        if(setTime == 0){
            gpdbHelper.newGracePeriod();
            setTime = 5;
        }
        tvGracePeriod = (TextView)findViewById(R.id.tvGracePeriod);
        tvGracePeriod.setText(setTime + " seconds");

        rlGracePeriod = (RelativeLayout) findViewById(R.id.rlGracePeriod);

        rlGracePeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code from http://stackoverflow.com/questions/10903754/input-text-dialog-android by user "Aaron"
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Enter time in seconds");
                int oldtime = gpdbHelper.getGracePeriod();
                final EditText input = new EditText(v.getContext());
                input.setText(""+oldtime);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String time = input.getText().toString();
                        if (time.matches("^[0-9]+$")) {
                            int interval = Integer.parseInt(time);
                            if (interval == 0)
                                Toast.makeText(getApplicationContext(), "Input cannot be 0. No changes were saved.",
                                        Toast.LENGTH_SHORT).show();
                            else {
                                gpdbHelper.saveGracePeriod(interval);
                                tvGracePeriod.setText(interval + " seconds");
                                Toast.makeText(getApplicationContext(), "Setings saved.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid time input. No changes were saved.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                // end of code from http://stackoverflow.com/questions/10903754/input-text-dialog-android
            }
        });

    }
}
