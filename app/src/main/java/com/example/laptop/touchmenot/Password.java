package com.example.laptop.touchmenot;

/**
 * Created by Bryan on 13/03/2016.
 */
public class Password {

    String password;
    String confirmpassword;
    int id;

    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CONFIRMPASSWORD = "confirmpassword";
    public static final String CONTENT_TABLE_NAME = "password2";
    public static final String COLUMN_ID = "_id";

    public Password(){

    }

    public Password(String password, String confirmpassword) {
        this.password = password;
        this.confirmpassword = confirmpassword;
    }

    public Password(String password, String confirmpassword, int id) {
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public int getID(){
        return  id;
    }

    public void setID(int id){
        this.id = id;
    }
}
