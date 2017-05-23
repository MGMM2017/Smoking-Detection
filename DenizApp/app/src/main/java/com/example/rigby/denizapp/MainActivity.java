package com.example.rigby.denizapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button buttonClear;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        buttonClear = (Button) findViewById(R.id.button2);
        final Context context = getApplicationContext();
        if(readFromFile(context)==null){
            textView.setText("0");
        }
        else{
            textView.setText(countLinesFromFile(context).toString());
        }

        button.setOnClickListener(new OnClickListener() {

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
            outputStreamWriter.write("");
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
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