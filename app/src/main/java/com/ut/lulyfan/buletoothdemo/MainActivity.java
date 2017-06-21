package com.ut.lulyfan.buletoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ut.lulyfan.buletoothdemo.bluetooth.BluetoothUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements BluetoothUtil.BluetoothListener{

    private static final int ENABLE_DISCOVERABLE = 1;
    private static final int MESSAGE_READ = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private ListView boundedDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boundedDevices = (ListView) findViewById(R.id.boundedDevices);
        boundedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothUtil.MyBluetoothDevice device = (BluetoothUtil.MyBluetoothDevice) parent.getSelectedItem();
                BluetoothUtil.ConnectThread connectThread = new BluetoothUtil.ConnectThread(device.bluetoothDevice);
                connectThread.setBluetoothListener(MainActivity.this);
                connectThread.start();
            }
        });

        mBluetoothAdapter = BluetoothUtil.mBluetoothAdapter;
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivityForResult(discoverableIntent, ENABLE_DISCOVERABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<BluetoothUtil.MyBluetoothDevice> devices = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                devices.add(new BluetoothUtil.MyBluetoothDevice(device));
            }
        }
        boundedDevices.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, devices));

        BluetoothUtil.AcceptThread acceptThread = new BluetoothUtil.AcceptThread();
        acceptThread.setBluetoothListener(this);
        acceptThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENABLE_DISCOVERABLE && requestCode == RESULT_OK) {

        }
    }

    @Override
    public void connect(BluetoothUtil.ConnectedThread connectedThread) {

    }

    @Override
    public void handleMsg(byte[] buf, int count) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


}
