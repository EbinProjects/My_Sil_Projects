package com.softland.demoiot;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.softland.demoiot.adapters.BarcodeListAdapter;
import com.softland.demoiot.adapters.PairedListAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnClickListner {

    public final String TAG = MainActivity.class.getSimpleName();
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    public final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private TextView mBluetoothStatus;
    private TextView mReadBuffer;
    private Button mScanBtn;
    private Button mOffBtn;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private ListView mDevicesListView;
    private TextView charge;
    private TextView ItemCount;
    private ImageView Battey;
    private CheckBox mLED1;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayList<String> mBTArrayAdapter;
    private ArrayList<String> Barcodenumbers;
    MaterialToolbar toolbar;
    private RecyclerView recycler_vew_barcode_number;
    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    static boolean firstTime = true;
    BarcodeListAdapter barcodeListAdapter;
    private BottomSheetDialog bottomSheetDialog;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Live Monitor");
        actionBar.setDisplayShowHomeEnabled(true);
        mBTArrayAdapter = new ArrayList<>();
        charge = findViewById(R.id.Charge);
        ItemCount = findViewById(R.id.dataCount);
        Battey = findViewById(R.id.battery);
        Barcodenumbers = new ArrayList<>();
        barcodeListAdapter = new BarcodeListAdapter(Barcodenumbers, MainActivity.this);


        recycler_vew_barcode_number = (RecyclerView) findViewById(R.id.recycler_vew_barcode_number);
        recycler_vew_barcode_number.setAdapter(barcodeListAdapter);
        recycler_vew_barcode_number.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));



        toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Disconnected....");
        toolbar.inflateMenu(R.menu.option_mewnu);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio
        toolbar.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btn_bluetooth) {
                    if (!mBTAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        return true;
                    } else {
                        mBTArrayAdapter.clear();
                        mPairedDevices = mBTAdapter.getBondedDevices();
                        for (BluetoothDevice device : mPairedDevices)
                            mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

                        showPairedBluetoothList();
                    }
                } else if (item.getItemId() == R.id.btn_clear_data) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Confirm ?")
                            .setMessage("Do you want to clear list ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (mConnectedThread != null) {
                                        mConnectedThread.write("0x1C");
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();

                }

                return false;
            }
        });


        mHandler = new Handler(Looper.getMainLooper()) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    if (readMessage.trim().equalsIgnoreCase("FILE DELETED")) {
                        Barcodenumbers = new ArrayList<>();
                        ItemCount.setText("0");
                        barcodeListAdapter.setData(Barcodenumbers);
                        if (mConnectedThread != null) {
                            mConnectedThread.write("0x1D");
                        }
                    } else if (readMessage.trim().contains("Battery percentage is")) {
                        String str=readMessage.trim().replace("Battery percentage is", " ").trim();
                        int number = Integer.parseInt(str);
                        if(number>=90){
                            Battey.setImageResource(R.drawable.between65to90);
                        }else if (number<90 && number>=65){
                            Battey.setImageResource(R.drawable.between65to90);
                        }else if (number<65 && number>=35){
                            Battey.setImageResource(R.drawable.between36to64);
                        }else if (number<35 && number>=16){
                            Battey.setImageResource(R.drawable.between35to16);
                        }else if (number<16 && number>=1){
                            Battey.setImageResource(R.drawable.below15);
                        }else {
                            Battey.setImageResource(R.drawable.zero);
                        }

                        charge.setText("  "+number+ "%");
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {}

                        if (mConnectedThread != null) {
                            Barcodenumbers.clear();
                            mConnectedThread.write("0x1D");
                        }
                    } else {
                        try {

                            Log.i(TAG, "handleMessage: "+readMessage);
                          //  final List<String> stringList = new ArrayList<String>(Arrays.asList(readMessage.trim().split("\n")));
                            Barcodenumbers.addAll(cleanInputs(readMessage.trim().split("\n")));
                            barcodeListAdapter.setData(Barcodenumbers);
                            String dat=String.valueOf(Barcodenumbers.size());
                           try {
                               ItemCount.setText(dat);
                           }catch (Exception ignored) {}
                        }
                        catch (Exception e){

                            Barcodenumbers = new ArrayList<>();
                            barcodeListAdapter.setData(Barcodenumbers);

                            if (mConnectedThread != null) {
                                mConnectedThread.write("0x1D");
                            }


                        }

                    }

                }

                if (msg.what == CONNECTING_STATUS) {
                    charge.setText("");
                    ItemCount.setText("");
                    if (msg.arg1 == 1) {
                        Battey.setVisibility(View.VISIBLE);
//                        toolbar.getMenu().findItem(R.id.battery).setVisible(true);
                        toolbar.setSubtitle("Connected to Device: " + msg.obj);
                        if (firstTime) {
                            firstTime = false;
                            sendBatteryCommand();

                        }
                    } else {
                        toolbar.setSubtitle("Connection Failed");
                        charge.setText(null);
                        ItemCount.setText(null);
                        Battey.setVisibility(View.GONE);
                        toolbar.getMenu().findItem(R.id.charges).setVisible(false);
                        Barcodenumbers = new ArrayList<>();
                        barcodeListAdapter.setData(Barcodenumbers);

                    }
                }
            }
        };

    }


    public void sendBatteryCommand() {

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (mConnectedThread != null) {
                        mConnectedThread.write("0x1B");
                    }
                    SystemClock.sleep(60000);
                }
            }
        }.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
            }

        }
    }


    private ArrayList<String> cleanInputs(String[] inputArray) {
        ArrayList<String> result = new ArrayList<String>();

        for (String input : inputArray) {
            if (input != null) {
                String str = input.trim();
                if (!str.isEmpty()) {
                    result.add(str);
                }
            }
        }
        Log.i(TAG, "cleanInputs: "+result.size());

        return result;
    }



    private void showPairedBluetoothList() {
        Log.e("abcd", "mBTArrayAdapter  => " + mBTArrayAdapter.size());
        PairedListAdapter pairedListAdapter = new PairedListAdapter(mBTArrayAdapter, MainActivity.this);
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.layout_paired_list);
        RecyclerView pairedList = bottomSheetDialog.findViewById(R.id.recycler_view_paired_list);
        pairedList.setAdapter(pairedListAdapter);
        pairedList.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        pairedListAdapter.setListner(MainActivity.this);
        pairedListAdapter.notifyDataSetChanged();
        bottomSheetDialog.show();
    }

    @Override
    public void onClick(int position, String deviceName, String deviceId) {
        {

            firstTime = true;
            Barcodenumbers = new ArrayList<>();
            barcodeListAdapter.setData(Barcodenumbers);
            if (bottomSheetDialog.isShowing()) {
                bottomSheetDialog.cancel();
            }

            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }

            toolbar.setSubtitle("Connecting...");
            final String address = deviceId;
            final String name = deviceName;

            new Thread() {
                @Override
                public void run() {
                    boolean fail = false;
                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }

                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                        } catch (IOException e2) {
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!fail) {
                        mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                        mConnectedThread.start();
                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name).sendToTarget();
                    }
                }
            }.start();
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection", e);
        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }


}