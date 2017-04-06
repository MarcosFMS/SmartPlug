package com.iot.smartplug.smartplug.web;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

/**
 * Created by Marcos on 05/04/2017.
 */

public class SmartplugWebConnection {

    private static SmartplugWebConnection webConnection;

    private MobileServiceClient mClient;

    private SmartplugWebConnection(Context c){
        try {
            mClient = new MobileServiceClient("https://smartplugiot.azurewebsites.net", c);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static SmartplugWebConnection getInstance(Context c){
        if(webConnection == null){
            webConnection = new SmartplugWebConnection(c);
        }
        return webConnection;
    }


}
