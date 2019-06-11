package com.vanberlo.blake.newname_android.ML;

import android.content.Context;

import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 * Defines the parameters of an RNN model
 */
public class Parameters {

    public Matrix Wax; // Weight matrix multiplying the input
    public Matrix Waa; // Weight matrix multiplying the hidden state
    public Matrix Wya; // Weight matrix connecting hidden state to output
    public Matrix b; // Bias vector
    public Matrix by; // Bias connecting hidden state to output

    private Context context;

    /**
     * Initializes the weight and bias matrices. Loads the numeric pre-trained parameters from a CSV
     * file, based on the specified gender
     * @param g - the gender specified for the model
     * @param c - the current application context
     */
    public Parameters(Gender g, Context c){
        context = c;
        if(g == Gender.MALE){
            InputStream inputStream = context.getResources().openRawResource(R.raw.wax_male);
            Wax = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.waa_male);
            Waa = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.wya_male);
            Wya = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.b_male);
            b = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.by_male);
            by = readCSVIntoMatrix(inputStream);
        }
        else {
            InputStream inputStream = context.getResources().openRawResource(R.raw.wax_female);
            Wax = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.waa_female);
            Waa = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.wya_female);
            Wya = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.b_female);
            b = readCSVIntoMatrix(inputStream);
            inputStream = context.getResources().openRawResource(R.raw.by_female);
            by = readCSVIntoMatrix(inputStream);
        }
    }

    /**
     * Reads the content of a CSV file and puts it into a Matrix object
     * @param inputStream - the CSV file
     * @return - a Matrix object
     */
    private Matrix readCSVIntoMatrix(InputStream inputStream){
        List<Double[]> result = new ArrayList<Double[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                Double[] rowVals = new Double[row.length];
                for(int i = 0; i < rowVals.length; i++){
                    rowVals[i] = Double.parseDouble(row[i]);
                }
                result.add(rowVals);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Object[] arr = result.toArray();
        double[][] arrPrim = new double[arr.length][((Double[])arr[0]).length];
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < ((Double[])arr[0]).length; j++){
                arrPrim[i][j] = ((Double[])arr[i])[j];
            }
        }
        return new Matrix(arrPrim);
    }

}
