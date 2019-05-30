package com.vanberlo.blake.newname_android.ML;
import android.content.Context;

import com.vanberlo.blake.newname_android.Constants;
import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import Jama.Matrix;

public class Model {

    private Parameters paramsMale;
    private Parameters paramsFemale;
    private ArrayList<String> existingMaleNames;
    private ArrayList<String> existingFemaleNames;

    public Model(Context context) {
        paramsMale = new Parameters(Gender.MALE, context);
        paramsFemale = new Parameters(Gender.FEMALE, context);
        existingMaleNames = new ArrayList<String>();
        existingFemaleNames = new ArrayList<String>();
        InputStream inputStream = context.getResources().openRawResource(R.raw.male_set);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputStreamReader);
        String line;
        try {
            while (( line = buffReader.readLine()) != null) {
                existingMaleNames.add(line);
            }
        } catch (IOException e) {}
        inputStream = context.getResources().openRawResource(R.raw.female_set);
        inputStreamReader = new InputStreamReader(inputStream);
        buffReader = new BufferedReader(inputStreamReader);
        try {
            while (( line = buffReader.readLine()) != null) {
                existingFemaleNames.add(line);
            }
        } catch (IOException e) {}
    }


    public String predictName(Gender g){
        Parameters params;
        if(g == Gender.MALE){
            params = paramsMale;
        }
        else {
            params = paramsFemale;
        }

        String name = "";
        boolean inNameList = true;
        int numTries = 0;
        do {
            name = "";
            List<Integer> indices = new ArrayList<Integer>();
            int vocabSize = Constants.CHAR_TO_IX.size();
            int n_a = params.Waa.getColumnDimension();

            // Initialize x
            Matrix x = new Matrix(vocabSize, 1);

            // Initialize a_prev
            Matrix a_prev = new Matrix(n_a, 1);

            int idx = -1; // A flag to detect newline character
            int counter = 0; // Keep track of length of name
            int newlineCharacter = Constants.CHAR_TO_IX.get('\n');


            while (idx != newlineCharacter && counter != Constants.MAX_NAME_LENGTH) {
                Matrix a = (params.Wax.times(x)).plus((params.Waa.times(a_prev))).plus(params.b);
                for (int i = 0; i < a.getRowDimension(); i++) {
                    for (int j = 0; j < a.getColumnDimension(); j++) {
                        a.set(i, j, Math.tanh(a.get(i, j)));
                    }
                }
                Matrix z = params.Wya.times(a).plus(params.by);
                Matrix y = MathUtils.softmax(z);
                idx = MathUtils.sampleFromPDF(y);
                if (idx != newlineCharacter) {
                    indices.add(idx);
                }

                x = new Matrix(vocabSize, 1);
                x.set(idx, 0, 1);

                a_prev = a;
                counter += 1;
            }

            if (counter == 20) {
                indices.add(newlineCharacter);
            }

            for (Integer index : indices) {
                name += Constants.IX_TO_CHAR.get(index);
            }

            if(g == Gender.FEMALE){
                inNameList = existingFemaleNames.contains(name);
            } else {
                inNameList = existingMaleNames.contains(name);
            }
            ++numTries;
        } while(inNameList && numTries <= 50);

        return name;
    }

    public String predictNameWithStartChar(int numSamples, char startChar){
        String name = "" + startChar;

        return name;
    }

}
