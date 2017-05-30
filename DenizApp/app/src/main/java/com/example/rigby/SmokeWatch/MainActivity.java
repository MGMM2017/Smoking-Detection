package com.example.rigby.SmokeWatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Set;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button buttonCount;
    Button buttonClear;
    Button buttonConnect;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        buttonCount = (Button) findViewById(R.id.button);
        buttonClear = (Button) findViewById(R.id.button2);
        buttonConnect = (Button) findViewById(R.id.button4);
        final Context context = getApplicationContext();
        if(readFromFile(context)==null){
            textView.setText("0");
        }
        else{
            textView.setText(countLinesFromFile(context).toString());
        }

        buttonCount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Calendar c = Calendar.getInstance();
                String date = c.getTime().toString();
                writeToFile(date,context);
                textView.setText(countLinesFromFile(context).toString());
            }

        });

        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFile(context);
                textView.setText(readFromFile(context));
            }
        });

        buttonConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connectBT(context);
            }
        });

    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("stats.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.write("\n");
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("stats.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void clearFile(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("stats.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write("0");
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectBT(Context context) {
        int REQUEST_BLUETOOTH = 1;
        ArrayList <DeviceItem>deviceItemList;

        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }
        else{
            if (!BTAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }

            Log.d("DEVICELIST", "Super called for DeviceListFragment onCreate\n");
            deviceItemList = new ArrayList<DeviceItem>();
            Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    DeviceItem newDevice= new DeviceItem(device.getName(),device.getAddress(),"false");
                    deviceItemList.add(newDevice);
                }
            }
        }
    }

    private Integer countLinesFromFile(Context context) {
        int numberOfLines=0;
        try {
            InputStream inputStream = context.openFileInput("stats.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while ( (bufferedReader.readLine()) != null ) {
                    numberOfLines++;
                }
                inputStream.close();
            }

        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return numberOfLines;
    }

}