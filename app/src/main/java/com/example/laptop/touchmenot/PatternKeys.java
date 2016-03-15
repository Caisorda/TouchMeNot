package com.example.laptop.touchmenot;

/**
 * Created by Laptop on 3/14/2016.
 */
public class PatternKeys {
    public static final String TABLE_NAME_VOLUME = "volume";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CURRVOLUME = "currvolume";
    public static final String TABLE_NAME_PATTERN = "pattern";
    public static final String COLUMN_KEY = "key";
    public static final String VALUE_VOLUME_UP = "volumeup";
    public static final String VALUE_VOLUME_DOWN = "volumedown";
    public static final String VALUE_POWER = "power";

    private int order;
    private String key;

    public PatternKeys() {
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
