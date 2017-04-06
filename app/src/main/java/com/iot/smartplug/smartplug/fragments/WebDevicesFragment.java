package com.iot.smartplug.smartplug.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.iot.smartplug.smartplug.R;
import com.iot.smartplug.smartplug.activities.AddDeviceActivity;
import com.iot.smartplug.smartplug.activities.AddWebDeviceActivity;
import com.iot.smartplug.smartplug.dao.DeviceDAO;
import com.iot.smartplug.smartplug.dao.SmartplugDbHelper;
import com.iot.smartplug.smartplug.enums.RequestTypes;
import com.iot.smartplug.smartplug.model.Device;
import com.iot.smartplug.smartplug.view.TurnDeviceImageButton;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class WebDevicesFragment extends Fragment {


    private SmartplugDbHelper dbHelper;
    TurnDeviceImageButton imgBtnTurnDeviceAux;
    private MobileServiceClient mClient;
    private List<Device> devices;

    public WebDevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            mClient = new MobileServiceClient("http://smartplug-iot.azurewebsites.net/", getActivity());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        dbHelper = SmartplugDbHelper.getInstance(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_devices, container, false);
        //Event of the button that adds a new device
        final ImageButton addButton = (ImageButton) view.findViewById(R.id.btn_add_device);
        addButton.setScaleType(ImageView.ScaleType.CENTER);
        addButton.setAdjustViewBounds(true);
        addButton.setBackgroundColor(Color.TRANSPARENT);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWebDeviceActivity.class);
                startActivity(intent);
            }
        });
        getDevicesFromAzure(view);
        //loadDeviceList(view);
        return view;
    }

    private void getDevicesFromAzure(final View view) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    devices = mClient.getTable(Device.class).execute().get();

                    for (Device d : devices) {
                        Log.d("for", d.getName());
                    }
                } catch (final Exception e) {
                    Toast.makeText(getActivity(), "Erro ao acessar servidor", Toast.LENGTH_LONG).show();
                    devices = new LinkedList<>();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                loadDeviceList(view);
            }
        }.execute();
    }

    private void loadDeviceList(View view) {

        //Load the list of devices
        TableLayout tl = (TableLayout) view.findViewById(R.id.table_devices);
        for (Device d : devices) {

            //Create device name column
            TableRow tr = new TableRow(getActivity());
            tr.setBackgroundColor(getResources().getColor(R.color.table_devices_content_background));
            int padding = (int) getResources().getDimension(R.dimen.table_devices_content_padding);
            tr.setPadding(padding, padding, padding, padding);

            TextView tvNome = new TextView(getActivity());
            tvNome.setText(d.getName());
            tvNome.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2f));
            tvNome.setTextSize(15);
            tvNome.setTextAppearance(this.getContext(), R.style.Widget_AppCompat_TextView_SpinnerItem);
            tr.addView(tvNome);

            //create the button
            final TurnDeviceImageButton imgBtnTurnDevice = new TurnDeviceImageButton(getActivity(), d);
            imgBtnTurnDevice.setImageResource(R.drawable.button_off);
            imgBtnTurnDevice.setId(d.getId());
            int size = (int) this.getResources().getDimension(R.dimen.dimen_turn_button_in_dp);
            imgBtnTurnDevice.setLayoutParams(new TableRow.LayoutParams(size, size));
            imgBtnTurnDevice.setScaleType(ImageView.ScaleType.CENTER);
            imgBtnTurnDevice.setAdjustViewBounds(true);
            imgBtnTurnDevice.setBackgroundColor(Color.TRANSPARENT);

            imgBtnTurnDevice.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgBtnTurnDeviceAux = (TurnDeviceImageButton) v;
                    if (imgBtnTurnDeviceAux.getDevice().isOn()) {
                        turnDevice(imgBtnTurnDeviceAux.getDevice());
                    } else {
                        turnDevice(imgBtnTurnDeviceAux.getDevice());
                    }
                }
            });
            tr.addView(imgBtnTurnDevice);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void turnDevice(final Device device) {
        final List<Pair<String, String>> parameters = new LinkedList<>();
        parameters.add(new Pair<>("ON", String.valueOf(device.isOn())));
        //update button
        if (imgBtnTurnDeviceAux.getDevice().isOn()) {
            imgBtnTurnDeviceAux.setImageResource(R.drawable.button_off);
            imgBtnTurnDeviceAux.getDevice().setOn(false);
        } else {
            imgBtnTurnDeviceAux.setImageResource(R.drawable.button_on);
            imgBtnTurnDeviceAux.getDevice().setOn(true);
        }

        Log.d("isOn", String.valueOf(device.isOn()));
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Log.d("onBackground", "started");
                try {
                    mClient.getTable(Device.class).update(device, parameters).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("onBackground", "finish");
                return null;
            }
        }.execute();
    }

}
