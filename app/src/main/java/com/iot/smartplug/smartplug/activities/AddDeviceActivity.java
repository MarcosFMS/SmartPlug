package com.iot.smartplug.smartplug.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iot.smartplug.smartplug.MainActivity;
import com.iot.smartplug.smartplug.R;
import com.iot.smartplug.smartplug.dao.DeviceDAO;
import com.iot.smartplug.smartplug.dao.SmartplugDbHelper;
import com.iot.smartplug.smartplug.model.Device;

public class AddDeviceActivity extends AppCompatActivity {



    public static final String TAG_FRAGMENT = "devicesFragment";
    private static final int MY_SOCKET_TIMEOUT_MS = 50000;
    private String deviceIp = "http://192.168.4.1/";
    private int deviceId;
    private String deviceWifiIp;
    private String deviceName;
    private final String RESPONSE_SEPARATOR = ";";
    private SmartplugDbHelper dbHelper;

    PopupWindow popup;
    Button btnFinish;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = SmartplugDbHelper.getInstance(this.getApplicationContext());

        setContentView(R.layout.activity_add_device);
        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Adicionar Dispositivo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup actions of the buttons
        //Event of the button that sends ssid and password to the device
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deviceName = ((EditText) findViewById(R.id.edtxt_name)).getText().toString();
                EditText edtxtSsid = (EditText) findViewById(R.id.edtxt_ssid);
                EditText edtxtPassword = (EditText) findViewById(R.id.edtxt_senha);
                sendWIFIData(edtxtSsid.getText().toString(), edtxtPassword.getText().toString());

                v.setEnabled(false);
            }
        });
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDeviceWifiIp();
            }
        });
        showPopUpWindow();
    }

    public void onResponse(String response, boolean ok) {
        Toast t;
        if (ok) {

            Log.d("Response", "Chegou!");
            deviceId = Integer.parseInt(response);//id of the device
            Device d = new Device(deviceName, deviceId, null, false, null, null);
            addDevice(d);
            t = Toast.makeText(getApplicationContext(), "Response = " + deviceId, Toast.LENGTH_SHORT);
            btnFinish.setVisibility(View.VISIBLE);
        } else {
            Log.d("Response", "Errou!");
            btnRegister.setEnabled(true);
            t = Toast.makeText(getApplicationContext(), "Erro ao enviar dados!", Toast.LENGTH_SHORT);
        }
        Log.d("Response", response);
        t.show();
    }

    public void onWifiIpResponse(String response, boolean ok) {
        Toast t;
        if (ok) {
            deviceWifiIp = response;//ip wifi gets when connected to wifi
            Device d = new Device(deviceName, deviceId, deviceWifiIp, false, null, null);
            updateDevice(d);
            t = Toast.makeText(getApplicationContext(), "Response = " + deviceId, Toast.LENGTH_SHORT);
            reloadDevicesActivity();
        } else {
            t = Toast.makeText(getApplicationContext(), "Erro ao enviar dados!", Toast.LENGTH_SHORT);
        }
        Log.d("Response", response);
        t.show();
    }

    public void reloadDevicesActivity() {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addDevice(Device device) {
        DeviceDAO.insertDevice(dbHelper, device);
    }

    public void updateDevice(Device d) {
        DeviceDAO.updateDevice(dbHelper, d);
    }

    public void sendWIFIData(String ssid, String password) {
        getRequest(ssid, password);
    }

    public void getDeviceWifiIp() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = deviceIp;
        url += "?wifiip=";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AddDeviceActivity.this.onWifiIpResponse(response, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Caso dê erro
                onWifiIpResponse(error.getMessage(), false);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getRequest(String ssid, String password) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = deviceIp;
        url += "?wifidata=" + ssid + "&" + password + "|";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AddDeviceActivity.this.onResponse(response, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Caso dê erro
                onResponse(error.getMessage(), false);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void showPopUpWindow() {
        //Ask the user if they want to quit
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Atenção!")
                .setMessage("Você deve conectar o seu smartphone na rede wifi gerada pelo dispositivo a ser cadastrado para continuar")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();

    }
}
