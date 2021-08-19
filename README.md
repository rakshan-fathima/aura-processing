# aura-processing

This repo contains the program that undergoes data analysis within Android Studio.

Unlike Python, Android Studio does not contain a data analysis tool such as Pandas that can help with creating dataframes. Therefore,we imported a library called joinery to help with processing and analyzing data.

### Importing joinery library

In Projects, app --> libs; import the jar file that contains joinery dataframe. You can find the jar file at: https://github.com/cardillo/joinery
Once you've imported the jar file, go to Project Structures and in Dependencies --> App--> Add Jar/Aar Dependency and provide the path of the jar file imported. 

After these steps are carried out, you will be able to use the inbuilt functions from joinery library. 

### Reading CSV File 

Import the CSV file onto Assets Folder in Android Studio. The CSV Reading is done in the class ```CSVReader.java```.
A dataframe is created that will contain the values from the CSV file: ``` DataFrame<Object> df = new DataFrame<>("AID","TimeStamp");```


``` String[] row = line.split(cvsSplitBy)```, this is done to take data row by row. Further to add data in the appropriate DataFrames columns, we split by comma and append the values into the DataFrame.
 
                String[] list = row[i].split(",");
                df.append(Arrays.asList(list[0],list[1]));
                
### Data Clustering 

First we need to collect unique AID from the main DataFrame: ```DataFrame<Object> array = df1.unique("AID");```
 Then to create unique dataframes with respect to AID the following is done: 
 
                    for (int i = 0; i < array.length(); i++) {
                    DataFrame<Object> dfsingle = dataframes((String)array.get(i, "AID"));
                    timeframes(dfsingle); } 
                    
dataframes method is used to create clusters with respect to unique AID stored in the array. Once the clustering is done, we convert the TimeStamp column into seconds and add it into TimeDifference column. This is done passed into timeframes method as a parameter where we will be calculating time difference and pre-processing data. 

### Data Processing 

In the dataprocessing method,we will be carrying out calculations to find the mean and standard deviation of the data. 
Using mean and standard deviation we can find the boundary values for the time each appliance was used. 

If the time difference is greater than or equal to 1800 seconds, we require those rows of data.
```cluster = finaldf.slice(start, i + 1);``` 

slice is a function in joinery dataframe that takes only the values of the rows that we have set. If the cluster has more than one row we carry onto finding mean,standard deviation and the boundaries of the range. Otherwise, we just need to calculate its mean. 

Data that is not a part of main cluster can be taken to make calculations too. remains is a cluster that starts from the row of the last cluster that fit the range we provided, and goes on till the end of the dataframe.
          








                    












