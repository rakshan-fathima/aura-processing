package com.example.afinal;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import joinery.DataFrame;


public class MainActivity extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Mytask().execute();

    }
    class Mytask extends AsyncTask<Void, Void, Void> {
        DataFrame<Object> df1 = new DataFrame<>("AID", "TimeStamp");


        @Override
        protected Void doInBackground(Void... params) {
            CSVReader csvreader = new CSVReader(MainActivity.this, "AID.csv");

            try {
                //accessing reader class
                df1 = csvreader.ReadCSV();


                //array that contains unique AID
                DataFrame<Object> array = df1.unique("AID");
                //Log.d("unique array ", String.valueOf(array));

                for (int i = 0; i < array.length(); i++) {

                    DataFrame<Object> dfsingle = dataframes((String)array.get(i, "AID"));
                    timeframes(dfsingle);

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }
       // this method carries out the statistical calculations required in making appropriate assumptions towards user behaviour. 
        private void dataprocessing(DataFrame<Object> timeframe) {
            new DataFrame<>("AID", "TimeStamp", "TimeDifference");
            new DataFrame<>();
            DataFrame<Object> cluster;
            new DataFrame<>();
            DataFrame<Object> remains;
            DataFrame<Object> finaldf = new DataFrame<>("AID","TimeStamp","TimeDifference","Minutes");

            List<Object> td = timeframe.col("TimeDifference");
            int start = 0;
            int end = 0;
            double x ;
            double y;
            float low;
            float high;
            int minlow ;
            int minhigh ;
            double minutes_mean ;
            int low_hour ;
            int high_hour ;
            Object mean;
            Object sdev;
            int mean_hours ;
            int mm ;


            finaldf.append(Arrays.asList(timeframe.get(0,"AID"),timeframe.get(0,"TimeStamp"),timeframe.get(0,"TimeDifference")));
            Log.d("ORIGINAL", String.valueOf(timeframe));

            for(int i=1 ; i< td.size() ; i++) {


                int difference = (Integer) td.get(i) - (Integer) td.get(0);
                String[] tm = timeframe.get(i, "TimeStamp").toString().split(" ");

                String[] split = tm[1].split(":");

                int min = ((Integer.parseInt(split[0])) * 60 + ((Integer.parseInt(split[1]))));


                finaldf.append(Arrays.asList(timeframe.get(i, "AID"), timeframe.get(i, "TimeStamp"), timeframe.get(i, "TimeDifference"), min));

                // if the time difference is greater than or equal to 1800 seconds, we require those rows of data.
                if (difference >= 1800) {


                    cluster = finaldf.slice(start, i + 1);


                    Log.d("si", start + "," + i);
                    Log.d("dataframe-cluster", String.valueOf(cluster));
                    start = i + 1;
                    end = start;

                    //if cluster has more than one row
                    if (cluster.length() > 1) {

                        mean = cluster.mean().col("Minutes").get(0);


                        double mean_in_hours = (double) mean/60;
                        mean_hours = (int) mean_in_hours;
                        minutes_mean = ((float) (mean_in_hours % 1) );
                        mm = (int) (minutes_mean*60);


                        sdev = cluster.stddev().col("Minutes").get(0);


                        double sdev_in_hours = (double) sdev/60;

                        x = mean_in_hours;
                        y=sdev_in_hours;



                        low =  (float)(x - y);
                        high = (float) (x + y);

                        low_hour = (int) low;
                        high_hour = (int) high;



                         minlow = (int) ((low % 1) *60);
                         minhigh = (int) ((high % 1) * 60);

                        Log.d("low-hour", low_hour+ ":" + String.format("%02d", minlow));
                        Log.d("mean-hour", mean_hours + ":" + String.format("%02d", mm));
                        Log.d("high-hour", high_hour + ":" + String.format("%02d", minhigh));
                    } else {

                        // if cluster has only one row, we just need to find the mean.

                        Object val = timeframe.get(i, "TimeStamp");
                        String[] date_time = val.toString().split(" ");

                        Log.d("mean-hour",date_time[1]);

                    }


                }

                    }

            // data that is not a part of main cluster can be taken to make calculations too.
            int length = finaldf.length();

            // remains is a cluster that starts from the row of the last cluster that fit the range we provided, and goes on till the end of the dataframe.
            remains=  finaldf.slice(end,length);

            Log.d("dataframe-remaining", String.valueOf(remains));

            if(remains.length() != 0){

            //mean of minutes column in remains
            Object mean_rem = remains.mean().col("Minutes").get(0);
            Log.d("mean", String.valueOf(mean_rem));

            //converting minutes to 24 hour format
            double mean_in_hours = (double) mean_rem/60;
            mean_hours = (int) mean_in_hours;
            minutes_mean = ((float) (mean_in_hours % 1) );
            mm = (int) (minutes_mean*60);

            //standard deviation
            sdev = remains.stddev().col("Minutes").get(0);
            double sdev_in_hours = (double) sdev/60;

            x = mean_in_hours;
            y=sdev_in_hours;


            // finding the limits of the range around the mean wrt to the standard deviation.
            low =  (float)(x - y);
            high = (float) (x + y);

            low_hour = (int) low;
            high_hour = (int) high;

            minlow = (int) ((low % 1) *60);
            minhigh = (int) ((high % 1) * 60);


            Log.d("low-hour", low_hour+ ":" + String.format("%02d", minlow));
            Log.d("mean-hour", mean_hours + ":" + String.format("%02d", mm));
            Log.d("high-hour", high_hour + ":" + String.format("%02d", minhigh));
            }


        }

        //this method is used to create unique dataframes wrt to the AID and calculates the seconds from each row.
        private DataFrame<Object> dataframes(String aid) {
            DataFrame<Object> df_new1 = new DataFrame<>("AID","TimeStamp","TimeDifference");

            for (int i = 0; i < df1.length(); i++) {
                String AID = (String) df1.get(i,"AID");


                if (AID.equals(aid)) {

                    String[] tm = ((String) df1.get(i,"TimeStamp")).split(" ");
                    String[] split =  tm[1].split(":");
                    int seconds = ((Integer.parseInt(split[0]))*60*60) + ((Integer.parseInt(split[1]))*(60))+ (Integer.parseInt(split[2]));


                    df_new1.append(Arrays.asList(df1.get(i,"AID"),df1.get(i,"TimeStamp"),seconds));
                }

            }

            return df_new1;
        }


        // this method is used to find the time difference in consecutive rows.
        private void timeframes(DataFrame<Object> dt) {
            DataFrame<Object> df_new2 = new DataFrame<>("AID","TimeStamp","TimeDifference");


            if(dt.length() > 1) {
                List<Object> ts = dt.col("TimeDifference");

                df_new2.append(Arrays.asList(dt.get(0,"AID"),dt.get(0,"TimeStamp"),0));
                for (int i = 1; i < ts.size(); i++) {


                    ts.indexOf(i - 1);
                    int index;
                    //Casting is done in order to find time difference.
                    index = ((Integer) ts.get(i)) - ((Integer)ts.get(i-1));

                    //time difference value is appended in its respective column.
                    df_new2.append(Arrays.asList(dt.get(i,"AID"), dt.get(i,"TimeStamp"),index));

                }

                dataprocessing(df_new2);
            }

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);



        }


    }
}
