package com.example.dan.beantalker;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.punchthrough.bean.sdk.*;
import com.punchthrough.bean.sdk.message.BatteryLevel;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private Bean current; // will be the bean that is connected out
    final List<Bean> beans = new ArrayList<>();
//    private ListIterator<Bean> beanz = beans.listIterator();
    private Button scanButton; // cant have this here before its oncreate = (Button) findViewById(R.id.scanbutton);

/*
 * so later on, maybe we could use the upload functionality for the bean to
 * give it differential functionalities based on a button click.
 * boom its a watch, boom its a key, etc etc
 * */
//    private String getBeanz(){
//        String out = "";
//        for(Bean elem: beans){
//            out += "\n" + elem.getDevice().getName();
//        }
//        return out;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Button scanButton =  (Button) findViewById(R.id.scanbutton);
        scanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ListIterator<Bean> beanz = beans.listIterator();
                if(mBluetoothAdapter == null) {
                    scanButton.setText("Bluetooth is not available on this device");
                } else if(mBluetoothAdapter.isEnabled()) {
                    if(beans.size() == 0) { // if no beans discovered yet,
                        scanButton.setText("Scanning");
                        //SCAN FOR BEANS
                        BeanManager.getInstance().startDiscovery(bdListener);
                        // the on discovered bean tells it to add the bean to the registry of discovered beans
                    } else {
                        //on short press, show next bluetooth found
                        if(beanz.hasNext()) {
                            Bean temp = beanz.next();
                            setCurrent(temp);
//                            setCurrent(beanz.next());
                            scanButton.setText(getCurrent().getDevice().getName());
                        } else
                            beanz = beans.listIterator(); // start over
                    }
                } else {
                    scanButton.setText("Please enable bluetooth");
                }
            }
        });
        // on long click, connect
        scanButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(beans.size() == 0)
                    return true;
                else {
                    connectToBeanNow();
                }
                return true;
            }
        });
        //Now, just hide setpin field until it is needed
        findViewById(R.id.setBeanPinEditText).setVisibility(GONE);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_ACCESS_FINE_LOCATION);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private  void connectToBeanNow() {
    	//assumes beans is not empty
        current.connect(this, beanListener);
    }

    private void setCurrent(Bean thenext){current = thenext;}
    private Bean getCurrent(){return current;}

    BeanDiscoveryListener bdListener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int rssi) {
            beans.add(bean);
        }

        @Override
        public void onDiscoveryComplete() {
            scanButton = (Button) findViewById(R.id.scanbutton);
            if(beans.size() == 0)
             	scanButton.setText("Scan again");
            else
            	scanButton.setText("Beans available");
        }
    };

    BeanListener beanListener = new BeanListener() {
        @Override
        public void onConnected() {
            if(current != null){
                scanButton = (Button) findViewById(R.id.scanbutton);
                scanButton.setText("Connected to " + current.getDevice().getName());
            } else
                return;
        }

        @Override
        public void onConnectionFailed() {
            if(current != null){
                scanButton = (Button) findViewById(R.id.scanbutton);
                scanButton.setText("Failed to connect to " + current.getDevice().getName());
                //wait, then put back to scan maybe
            } else
                return;
        }

        @Override
        public void onDisconnected() {
            //set bean info to null
            if(current != null){
                scanButton = (Button) findViewById(R.id.scanbutton);
                scanButton.setText("Disconnected from " + current.getDevice().getName().toString());
                current = null;
            } else
                return;
        }

        @Override
        public void onSerialMessageReceived(byte[] data) {}

        @Override
        public void onScratchValueChanged(ScratchBank bank, byte[] value) {
            if(bank == ScratchBank.BANK_2){
                Calendar c = Calendar.getInstance();
                byte[] buffer=convertTime();
                current.setScratchData(ScratchBank.BANK_1, buffer); // will be bank1
                Log.d("DJP", "HOUR, MINUTE, AMPM: " + buffer[0] + "\t" + buffer[1]  + "\t" + buffer[2]);
            }
        }

        @Override
        public void onError(BeanError error) {
            Log.d("DJP", error.toString());
            if(current != null){
                scanButton = (Button) findViewById(R.id.scanbutton);
                scanButton.setText("error, trying to reconnect to " + current.getDevice().getName().toString());
                try{
                    connectToBeanNow();
                }catch(Exception e){
//                    Handler h = new Handler();
//                    h.postDelayed(new ViewUpdater(e.getLocalizedMessage().toString(), scanButton), 1000);
                    // TODO: make an invisible view that shows errors only when there is an error.
                }
                scanButton.setText("Beans available");
            }
        }
        @Override
        public void onReadRemoteRssi(int rssi) {
        }
    };

    public byte[] convertTime(){
        //hours, minutes, seconds, ampm
        byte [] buffer = new byte [3/*5*/];
        Calendar c = Calendar.getInstance();
        buffer[0] = (byte) ((c.get(Calendar.HOUR)) & (0xFF));
        buffer[1] = (byte) ((c.get(Calendar.MINUTE)) & (0xFF));
        buffer[2] = (byte) ((c.get(Calendar.AM_PM)) & (0xFF));
        return buffer;
    }
    public void cancelD(View view){
        BeanManager.getInstance().cancelDiscovery();
        scanButton = (Button) findViewById(R.id.scanbutton);
        scanButton.setText("Beans available");
        if(current != null){
            current.disconnect();
            current = null;
        }
    }
    public void getBeanBattery(View view){
        if(current != null){
            current.readBatteryLevel(new Callback<BatteryLevel>() {
                @Override
                public void onResult(BatteryLevel result) {
                    Button batbut = (Button) findViewById(R.id.battery);
                    Log.d("DJP", result.getPercentage() + "%");
                    batbut.setText(result.getPercentage() + "%, " + result.getVoltage() + "V");
//                    Handler handler = new Handler();
//                    handler.postDelayed(new ViewUpdater(result.getPercentage() + "%", batbut), 1000);
//                    handler.postDelayed(new ViewUpdater(result.getVoltage() + "V", batbut), 2000);
                }
            });
        }
    }

    public void setBeanPin(View view){
        // TODO: Decide if the security is worth it.
        if(current != null){
            EditText et = (EditText) findViewById(R.id.setBeanPinEditText);
            et.setVisibility(View.VISIBLE);
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                int newPin = 0;

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//                        newPin = sth.getText();
                        handled = true;
                    }

                    if(newPin != 0){
//                        current.setPin(newPin, );
                    }
                    return handled;
                }
            });

        }
    }

//    private class ViewUpdater implements Runnable{
//    TODO: get this working.
//        private String mString;
//        private Button mB;
//
//        public ViewUpdater(String string, Button mb){
//            mString = string;
//            mB = mb;
//        }
//
//        @Override
//        public void run() {
//            mB.setText(mString);
//        }
//    }
}
