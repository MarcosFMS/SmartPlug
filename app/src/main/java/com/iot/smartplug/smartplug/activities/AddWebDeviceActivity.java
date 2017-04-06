package com.iot.smartplug.smartplug.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.iot.smartplug.smartplug.MainActivity;
import com.iot.smartplug.smartplug.R;
import com.iot.smartplug.smartplug.dao.DeviceDAO;
import com.iot.smartplug.smartplug.dao.SmartplugDbHelper;
import com.iot.smartplug.smartplug.model.Device;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

public class AddWebDeviceActivity extends AppCompatActivity {

    SmartplugDbHelper dbHelper;
    int selectedId;

    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_web_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = SmartplugDbHelper.getInstance(this.getApplicationContext());

        loadSpinnerDevices();

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Device d = DeviceDAO.selectDevice(dbHelper, selectedId);
                try {
                    mClient = new MobileServiceClient("http://smartplug-iot.azurewebsites.net/", getApplicationContext());

                    mClient.getTable(Device.class).insert(d, new TableOperationCallback<Device>() {
                        public void onCompleted(Device entity, Exception exception, ServiceFilterResponse response) {
                            if (exception == null) {
                                Log.d("Azure", "Worked");
                            } else {
                                exception.printStackTrace();
                            }
                        }
                    });
                    reloadWebDevicesActivity();
                } catch (MalformedURLException e) {
                    Toast.makeText(getApplicationContext(), "Erro ao acessar servidor", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
    public void reloadWebDevicesActivity() {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    private void loadSpinnerDevices() {
        List<Device> devices = DeviceDAO.selectAllDevices(dbHelper);
        final Spinner spDevices = (Spinner) this.findViewById(R.id.sp_devices);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Device> dataAdapter = new ArrayAdapter<Device>(this.getApplicationContext(), android.R.layout.simple_spinner_item, devices);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spDevices.setAdapter(dataAdapter);
        spDevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Device d = (Device) spDevices.getItemAtPosition(position);
                selectedId = d.getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

}
