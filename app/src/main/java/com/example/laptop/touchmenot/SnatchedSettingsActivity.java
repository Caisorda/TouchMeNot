package com.example.laptop.touchmenot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SnatchedSettingsActivity extends AppCompatActivity {

    private RelativeLayout rlContent, rlInterval, rlPatternSetting;
    private TextView tvInterval;
    private IntervalDBOpenHelper idbHelper;
    private ToggleButton tglbtnDetectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snatched_settings);

        idbHelper = new IntervalDBOpenHelper(this);
        int setTime = idbHelper.getInterval();
        if(setTime == 0){
            idbHelper.newInterval();
            setTime = 5;
        }
        tvInterval = (TextView)findViewById(R.id.tvInterval);
        tvInterval.setText(setTime + " seconds");

        rlContent = (RelativeLayout) findViewById(R.id.rlContent);

        rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getBaseContext(), ChangeMessageActivity.class);
                startActivity(i);
            }
        });

        rlInterval = (RelativeLayout) findViewById(R.id.rlInterval);

        rlInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code from http://stackoverflow.com/questions/10903754/input-text-dialog-android by user "Aaron"
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Enter time in seconds");
                int oldtime = idbHelper.getInterval();
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
                                idbHelper.saveInterval(interval);
                                tvInterval.setText(interval + " seconds");
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

        tglbtnDetectMode = (ToggleButton) findViewById(R.id.tglbtnDetectMode);
        rlPatternSetting = (RelativeLayout) findViewById(R.id.rlPatternSetting);
        rlPatternSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getBaseContext(), ChangePatternActivity.class);
                startActivity(i);
            }
        });

    }
}
