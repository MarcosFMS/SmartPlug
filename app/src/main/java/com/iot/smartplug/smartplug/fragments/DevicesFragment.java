package com.iot.smartplug.smartplug.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.iot.smartplug.smartplug.enums.RequestTypes;
import com.iot.smartplug.smartplug.model.Device;
import com.iot.smartplug.smartplug.view.TurnDeviceImageButton;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;


public class DevicesFragment extends Fragment {

    /*
    public static final String TAG_FRAGMENT = "devicesFragment";
    private String servidor = "http://192.168.4.1/";
    private int idServidor = 1;
    private boolean servOn = true;*/
    SmartplugDbHelper dbHelper;
    TurnDeviceImageButton imgBtnTurnDeviceAux;

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
        final FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.btn_add_device);
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
        /*List<Device> devices = new LinkedList<>();
        devices.add(new Device("Cafeteira", 1, null, false));
        devices.add(new Device("Geladeira", 2, null, false));
        devices.add(new Device("Televisão", 3, null, false));
        devices.add(new Device("Torradeira", 4, null, false));*/
        TableLayout tl = (TableLayout) view.findViewById(R.id.table_devices);
        for (Device d : devices) {

            //Create device name column
            TableRow tr = new TableRow(getActivity());
            tr.setBackgroundColor(getResources().getColor(R.color.table_devices_content_background));
            int padding = (int) getResources().getDimension(R.dimen.table_devices_content_padding);
            tr.setPadding(padding, padding, padding, padding);

            TextView tvNome = new TextView(getActivity());
            tvNome.setText(d.getName());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2f);
            lp.topMargin = 50;
            tvNome.setLayoutParams(lp);
            tvNome.setTextSize(15);
            tvNome.setTextAppearance(this.getContext(), R.style.Widget_AppCompat_TextView_SpinnerItem);

            tr.addView(tvNome);

            //create the button
            final TurnDeviceImageButton imgBtnTurnDevice = new TurnDeviceImageButton(getActivity(), d);
            imgBtnTurnDevice.setImageResource(R.drawable.turn_off);
            imgBtnTurnDevice.setId(d.getId());
            int size = (int) this.getResources().getDimension(R.dimen.dimen_turn_button_in_dp);
            imgBtnTurnDevice.setLayoutParams(new TableRow.LayoutParams(size, size));
            imgBtnTurnDevice.setScaleType(ImageView.ScaleType.CENTER);
            imgBtnTurnDevice.setAdjustViewBounds(true);
            imgBtnTurnDevice.setBackgroundColor(Color.TRANSPARENT);

            imgBtnTurnDevice.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnTurnDeviceAux = (TurnDeviceImageButton) v;
                    if(imgBtnTurnDeviceAux.getDevice().isOn()){
                        turnDevice(v.getId(), false);
                    }else{
                        turnDevice(v.getId(), true);
                    }
                }
            });
            tr.addView(imgBtnTurnDevice);

            View hLine = new View(getActivity());
            hLine.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
            hLine.setBackgroundColor(Color.BLACK);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tl.addView(hLine);
        }
    }

    public void turnDevice(int deviceId, boolean on){
        Log.d("device status", deviceId+" is "+on);
        Device device = new Device();
        device.setIp(DeviceDAO.getIp(dbHelper, deviceId));
        device.setOn(on);
        device.setId(deviceId);
        getRequest(RequestTypes.GET_TURN_DEVICE, device);
    }

    public void onTurnResponse(String response, boolean ok){
        Log.d("turndevice", response);
        if(ok){
            if(imgBtnTurnDeviceAux.getDevice().isOn()){
                imgBtnTurnDeviceAux.setImageResource(R.drawable.turn_off);
                imgBtnTurnDeviceAux.getDevice().setOn(false);
            }else{
                imgBtnTurnDeviceAux.setImageResource(R.drawable.turn_on);
                imgBtnTurnDeviceAux.getDevice().setOn(true);
            }
        }else{
            Toast.makeText(getActivity(), "Erro de comunicação com dispositivo", Toast.LENGTH_LONG).show();
        }
    }

    public void getRequest(RequestTypes requestType, Device device) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        if(requestType == RequestTypes.GET_TURN_DEVICE) {
            String url = "http://"+device.getIp();
            int on = (device.isOn()) ? 1 : 0;
            url += "/?turndevice=" + on + "|";
            Log.d("turn",url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    onTurnResponse(response, true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Caso dê erro
                    Log.d("error", error.getMessage());
                    onTurnResponse(error.getMessage(), false);
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
}