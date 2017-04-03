package com.iot.smartplug.smartplug.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.iot.smartplug.smartplug.model.Device;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marcos on 26/03/2017.
 */

public class DeviceDAO {

    private static final String TABLE_NAME = "DEVICE";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    "ID INT PRIMARY KEY NOT NULL," +
                    "NAME VARCHAR(50)," +
                    "IS_ON BIT NOT NULL," +
                    "LAST_TIMEOFF DATETIME," +
                    "IP VARCHAR(11)," +
                    "LAST_TIMEON DATETIME);";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static List<Device> selectAllDevices(SmartplugDbHelper dbHelper) {
        LinkedList<Device> devices = new LinkedList<Device>();

        //setup the query to be executed
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery.append("SELECT * FROM DEVICE");

        Cursor cursor = dbHelper.getWritableDatabase().rawQuery(stringBuilderQuery.toString(), null);
        cursor.getColumnNames();
        /*Position the cursor on the first register*/
        cursor.moveToFirst();
        cursor.getColumnName(0);


        Device device;

        //Reads until the cursor reaches the final register
        while (!cursor.isAfterLast()) {
            device = new Device();

            device.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            device.setName(cursor.getString(cursor.getColumnIndex("NAME")));
            device.setIp(cursor.getString(cursor.getColumnIndex("IP")));
            try {
                device.setLastTimeoff(stringToCalendar(cursor.getString(cursor.getColumnIndex("LAST_TIMEOFF"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            device.setOn(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("IS_ON"))));

            devices.add(device);

            cursor.moveToNext();
        }
        return devices;
    }

    public static void insertDevice(SmartplugDbHelper dbHelper, Device device) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", device.getName());
        contentValues.put("id", device.getId());
        contentValues.put("ip", device.getIp());
        contentValues.put("last_timeoff", calendarToString(device.getLastTimeoff()));
        contentValues.put("is_on", String.valueOf(device.isOn()));
        dbHelper.getWritableDatabase().insert("DEVICE", null, contentValues);

    }

    public static void updateDevice(SmartplugDbHelper dbHelper, Device device) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", device.getName());
        contentValues.put("id", device.getId());
        contentValues.put("ip", device.getIp());
        contentValues.put("last_timeoff", calendarToString(device.getLastTimeoff()));
        contentValues.put("is_on", String.valueOf(device.isOn()));
        dbHelper.getWritableDatabase().update("DEVICE", contentValues, "id = ?", new String[]{String.valueOf(device.getId())});
    }

    public static Calendar stringToCalendar(String date) throws ParseException {
        if (date != null) {
            String DEFAULT_LOCALE_NAME = "pt";
            String DEFAULT_COUNTRY = "BR";
            Locale DEFAULT_LOCALE = new Locale(DEFAULT_LOCALE_NAME, DEFAULT_COUNTRY);
            SimpleDateFormat format = new SimpleDateFormat("DD/MM/YYYY HH:MM", DEFAULT_LOCALE);
            Date d = format.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c;
        }
        return null;
    }

    public static String getIp(SmartplugDbHelper dbHelper, int id) {

        //setup the query to be executed
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery.append("SELECT IP FROM DEVICE WHERE ID = " + id);

        Cursor cursor = dbHelper.getWritableDatabase().rawQuery(stringBuilderQuery.toString(), null);
        /*Position the cursor on the first register*/
        cursor.moveToFirst();
        String ip;

        //Reads until the cursor reaches the final register
        ip = cursor.getString(cursor.getColumnIndex("IP"));

        return ip;
}

    public static String calendarToString(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            String a = s.format(calendar.getTime());
            return a;
        }
        return null;
    }
}
