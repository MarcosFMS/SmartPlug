package com.iot.smartplug.smartplug.view;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.iot.smartplug.smartplug.model.Device;

/**
 * Created by Marcos on 02/04/2017.
 */

@SuppressLint("AppCompatCustomView")
public class TurnDeviceImageButton extends ImageButton {

    private Device device;

    public TurnDeviceImageButton(Context context, Device device) {
        super(context);
        this.device = device;
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



    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
