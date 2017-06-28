package com.example.rigby.denizapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.rigby.denizapp.R.id.graph;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button buttonClear;
    TextView textView;
    List<TimeAndLocation> values;
    private TimeAndLocationDataSource timeAndLocationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeAndLocationSource = new TimeAndLocationDataSource(this);
        try {
            timeAndLocationSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        values = timeAndLocationSource.getAllTimeAndLoc();

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        buttonClear = (Button) findViewById(R.id.button2);
        final Context context = getApplicationContext();
        if(readFromFile(context)==null){
            textView.setText("0");
        }
        else{
            textView.setText(countLinesFromFile(context).toString());
            drawGraph(context);

        }

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                java.util.Date date = new java.util.Date();
                String dateString = new java.sql.Timestamp(date.getTime()).toString();
                TimeAndLocation tlo = timeAndLocationSource.createTime(dateString, "1", "Mylocation");
                writeToFile(dateString,context);
                textView.setText(countLinesFromFile(context).toString());
                drawGraph(context);

            }

        });

        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFile(context);
                textView.setText(readFromFile(context));
                drawGraph(context);
                String forDebug = "";
                int a;
                for (a = 0; a<values.size();a++){
                    forDebug += values.get(a).getId();
                    forDebug += values.get(a).getNicotin();
                    forDebug += values.get(a).getTime();
                    forDebug += values.get(a).getLocation();
                }
                printData("DB Results",forDebug);
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

    private void drawGraph(Context context){
        Date date;
        SimpleDateFormat sdfmt1 = new SimpleDateFormat();
        int numberOfLabels = values.size();
        GraphView graphView = (GraphView) findViewById(graph);

        /*DataPoint[] dp =new DataPoint[values.size()];
        for(int i = 0; i<values.size();i++){
            try {
                date = (Date) sdfmt1.parse(values.get(i).getTime());
                int nicotin = Integer.parseInt(values.get(i).getNicotin());
                dp[i] =  new DataPoint(date,nicotin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dp);*/


        // generate Dates
        Calendar calendar = Calendar.getInstance();
        java.util.Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        java.util.Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        java.util.Date d3 = calendar.getTime();

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });
        graphView.addSeries(series);


        graphView.addSeries(series);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphView.getContext()));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(numberOfLabels);

        graphView.getViewport().setMinX(4);
        graphView.getViewport().setMaxX(6);
        graphView.getViewport().setXAxisBoundsManual(true);



        //Integer numberOfEntries = Integer.parseInt(countLinesFromFile(context).toString());

        //DataPoint[] dp = new DataPoint[];
        //for(int i=0; i<numberOfEntries; i++) {
           // dp[i] = new DataPoint(i*i, i);
        //}
//see code https://github.com/appsthatmatter/GraphView-Demos/blob/master/app/src/main/java/com/jjoe64/graphview_demos/examples/Dates.java
    }

    private void printData(String title , String message)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}