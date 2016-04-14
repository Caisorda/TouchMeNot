package com.example.laptop.touchmenot;


import java.util.Date;

/**
 * Created by Laptop on 4/6/2016.
 */
public class PressedButton {
    public static final String TABLE_NAME = "pressed";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BUTTON = "button";
    public static final String COLUMN_TIME = "time";
    public static final String DETECTING_PREFERENCE = "detect";
    private String button;
    private long timePressed;
    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public long getTimePressed() {
        return timePressed;
    }

    public void setTimePressed(long timePressed) {
        this.timePressed = timePressed;
    }
}
