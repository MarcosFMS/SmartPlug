package com.iot.smartplug.smartplug.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Marcos on 02/04/2017.
 */

@SuppressLint("AppCompatCustomView")
public class TurnDeviceImageButton extends ImageButton {

    private int deviceId;
    private String deviceIp;
    private int on;

    public TurnDeviceImageButton(Context context, int deviceId, String deviceIp, int on) {
        super(context);
        this.deviceId = deviceId;
        this.deviceIp = deviceIp;
        this.on = on;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public int getOn() {
        return on;
    }

    public void setOn(int on) {
        this.on = on;
    }

    public TurnDeviceImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TurnDeviceImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TurnDeviceImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



}
