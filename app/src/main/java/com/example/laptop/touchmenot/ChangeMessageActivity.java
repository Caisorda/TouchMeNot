package com.example.laptop.touchmenot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeMessageActivity extends AppCompatActivity {

    private Button btnSave, btnCancel;
    private EditText etMessage;
    private MessageDBOpenHelper mdboHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_message);

        btnSave = (Button) findViewById(R.id.btnEditMsgSave);
        btnCancel = (Button) findViewById(R.id.btnEditMsgCancel);
        etMessage = (EditText) findViewById(R.id.etEditMessage);
        
        mdboHelper = new MessageDBOpenHelper(getBaseContext());
        String message;
        message = mdboHelper.getMessage();
        if(message.equals("")) {
            mdboHelper.newMessage();
            message = mdboHelper.getMessage();
        }
        etMessage.setText(message);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mdboHelper.saveMessage(etMessage.getText().toString());
                finish();
            }
        });
    }
}
