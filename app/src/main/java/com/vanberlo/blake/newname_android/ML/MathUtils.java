package com.vanberlo.blake.newname_android.ML;



import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class MathUtils {

    /**
     *
     * @param M: A column vector
     * @return Softmax of M
     */
    public static Matrix softmax(Matrix M){
        Matrix S = new Matrix(M.getRowDimension(), M.getColumnDimension());
        double sum = 0.0;
        double max = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < M.getRowDimension(); i++){
            double m = M.get(i, 0);
            if(m > max){
                max = m;
            }
        }
        for(int i = 0; i < M.getRowDimension(); i++){
            double m = M.get(i, 0);
            sum += Math.exp(m - max);
        }
        for(int i = 0; i < M.getRowDimension(); i++){
            double m = M.get(i, 0);
            S.set(i, 0, Math.exp(m - max)/sum);
        }
        return S;
    }

    /**
     *
     * @param Y
     * @return
     */
    public static Integer sampleFromPDF(Matrix Y){
        List<Pair<Integer, Double>> P = new ArrayList<Pair<Integer, Double>>();
        for(int i = 0; i < Y.getRowDimension(); i++){
            P.add(new Pair<Integer, Double>(i, Y.get(i, 0)));
        }
        EnumeratedDistribution pdf = new EnumeratedDistribution(P);
        Integer sample = (Integer)pdf.sample();
        return sample;
    }
}
