package com.example.laptop.touchmenot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    Button btnOkay, btnCancel;
    EditText etPassword, etConfirmPassword;
    PasswordDBOpenHelper pdbo;
    Password pass;
    public static boolean status = false;
    public static boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        pdbo = new PasswordDBOpenHelper(getBaseContext());
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        pass = new Password();
        btnOkay = (Button) findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if((!password.equals("") && !confirmPassword.equals("")) && password.equals(confirmPassword) && first == true){
                    status = true;
                    first = false;
                    pass.setPassword(password);
                    pass.setConfirmpassword(confirmPassword);
                    pdbo.insertPassword(pass);
                    //pdbo.savePassword(pass);
                    Log.w("Password", password);
                    Log.w("Confirm", confirmPassword);
                    System.out.println(password);
                    System.out.println(confirmPassword);
                    finish();
                    Toast.makeText(getApplicationContext(), "Password is now set",
                            Toast.LENGTH_SHORT).show();
                }
                else if((!password.equals("") && !confirmPassword.equals("")) && password.equals(confirmPassword) && first == false){
                    status = true;
                    pass.setPassword(password);
                    pass.setConfirmpassword(confirmPassword);
                    pdbo.savePassword(pass);
                    Log.w("Password", password);
                    Log.w("Confirm", confirmPassword);
                    System.out.println(password);
                    System.out.println(confirmPassword);
                    finish();
                    Toast.makeText(getApplicationContext(), "Password is now set",
                            Toast.LENGTH_SHORT).show();
                }
                else if(password.equals("") || confirmPassword.equals("")){
                    Toast.makeText(getApplicationContext(), "Fill necessary fields",
                            Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(), "Passwords mismatch",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
