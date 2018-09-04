package com.vanberlo.blake.newname_android.ML;
import android.content.Context;

import com.vanberlo.blake.newname_android.Constants;
import com.vanberlo.blake.newname_android.Enumerations.Gender;
import com.vanberlo.blake.newname_android.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import Jama.Matrix;

public class Model {

    private Parameters paramsMale;
    private Parameters paramsFemale;

    public Model(Context context) {
        paramsMale = new Parameters(Gender.MALE, context);
        paramsFemale = new Parameters(Gender.FEMALE, context);
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


        while(idx != newlineCharacter && counter != Constants.MAX_NAME_LENGTH){
            Matrix a = (params.Wax.times(x)).plus((params.Waa.times(a_prev))).plus(params.b);
            for(int i = 0; i < a.getRowDimension(); i++){
                for(int j = 0; j < a.getColumnDimension(); j++){
                    a.set(i, j, Math.tanh(a.get(i, j)));
                }
            }
            Matrix z = params.Wya.times(a).plus(params.by);
            Matrix y = MathUtils.softmax(z);
            idx = MathUtils.sampleFromPDF(y);
            indices.add(idx);

            x = new Matrix(vocabSize, 1);
            x.set(idx, 0, 1);

            a_prev = a;
            counter += 1;
        }

        if(counter == 20){
            indices.add(newlineCharacter);
        }

        for (Integer index : indices) {
            name += Constants.IX_TO_CHAR.get(index);
        }

        return name;
    }

    public String predictNameWithStartChar(int numSamples, char startChar){
        String name = "" + startChar;

        return name;
    }

}
