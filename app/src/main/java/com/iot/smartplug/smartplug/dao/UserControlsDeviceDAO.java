package com.iot.smartplug.smartplug.dao;

/**
 * Created by Marcos on 26/03/2017.
 */

public class UserControlsDeviceDAO {

    private static final String TABLE_NAME = "USER_CONTROLS_DEVICE";

    public static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME +"("+
                    "ID_USER INT NOT NULL" +
                    " CONSTRAINT ID_USER REFERENCES USER (ID)," +
                    "ID_DEVICE INT NOT NULL" +
                    " CONSTRAINT ID_DEVICE REFERENCES DEVICE (ID)," +
                    "LAST_COMMAND BIT," +
                    "LAST_COMMAND_TIME DATETIME," +
                    "PRIMARY KEY(ID_USER, ID_DEVICE));";


    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS "+TABLE_NAME;
}
