package com.iot.smartplug.smartplug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.iot.smartplug.smartplug.adapters.ViewPagerAdapter;
import com.iot.smartplug.smartplug.fragments.DevicesFragment;
import com.iot.smartplug.smartplug.fragments.ThreeFragment;
import com.iot.smartplug.smartplug.fragments.WebDevicesFragment;


public class MainActivity extends AppCompatActivity {

    //Layout items

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //private MobileServiceClient mClient;

    private String servidor = "http://192.168.4.1/";
    private String ip = "";//ip address of the device when connected to wifi

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupMenuBar();

        /*
        try {
            mClient = new MobileServiceClient("https://smartplugiot.azurewebsites.net", this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TodoItem item = new TodoItem();
        item.Text = "New Item";

        mClient.getTable(TodoItem.class).insert(item, new TableOperationCallback<TodoItem>() {
            public void onCompleted(TodoItem entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("Azure", "Worked");
                } else {
                    exception.printStackTrace();
                }
            }
        });
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    List<TodoItem> lstRequest = mClient.getTable(TodoItem.class).execute().get();
                    for(TodoItem t:lstRequest){
                        Log.d("for", t.Text);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        */

    }

    public void setupMenuBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DevicesFragment(), "Dispositivos");
        adapter.addFragment(new WebDevicesFragment(), "Dispositivos WEB");
        viewPager.setAdapter(adapter);
    }


}

