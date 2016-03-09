package com.example.laptop.touchmenot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class SnatchedSettingsActivity extends AppCompatActivity {

    private RelativeLayout rlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snatched_settings);

        rlContent = (RelativeLayout) findViewById(R.id.rlContent);

        rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getBaseContext(), ChangeMessageActivity.class);
                startActivity(i);
            }
        });
    }
}
