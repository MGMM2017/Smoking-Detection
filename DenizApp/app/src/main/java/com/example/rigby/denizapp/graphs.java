package com.example.rigby.denizapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import static com.example.rigby.denizapp.R.id.graph;

    public class graphs extends AppCompatActivity {
        Button button;
        TextView textView;
        List<TimeAndLocation> values;
        private TimeAndLocationDataSource timeAndLocationSource;
        private Spinner spinner;
        private static final String[]modes = {"Hourly", "Weekly", "Monthly"};

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

            //textView = (TextView) findViewById(R.id.textView);
            //button = (Button) findViewById(R.id.button);
            final Context context = getApplicationContext();
            //if(values.size()<1){
              //  textView.setText("0");
            //}
            //else{
                //Integer a = values.size();
                //textView.setText(a.toString());
                drawGraphHourly(context);

            //}

            /*button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    java.util.Date date = new java.util.Date();
                    String dateString = new java.sql.Timestamp(date.getTime()).toString();
                    TimeAndLocation tlo = timeAndLocationSource.createTime(dateString, "1", 48.013515, 7.830392);
                    //writeToFile(dateString,context);
                    values = timeAndLocationSource.getAllTimeAndLoc();
                    Integer a = values.size();
                    textView.setText(a.toString());
                    //drawGraphWeekly(context);

                }

            });*/



            spinner = (Spinner) findViewById(R.id.spinner2);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(graphs.this,
                    android.R.layout.simple_spinner_item,modes);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                    switch (position) {
                        case 0:
                            drawGraphHourly(context);
                            break;
                        case 1:
                            drawGraphWeekly(context);
                            break;
                        case 2:
                            drawGraphMonthly(context);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });

        /*Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    drawGraphMonthly(context);
                } else {
                    drawGraphWeekly(context);
                }
            }
        });*/
        }




        private void drawGraphMonthly(Context context) {
            SimpleDateFormat sdfmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int numberOfLabels = values.size();
            GraphView graphView = (GraphView) findViewById(graph);
            graphView.removeAllSeries();


            // generate Dates
            java.util.Date dates[] = new java.util.Date[numberOfLabels];
            int Jan =0;
            int Feb =0;
            int March =0;
            int Apr =0;
            int May =0;
            int Jun =0;
            int Jul =0;
            int Aug =0;
            int Sep =0;
            int Oct =0;
            int Nov =0;
            int Dec =0;

            for (int a = 0; a<values.size();a++){
                try {
                    dates[a] = sdfmt1.parse(values.get(a).getTime());
                    if(dates[a].getMonth() == 0){
                        Jan++;
                    }
                    else if (dates[a].getMonth() == 1){
                        Feb++;
                    }
                    else if (dates[a].getMonth() == 2){
                        March++;
                    }
                    else if (dates[a].getMonth() == 3){
                        Apr++;
                    }
                    else if (dates[a].getMonth() == 4){
                        May++;
                    }
                    else if (dates[a].getMonth() == 5){
                        Jun++;
                    }
                    else if (dates[a].getMonth() == 6){
                        Jul++;
                    }
                    else if (dates[a].getMonth() == 7){
                        Aug++;
                    }
                    else if (dates[a].getMonth() == 8){
                        Sep++;
                    }
                    else if (dates[a].getMonth() == 9){
                        Oct++;
                    }
                    else if (dates[a].getMonth() == 10){
                        Nov++;
                    }
                    else if (dates[a].getMonth() == 11){
                        Dec++;
                    }
                    //System.out.println("DenizDBG getmonth =" + dates[a].getMonth());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // you can directly pass Date objects to DataPoint-Constructor
            // this will convert the Date to double via Date#getTime()
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, Jan),
                    new DataPoint(1, Feb),
                    new DataPoint(2, March),
                    new DataPoint(3, Apr),
                    new DataPoint(4, May),
                    new DataPoint(5, Jun),
                    new DataPoint(6, Jul),
                    new DataPoint(7, Aug),
                    new DataPoint(8, Sep),
                    new DataPoint(9, Oct),
                    new DataPoint(10, Nov),
                    new DataPoint(11, Dec),
            });
            graphView.addSeries(series);

            //System.out.println("DenizDBG = "+dates[0]);
            graphView.setTitle("Monthly consumption ");
            //graphView.getGridLabelRenderer().setNumHorizontalLabels(numberOfLabels);

            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(12);
            graphView.getViewport().setXAxisBoundsManual(true);


        }



        private void drawGraphWeekly(Context context) {
            SimpleDateFormat sdfmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int numberOfLabels = values.size();
            GraphView graphView = (GraphView) findViewById(graph);
            graphView.removeAllSeries();

            // generate Dates
            java.util.Date dates[] = new java.util.Date[numberOfLabels];
            int mon =0;
            int tue =0;
            int wed =0;
            int thu =0;
            int fri =0;
            int sat =0;
            int sun =0;
            for (int a = 0; a<values.size();a++){
                try {
                    dates[a] = sdfmt1.parse(values.get(a).getTime());
                    if(dates[a].getDay() == 0){
                        sun++;
                    }
                    else if (dates[a].getDay() == 1){
                        mon++;
                    }
                    else if (dates[a].getDay() == 2){
                        tue++;
                    }
                    else if (dates[a].getDay() == 3){
                        wed++;
                    }
                    else if (dates[a].getDay() == 4){
                        thu++;
                    }
                    else if (dates[a].getDay() == 5){
                        fri++;
                    }
                    else if (dates[a].getDay() == 6){
                        sat++;
                    }
                    //System.out.println("DenizDBG =" + dates[a].getDay());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // you can directly pass Date objects to DataPoint-Constructor
            // this will convert the Date to double via Date#getTime()
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, sun),
                    new DataPoint(1, mon),
                    new DataPoint(2, tue),
                    new DataPoint(3, wed),
                    new DataPoint(4, thu),
                    new DataPoint(5, fri),
                    new DataPoint(6, sat)
            });
            graphView.addSeries(series);

            //System.out.println("DenizDBG = "+dates[0]);
            graphView.setTitle("Weekly consumption ");
            //graphView.getGridLabelRenderer().setNumHorizontalLabels(numberOfLabels);

            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(7);
            graphView.getViewport().setXAxisBoundsManual(true);


        }


        private void drawGraphHourly(Context context) {
            SimpleDateFormat sdfmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int numberOfLabels = values.size();
            GraphView graphView = (GraphView) findViewById(graph);
            graphView.removeAllSeries();


            // generate Dates
            java.util.Date dates[] = new java.util.Date[numberOfLabels];
            int eightTen = 0;
            int tenTwelve = 0;
            int twelveTwo = 0;
            int twoFour = 0;
            int fourSix = 0;
            int sixEight = 0;
            int eigtTen = 0;
            int tenTwelven = 0;
            int twelvetwoN = 0;
            int twoFuorN = 0;
            int fourSixN = 0;
            int sixEightN = 0;

            for (int a = 0; a < values.size(); a++) {
                try {
                    dates[a] = sdfmt1.parse(values.get(a).getTime());
                    if (dates[a].getHours() == 0 || dates[a].getHours() ==1) {
                        twelvetwoN++;
                    } else if (dates[a].getHours() == 2 || dates[a].getHours() ==3) {
                        twoFuorN++;
                    } else if (dates[a].getHours() == 4 || dates[a].getHours() == 5) {
                        fourSixN++;
                    } else if (dates[a].getHours() == 6 || dates[a].getHours() == 7) {
                        sixEightN++;
                    } else if (dates[a].getHours() == 8 || dates[a].getHours() == 9) {
                        eightTen++;
                    } else if (dates[a].getHours() == 10 || dates[a].getHours() == 11) {
                        tenTwelve++;
                    } else if (dates[a].getHours() == 12 || dates[a].getHours() == 13) {
                        twelveTwo++;
                    } else if (dates[a].getHours() == 14 || dates[a].getHours() == 15) {
                        twoFour++;
                    } else if (dates[a].getHours() == 16 || dates[a].getHours() == 17) {
                        fourSix++;
                    } else if (dates[a].getHours() == 18 || dates[a].getHours() == 19) {
                        sixEight++;
                    } else if (dates[a].getHours() == 20 || dates[a].getHours() == 21) {
                        eigtTen++;
                    } else if (dates[a].getHours() == 22 || dates[a].getHours() == 23) {
                        tenTwelven++;
                    }
                    //System.out.println("DenizDBG getmonth =" + dates[a].getHours());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, twelvetwoN),
                    new DataPoint(1, twoFuorN),
                    new DataPoint(2, fourSixN),
                    new DataPoint(3, sixEightN),
                    new DataPoint(4, eightTen),
                    new DataPoint(5, tenTwelve),
                    new DataPoint(6, twelveTwo),
                    new DataPoint(7, twoFour),
                    new DataPoint(8, fourSix),
                    new DataPoint(9, sixEight),
                    new DataPoint(10, eigtTen),
                    new DataPoint(11, tenTwelven),
            });
            graphView.addSeries(series);

            //System.out.println("DenizDBG = "+dates[0]);
            graphView.setTitle("Hourly consumption ");
            //graphView.getGridLabelRenderer().setNumHorizontalLabels(numberOfLabels);

            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(12);
            graphView.getViewport().setXAxisBoundsManual(true);
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

