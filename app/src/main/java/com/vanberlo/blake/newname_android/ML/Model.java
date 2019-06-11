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

/**
 * Contains 2 recurrent neural network models (one for each gender)
 */
public class Model {

    private Parameters paramsMale; // Male model parameters
    private Parameters paramsFemale; // Female model parameters
    private ArrayList<String> existingMaleNames; // List of male names the model was trained on
    private ArrayList<String> existingFemaleNames; // List of female names the model was trained on

    /**
     * Constructor for the Model class. Loads the model parameters from pre-trained weights
     * @param context
     */
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


    /**
     * Generates a name using the RNN model. Samples the name one character at a time from the
     * output of the RNN cell.
     * @param g - the gender of the name to generate
     * @return - a string representing a name generated by the RNN
     */
    public String predictName(Gender g){

        // Select the appropriate model parameters
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

            /* Continue generating new characters until either the newline character is generated
            or the name exceeds the maximum name length (specified in Constants class)
             */
            while (idx != newlineCharacter && counter != Constants.MAX_NAME_LENGTH) {

                // Run one step of forward propagation:

                // a = tanh(Wax*x + Waa*a_prev + b)
                Matrix a = (params.Wax.times(x)).plus((params.Waa.times(a_prev))).plus(params.b);
                for (int i = 0; i < a.getRowDimension(); i++) {
                    for (int j = 0; j < a.getColumnDimension(); j++) {
                        a.set(i, j, Math.tanh(a.get(i, j)));
                    }
                }

                // z = Wya*a + by
                Matrix z = params.Wya.times(a).plus(params.by);

                // y = softmax(z)
                Matrix y = MathUtils.softmax(z);

                // Sample next character from y (a probability distribution for the next character)
                idx = MathUtils.sampleFromPDF(y);

                // If the generated character isn't the newline character, append it to the list of character indices
                if (idx != newlineCharacter) {
                    indices.add(idx);
                }

                x = new Matrix(vocabSize, 1);
                x.set(idx, 0, 1);

                a_prev = a;
                counter += 1;
            }

            // Add the new character to the name string
            for (Integer index : indices) {
                name += Constants.IX_TO_CHAR.get(index);
            }

            // If the generated name exists within the training set, try again
            if(g == Gender.FEMALE){
                inNameList = existingFemaleNames.contains(name);
            } else {
                inNameList = existingMaleNames.contains(name);
            }
            ++numTries;
        } while(inNameList && numTries <= Constants.MAX_NUM_TRIES);

        return name;
    }

    // TODO: Implement a function that allows the user to generate a name with a specific first character
    public String predictNameWithStartChar(int numSamples, char startChar){
        String name = "" + startChar;

        return name;
    }

}
