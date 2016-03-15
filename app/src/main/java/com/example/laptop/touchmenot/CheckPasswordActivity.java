package com.example.laptop.touchmenot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CheckPasswordActivity extends AppCompatActivity {

    Button btnSubmit, btnCancel;
    PasswordDBOpenHelper pdbhelper;
    EditText etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_password);
        btnSubmit = (Button) findViewById(R.id.btnSubmitCheck);
        btnCancel = (Button) findViewById(R.id.btnCancelCheck);
        pdbhelper = new PasswordDBOpenHelper(getBaseContext());
        etPassword = (EditText) findViewById(R.id.etCheckPassword);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkPass = etPassword.getText().toString();
                String checker = pdbhelper.getPassword(1).getPassword();
                if(checker.equals(checkPass)){
                    Toast.makeText(getApplicationContext(), "Charging and now deactivating",
                            Toast.LENGTH_SHORT).show();
                    MainActivity.active = false;
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Incorrect password",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
