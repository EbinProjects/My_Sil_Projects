/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.printingapp.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.printingapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends AppCompatActivity {
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton, button_cancel;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;
    String receivedmsg = "";
    boolean ifbattery = false;
    byte cmdforcharge;
    String prevCmd = "";
    String readData = "";
    String printmsg = "", printadd = "", printcharge = "";
    boolean isfromCahDeposit = false;
    SharedPreferences my_Preferences;
    boolean isFromAudit = false;
    TextView txtprintername;
    boolean isFromOverdue = false;
    static Movie movie;
    //LinearLayout btnLayout;
    private long mLastClickTime = 0;
    String comparemsg = "";
    byte[] bytearr, printcmd, paymentSlip;
    ProgressDialog ringProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        my_Preferences = getSharedPreferences("Bluetooth", MODE_PRIVATE);
        setContentView(R.layout.print_page_layout);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayUseLogoEnabled(true);
        ab.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        ab.setTitle(" PRINT ");
        txtprintername = (TextView) findViewById(R.id.txtprintername);
        button_cancel = (Button) findViewById(R.id.button_cancel);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }

        button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        if (D)
            // Log.e( TAG, "++ ON START ++" );

            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the chat session
            } else {
                if (mChatService == null) {
                    setupChat();
                }
            }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setEnabled(true);

        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    private void setupChat() {

        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        // mConversationView = (ListView) findViewById(R.id.in);
        // mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        // mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        // mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setTag(1);
        // mSendButton.setText("Play");
        mSendButton.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                // TextView view = (TextView) findViewById(R.id.edit_text_out);
                // String message = view.getText().toString();
                // sendMessage(message);

                // Log.e( tag, msg )


//                mSendButton.setEnabled(false);
//                mSendButton.setText("");
//                ifbattery = false;
//                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
//                    return;
//                }
                printAsync pri=new printAsync();
                pri.execute();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        regProgressBar("Printing in progress....");
//                        mSendButton.setEnabled(true);
//
//                    }
//                }, 3000);
//                mLastClickTime = SystemClock.elapsedRealtime();
//                Log.e("click", "count");
//                // TODO: 18/06/2019 for checking of printer
//                printmsg += ESC.RESETPRINTER;
//                sendMessageTahi(printmsg);
//
//                try {
//                    Thread.sleep(100);
//                }catch (Exception e){}
//
//
//                sendMessageTahi("<0x1B><0x40><0x1B><0x21><0x00><0x0A>");
//
//                try {
//                    Thread.sleep(100);
//                }catch (Exception e){}
//
//
//
//                String root = Environment.getExternalStorageDirectory().toString();
//                File myDir = new File(root, "/printer");
//                if (!myDir.exists()) {
//                    myDir.mkdirs();
//                }
//
//                Log.e("abcd","myDir length ==> "+myDir.listFiles().length);
//                int index=0;
//                int length=myDir.listFiles().length;
//
//                while (index<length) {
//
//                    String fname = "img" + index + ".png";
//                    File file = new File(myDir, fname);
//
//                    Bitmap image = null;
//                    try {
//                        image = BitmapFactory.decodeStream(new FileInputStream(file));
//                    } catch (FileNotFoundException e) {
//                        Log.e("abcd", e.getMessage());
//                        e.printStackTrace();
//                    }
//
//                    int time = image.getHeight() / 3;
//                    time = time + 500;
//                    byte[] command = PrintUtil.getCommandPrintRasterBitImage(image.getWidth(), image.getHeight());
//
//                    Log.e("abcd", "image.getWidth()" + image.getWidth());
//                    Log.e("abcd", "image.getHeight()" + image.getHeight());
//
//                    Bitmap e = PrintUtil.getMonoChromeImage(image);
//                    byte[] rasterBitImage = PrintUtil.getRasterBitImage(e);
//
//
//                    printcmd = command;
//                    bytearr = rasterBitImage;
//
//                    mChatService.write(printcmd);
//
//                    try {
//                        Thread.sleep(time * 10);
//                    } catch (InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//
//                    mChatService.write(bytearr);
//
//
//                    index++;
//                }
//
//                ringProgressDialog.dismiss();




            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

        try {
            connectDevice(printadd);
        } catch (Exception e) {
            // Log.e( TAG, "- Exception in connect device -" );
        }

    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D)
            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null)
            mChatService.stop();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if (D)
            Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected... ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            // mOutEditText.setText(mOutStringBuffer);
        }
    }

    public void sendMessageTahi(String message) {
        receivedmsg = message;

        Log.e("asd", "sendMessagesendMessage     " + message);
        // Check that we're actually connected before trying anything
        /*
         * if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED)
         * {
         *
         * return; }
         */
        // if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED)
        // {

        //  ifbattery = true;


       /* try {
            Thread.sleep(1050);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        ifbattery = true;
        if (!ifbattery) {
            checkbattery();
            //  mSendButton.setEnabled(true);

        } else {
            try {
                Thread.sleep(1050);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (message.length() > 0) {
                String str = message;
                // MyToast.toastMessage("Please wait Printing is in Process ",
                // pContext);
//                Toast.makeText(BluetoothChat.this, "  Please wait Printing is in Progress ", Toast.LENGTH_LONG).show();

                String[] strArray = str.split("<");

                try {

                    for (int i = 0; i < strArray.length; i++) {
                        String mstr = "<" + strArray[i];
                        Pattern pattern = Pattern.compile("<(.*?)>");
                        Matcher matcher = pattern.matcher(mstr);
                        // Log.e( "",
                        // " inside try catch  inside for malliiiiiiiiii" );
                        // Log.i("", ""+matcher);
                        byte cmd = (byte) 0x10;
                        String strPrintArray = "...........";
                        // strPrintArray =
                        // mstr.replace("<"+matcher.group(1)+">","");
                        if (matcher.find()) {
                            // Log.e( "",
                            // " inside try catch  inside for  inside if malliiiiiiiiii"
                            // );
                            strPrintArray = mstr.replace("<" + matcher.group(1) + ">", "");
                            // Log.e( "", "strPrintArray   " + strPrintArray +
                            // "  matcher.group(1) " + matcher.group( 1 ) );
                            // System.out.println(strPrintArray+"  1 "+matcher.group(1)+"  "+matcher.groupCount());
                          /*  if ( matcher.group( 1 ).equals( prevCmd ) && !prevCmd.equals( "0x0A" ) )
                            {

                                // .y Log.e( "MAIN ACTIVITYYY",
                                // "PREV COMMAND MATCHEDDDD" );
                            }
                            else
                            {*/
                            try {

                                if (matcher.group(1).equals("0x09")) {

                                    Log.e("inside 09 match: ", "asdad");
                                    // Normal
                                    //cmd = ( byte ) 0x09;
                                    byte[] m = new byte[2];
                                    byte[] m2 = new byte[3];
                                       /* m[0] = (byte)0x1b;
                                        m[1] = (byte)0x74;
                                        m[2] = (byte)0x00;*/
                                    m[0] = (byte) 0x1b;
                                    m[1] = (byte) 0x40;

                                    mChatService.write(m);
                                    //Thread.sleep(1050);
                                    m2[0] = (byte) 0x1b;
                                    m2[1] = (byte) 0x21;
                                    m2[2] = (byte) 0x00;

                                    // Thread.sleep(1050);
                                        /*if ( i == 1 )
                                        {
                                            Thread.sleep( 1050 );
                                        }*/
                                    // Log.e( "MAINCHAT", "<0x09>" );
                                    mChatService.write(m2);
                                        /*for(int j = 0;j < m.length;j++) {
                                            mChatService.write(m[j]);

                                        }*/

                                    // mChatService.write( cmd );

                                    //   Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x11")) {
                                    // Double Width
                                    cmd = (byte) 0x11;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        //   Thread.sleep( 1050 );
                                    }
                                    // Log.e( "MAINCHAT", "<0x11>" );

                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x10")) {
                                    // Double Height
                                    cmd = (byte) 0x10;
                                    // Log.e( "MAINCHAT", "<0x10>" );
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    // Thread.sleep(1050);
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x12")) {
                                    // Double
                                    cmd = (byte) 0x12;
                                    // Thread.sleep(1050);
                                    // Log.e( "MAINCHAT", "<0x12>" );
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x13")) {
                                    // LandScape for 80 length
                                    //  byte[] m = new byte[ 5 ];

                                    byte[] m = new byte[2];
                                    byte[] m2 = new byte[3];



                                       /* m[0] = (byte)0x1b;
                                        m[1] = (byte)0x40;

                                        mChatService.write(m);*/
                                      /*  m[0] = (byte)0x1b;
                                        m[1] = (byte)0x74;
                                        m[2] = (byte)0x00;*/
                                    m2[0] = (byte) 0x1b;
                                    m2[1] = (byte) 0x13;
                                    m2[2] = (byte) 0x50;

                                    // Thread.sleep(1050);
                                        /*if ( i == 1 )
                                        {
                                            Thread.sleep( 1050 );
                                        }*/
                                    // Log.e( "MAINCHAT", "<0x09>" );
                                    mChatService.write(m2);

                                    //  cmd = ( byte ) 0x13;
                                    // Thread.sleep(1050);
                                    // Log.e( "MAINCHAT", "<0x13>" );
                                       /* if ( i == 1 )
                                        {
                                            Thread.sleep( 1050 );
                                        }*/
                                    //  mChatService.write( cmd );
                                        /*for(int j = 0;j < m.length;j++) {
                                            mChatService.write(m[j]);
                                        }*/

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x14")) {
                                    // LandScape for 140 length


                                    byte[] m = new byte[2];
                                    byte[] m2 = new byte[3];



                                       /* m[0] = (byte)0x1b;
                                        m[1] = (byte)0x40;

                                        mChatService.write(m);*/
                                    //Thread.sleep(1050);

                                    m2[0] = (byte) 0x1b;
                                    m2[1] = (byte) 0x13;
                                    m2[2] = (byte) 0x8c;

                                    //
                                        /*if ( i == 1 )
                                        {
                                            Thread.sleep( 1050 );
                                        }*/
                                    // Log.e( "MAINCHAT", "<0x09>" );
                                    mChatService.write(m2);

                                    //   Thread.sleep(1050);
                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x15")) {
                                    // LandScape for 110 length


                                    byte[] m = new byte[2];
                                    byte[] m2 = new byte[3];



                                       /* m[0] = (byte)0x1b;
                                        m[1] = (byte)0x40;

                                        mChatService.write(m);*/
                                    //Thread.sleep(1050);

                                    m2[0] = (byte) 0x1b;
                                    m2[1] = (byte) 0x13;
                                    m2[2] = (byte) 0x6E;

                                    //
                                        /*if ( i == 1 )
                                        {
                                            Thread.sleep( 1050 );
                                        }*/
                                    // Log.e( "MAINCHAT", "<0x09>" );
                                    mChatService.write(m2);

                                    //   Thread.sleep(1050);
                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0xpaper")) {
                                    byte[] m = new byte[2];
                                    byte[] m2 = new byte[3];



                                       /* m[0] = (byte)0x1b;
                                        m[1] = (byte)0x40;

                                        mChatService.write(m);*/
                                    //Thread.sleep(1050);
                                      /*  m[0] = (byte)0x1b;
                                        m[1] = (byte)0x74;
                                        m[2] = (byte)0x00;*/
                                    m2[0] = (byte) 0x1b;
                                    m2[1] = (byte) 0x13;
                                    m2[2] = (byte) 0x01;

                                    mChatService.write(m2);

                                } else if (matcher.group(1).equals("0x40")) {

                                    byte[] m = new byte[2];
                                    m[0] = (byte) 0x1b;
                                    m[1] = (byte) 0x40;


                                    mChatService.write(m);
                                    //Thread.sleep(1050);

                                } else if (matcher.group(1).equals("0x0B")) {
                                    // BarCode
                                    cmd = (byte) 0x0B;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x0D")) {
                                    // paper feed
                                    cmd = (byte) 0x0D;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x81")) {
                                    // Graphics
                                    cmd = (byte) 0x81;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x0A")) {
                                    // New Line
                                    cmd = (byte) 0x0A;
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(700);
                                    }
                                    mChatService.write(cmd);
                                } else if (matcher.group(1).equals("0x05")) {
                                    // Marati Normal
                                    cmd = (byte) 0x05;
                                    cmdforcharge = (byte) 0x05;
                                    if (i == 1) {
                                        Thread.sleep(700);
                                    }
                                    // Thread.sleep(1050);

                                    mChatService.write(cmd);
                                    // Thread.sleep(1050);

                                } else if (matcher.group(1).equals("0x0E")) {
                                    // New Line
                                    cmd = (byte) 0x0E;
                                    if (i == 1) {
                                        Thread.sleep(700);
                                    }
                                    // Thread.sleep(1050);

                                    mChatService.write(cmd);
                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x80")) {
                                    // Graphics
                                    cmd = (byte) 0x80;
                                    // Log.e( "simpleimage", "<0x80>" );
                                    // Thread.sleep(1050);
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    mChatService.write(cmd);

                                    // Log.e("simple","  (matcher.group(1).equals("
                                    // + strPrintArray );
                                    // Thread.sleep(1050);
                                } else if (matcher.group(1).equals("0x82")) {
                                    // New Line
                                    cmd = (byte) 0x82;

                                    byte[] m = new byte[2];
                                    m[0] = cmd;
                                    m[1] = cmd;
                                    if (i == 1) {
                                        Thread.sleep(1050);
                                    }
                                    System.out.println("if cmd == 82 ");

                                    mChatService.write(cmd);
                                    // Thread.sleep(1500);
                                    // Log.e(strPrintArray,"   length()   "+strPrintArray.length()+"   cmd  "+cmd);
                                }

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            prevCmd = matcher.group(1);
                            // Log.e( "", "PREV COMMAND>>>>>>>" + prevCmd );
                            //  }
                            // Log.e(strPrintArray,"   length()   "+strPrintArray.length()+"   cmd  "+cmd);
                            /*
                             * if(cmd == (byte)0x0A){ //Thread.sleep(1050);
                             *
                             * if(strPrintArray.length() > 32){ int charlen =
                             * strPrintArray.length()/32; //String finalStr =
                             * strPrintArray; int j=0; Log.e("",
                             * "in iffff part rrrrrrrrrrrrrrrrrrrrrrrrrrrr");
                             * do{ strPrintArray = strPrintArray+" "; j++;
                             * }while(j< charlen); }else{ Log.e("",
                             * "in else part rrrrrrrrrrrrrrrrrrrrrrrrrrrr"); do{
                             * strPrintArray = strPrintArray+" "; Log.e("",
                             * "strPrintArray "+strPrintArray);
                             * }while(strPrintArray.length() <32);
                             *
                             * }
                             * Log.d(strPrintArray,"   length()   "+strPrintArray
                             * .length()); //
                             * System.out.println(strPrintArray+"  font  "
                             * +matcher.group(1)+" cmd "+cmd); byte[] byteStr =
                             * strPrintArray.getBytes();
                             * mChatService.write(byteStr);
                             * //Thread.sleep(1050); }else
                             */

                            // Log.e( "", "remaining string: " + strPrintArray
                            // );

                           /* if ( i == 1 )
                            {
                                Thread.sleep( 1050 );
                            }*/

                            // Thread.sleep(1050);
                            byte[] byteStr = strPrintArray.getBytes();

                            mChatService.write(byteStr);

                        }

                        // Thread.sleep(1050);
                        /*
                         * byte[] byteStr = strPrintArray.getBytes();
                         * mChatService.write(byteStr);
                         */

                    }
                    // mChatService.stop();
                    // Log.e("mChatService",
                    // "STOPPEDDDDDDDDDDDDDDDDDDDDDDDDDD");
                    // deviceDisConnect();
                } catch (Exception e) {
                    //   mSendButton.setEnabled(true);

                    e.printStackTrace();
                }
                try {
                    // Thread.sleep(1500);
                    // ( ( Activity ) BluetoothChat.this ).finish();
                    mSendButton.setEnabled(true);
                    mSendButton.setText("Reprint");

                    if (txtprintername.getText().toString().equals("Not Connected...")) {
                        mSendButton.setText("");
                    }
                    ifbattery = false;

                } catch (Exception e) {
                    // mSendButton.setEnabled(true);

                }

                /*
                 * try { Thread.sleep(1050); } catch (InterruptedException e) {
                 *
                 * byte[] byteStr = message.getBytes();
                 * mChatService.write(byteStr);
                 */
            }
        }
        // mSendButton.setEnabled(true);
        // }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view,
                                      int actionId, KeyEvent event) {
            // If the
            // action is
            // a key-up
            // event on
            // the return
            // key, send
            // the
            // message
            if (actionId == EditorInfo.IME_NULL
                    && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if (D)
                Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    private final void setStatus(int resId) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(subTitle);
    }

    // The Handler that gets information back from the BluetoothChatService
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

                                       /*    Log.e("handleMessage msg: ",msg.toString() );
                                           Log.e("handleMessage msgwhat: ",""+msg.what );
                                           Log.e("handleMessage msgarg: ",""+msg.arg1 );
                                           Log.e("handleMessage msgobj: ",""+msg.obj );*/
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus("Charge " + printcharge);
                            txtprintername.setText("Connected to " + mConnectedDeviceName);
                            // mSendButton.setVisibility(
                            // View.VISIBLE );

//                                                   btnLayout.setVisibility( View.VISIBLE );
                            mConversationArrayAdapter.clear();

                            try {
                                Thread.sleep(1050);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mSendButton.setEnabled(true);
                            mSendButton.setText("print");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            // setStatus(
                            // R.string.title_connecting
                            // );
                            txtprintername.setText("Connecting... ");
                            // mSendButton.setVisibility(
                            // View.INVISIBLE );
//                                                   btnLayout.setVisibility( View.INVISIBLE );
                            mSendButton.setEnabled(false);
                            mSendButton.setText("");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            // setStatus(
                            // R.string.title_not_connected
                            // );
                            txtprintername.setText("Not Connected...");
                            mSendButton.setText("");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    try {
                        byte[] writeBuf = (byte[]) msg.obj;
                        String writeMessage = new String(writeBuf);

                        // mConversationArrayAdapter.add("Me:  "
                        // + writeMessage);
                    } catch (Exception e) {

                    }
                    // construct a string from the
                    // buffer

                    //   mSendButton.setEnabled(true);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the
                    // valid bytes in the buffer

                    String readMessage = new String(readBuf, 0, msg.arg1);
                    comparemsg = comparemsg + readMessage;
                    //  readMessage =readMessage+ msg2;
                    /*
                     * mConversationArrayAdapter.add(
                     * mConnectedDeviceName + ":  " +
                     * readMessage );
                     */
                    // Log.e( "",
                    // "READ MESSEGE OUT :" +
                    // readMessage );

                    Log.e("ChargeMEssage: ", comparemsg);
                    if (comparemsg.length() >= 5 || !comparemsg.trim().isEmpty()) {
                        comparemsg = "".trim();
                        // ifbattery = true;
                        readMessage = readMessage.trim().replace("BL=", "");
                        String chargeValue = readMessage.replaceAll("[^0-9]", "");
                        if (chargeValue.equals("")) {
                            chargeValue = "1";
                        }
                        int charge = 0;
                        // Log.e( "",
                        // "READ MESSEGE :" +
                        // readMessage );
                        /*
                         * try{
                         * charge=Integer.parseInt(
                         * readMessage ); }catch
                         * (Exception e) {
                         *  }
                         */


                        if (chargeValue.equals("0")) {
                            txtprintername.setText("No Charge in Printer ");

                        } else {

                            ifbattery = true;

                            BluetoothChat.this.sendMessageTahi(receivedmsg);

                        }
                        setStatus("Charge " + chargeValue);
                        SharedPreferences.Editor editor = my_Preferences.edit();
                        editor.putString("PRINTERCHARGE", readMessage);
                        editor.commit();
                        readMessage = "";
                        // ifbattery = true;
                        // BluetoothChat.this.sendMessageTahi(
                        // receivedmsg );

                        /*
                         * readMessage =
                         * readMessage.trim
                         * ().replace( "BL=", "" );
                         * printcharge = readMessage;
                         * // Log.e( "",
                         * "READ MESSEGE :" +
                         * readMessage );
                         *
                         * SharedPreferences.Editor
                         * editor =
                         * my_Preferences.edit();
                         * editor.putString(
                         * Variables.PRINTERCHARGE,
                         * readMessage );
                         * editor.commit();
                         *
                         * if ( readMessage.contains(
                         * "0" ) ) {
                         * txtprintername.setText(
                         * "No Charge in Printer " );
                         *
                         * }else {
                         *
                         * ifbattery = true;
                         *
                         * BluetoothChat.this.
                         * sendMessageTahi(
                         * receivedmsg );
                         *
                         * }
                         */
                    } else {
                        //  mSendButton.setEnabled(true);
                    }

                    // mSendButton.setEnabled( false
                    // );
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's
                    // name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    txtprintername.setText("Connected to " + mConnectedDeviceName);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    mSendButton.setEnabled(true);
                    mSendButton.setText("print");
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    //    mSendButton.setEnabled(true);
                    break;
            }
        }
    };
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void checkbattery() {
         /*byte cmd = ( byte ) 0x05;
        cmdforcharge = ( byte ) 0x05;*/

        byte[] m = new byte[3];

        m[0] = (byte) 0x1c;
        m[1] = (byte) 0x62;
        m[2] = (byte) 0x00;

        mChatService.write(m);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (D)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        connectDevice(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChat();
                } else {
                    Toast.makeText(this, "Bluetooth Not Enabled", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void connectDevice(String data) {
        try {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(data);
            // Attempt to connect to the device
            mChatService.connect(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, (Menu) menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
            case R.id.connect_scan:
                // Launch the DeviceListActivity to see devices and do scan
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    // Otherwise, setup the chat session
                } else {
                    // Launch the DeviceListActivity to see devices and do scan
                    serverIntent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
                return true;
            case R.id.discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;
    }


    public void deviceDisConnect() {
        mChatService.stop();
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        moveTaskToBack(true);
    }

    public class printAsync extends AsyncTask<String, String, String> {

        int time = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSendButton.setEnabled(false);
            mSendButton.setText("");
            ifbattery = false;
            if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                return;
            }
            regProgressBar("Printing in progress....");
        }

        @Override
        protected String doInBackground(String... aurl) {
            mLastClickTime = SystemClock.elapsedRealtime();
                Log.e("click", "count");
                // TODO: 18/06/2019 for checking of printer
                printmsg += ESC.RESETPRINTER;
                sendMessageTahi(printmsg);

                try {
                    Thread.sleep(100);
                }catch (Exception e){}

            sendMessageTahi("<0x1B><0x40><0x1B><0x21><0x00><0x0A>");

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root, "/printer");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            Log.e(TAG, "data: "+myDir.length() );

            String fname = "img0.png";
            File file = new File(myDir, fname);

            Bitmap image = null;
            try {
                image = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                Log.e("abcd",e.getMessage());
                e.printStackTrace();
            }
//                    Bitmap image = StringToBitMap(Variables.htmlDataForPrint);
//                    saveImageBitmap(image, "print");
            time = image.getHeight() / 2;
                            time = time + 500;
            byte[] command = PrintUtil.getCommandPrintRasterBitImage(image.getWidth(), image.getHeight());

            Log.e("abcd", "image.getWidth()" + image.getWidth());
            Log.e("abcd", "image.getHeight()" + image.getHeight());

            Bitmap e = PrintUtil.getMonoChromeImage(image);
            byte[] rasterBitImage = PrintUtil.getRasterBitImage(e);


            printcmd = command;
            bytearr = rasterBitImage;

            mChatService.write(printcmd);
            mChatService.write(bytearr);

           return  null;
        }

        @Override
        protected void onPostExecute(String unused) {





                    ringProgressDialog.dismiss();







        }
    }
    public void regProgressBar(String txtValue) {
        ringProgressDialog = ProgressDialog.show(BluetoothChat.this, "Please wait ...", "" + txtValue, true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
    }


}
