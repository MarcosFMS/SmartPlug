package com.iot.smartplug.smartplug.dao;

/**
 * Created by Marcos on 26/03/2017.
 */

public class UserDAO {

    private static final String TABLE_NAME = "USER";

    public static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME +"("+
                    "ID INT PRIMARY KEY NOT NULL," +
                    "NAME VARCHAR(50));";


    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS "+TABLE_NAME;

}
