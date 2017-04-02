package com.iot.smartplug.smartplug.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iot.smartplug.smartplug.R;
import com.iot.smartplug.smartplug.activities.AddDeviceActivity;
import com.iot.smartplug.smartplug.dao.DeviceDAO;
import com.iot.smartplug.smartplug.dao.SmartplugDbHelper;
import com.iot.smartplug.smartplug.model.Device;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;


public class DevicesFragment extends Fragment {

    public static final String TAG_FRAGMENT = "devicesFragment";
    private String servidor = "http://192.168.4.1/";
    private int idServidor = 1;
    private boolean servOn = true;
    SmartplugDbHelper dbHelper;

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = SmartplugDbHelper.getInstance(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devices, container, false);
        //Event of the button that adds a new device
        final Button addButton = (Button) view.findViewById(R.id.btn_add_device);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                startActivity(intent);
            }
        });
        loadDeviceList(view);
        return view;


    }

    private void loadDeviceList(View view){

        //Load the list of devices
        List<Device> devices = DeviceDAO.selectAllDevices(dbHelper);
        TableLayout tl = (TableLayout) view.findViewById(R.id.table_devices);
        for (Device d : devices) {

            //Create device name column
            TableRow tr = new TableRow(getActivity());
            tr.setBackgroundColor(getResources().getColor(R.color.table_devices_content_background));
            int padding = (int) getResources().getDimension(R.dimen.table_devices_content_padding);
            tr.setPadding(padding, padding, padding, padding);

            TextView tvNome = new TextView(getActivity());
            tvNome.setText(d.getName());
            tvNome.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
            tvNome.setTextSize(15);
            tr.addView(tvNome);

            //create device wifi ip column
            TextView tvIp = new TextView(getActivity());
            tvIp.setText(d.getIp());
            tvIp.setTextSize(15);
            tvIp.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2f));
            tr.addView(tvIp);

            //create the button
            final Button btnTurnDevice = new Button(getActivity());
            btnTurnDevice.setText("ON");
            btnTurnDevice.setId(d.getId());
            btnTurnDevice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
            btnTurnDevice.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(((Button) v).getText().equals("ON")){
                        ((Button) v).setText("OFF");
                        turnDevice(v.getId(), false);
                    }else{
                        ((Button) v).setText("ON");
                        turnDevice(v.getId(), true);
                    }
                }
            });
            tr.addView(btnTurnDevice);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void turnDevice(int deviceId, boolean on){
        Toast.makeText(getActivity(), deviceId+" is "+on, Toast.LENGTH_LONG);
        Log.d("device status", deviceId+" is "+on);
    }

}