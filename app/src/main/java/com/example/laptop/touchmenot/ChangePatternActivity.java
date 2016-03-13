package com.example.laptop.touchmenot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangePatternActivity extends AppCompatActivity {

    private Button btnAction, btnCancel;
    private ImageView imgInstruction;
    private boolean isRecording;
    private TextView tvInstruction;

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

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;
                if(!isRecording){
                    btnAction.setText("Record");
                    tvInstruction.setText("Tap the Record button to start recording the Button Pattern");
                }else{
                    btnAction.setText("Save");
                    tvInstruction.setText("Press each button in the order you desire.");
                }
            }
        });

    }
}
