package com.example.afinal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import joinery.*;

import android.content.Context;
import android.util.Log;

public class CSVReader {

    Context context;

    String file_name;

    ArrayList<HashMap<String, String>> CSVData;

    public CSVReader(Context context, String file_name) {
        this.context = context;
        this.file_name = file_name;

    }

    public DataFrame<Object> ReadCSV() throws IOException {

        InputStream is = context.getAssets().open(file_name);

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader br = new BufferedReader(isr);

        String line;

        String cvsSplitBy = "\n";

        br.readLine();

        DataFrame<Object> df = new DataFrame<>("AID","TimeStamp");




        while ((line = br.readLine()) != null) {

            String[] row = line.split(cvsSplitBy);

            for (int i = 0 ; i< row.length; i++) {

                //splitting to create columns
                String[] list = row[i].split(",");
                //adding columns
                df.append(Arrays.asList(list[0],list[1]));


            }

        }

        return df;

    }

}