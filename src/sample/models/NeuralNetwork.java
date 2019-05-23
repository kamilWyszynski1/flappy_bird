package sample.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class NeuralNetwork {
    private Vector<Vector<Double>> weights1 = new Vector<Vector<Double>>();
    private Vector<Vector<Double>> weights2 = new Vector<Vector<Double>>();
    private int inputsAmount;
    private int hiddenAmount;
    private Random generator = new Random();

    public NeuralNetwork(int inputsAmount, int hiddenAmount) {
        /**Initialization of matrix
         * weights1 are responsible for weights of inputs
         * weigts2 are responsible for each node of hidden layer
         * */
        this.inputsAmount = inputsAmount;
        this.hiddenAmount = hiddenAmount;

        for(int i = 0; i < this.inputsAmount; i++){
            Vector<Double> r = new Vector<>();
            for(int j = 0;j < this.hiddenAmount; j++){
                r.add(-1.0 + ((2.0) * generator.nextDouble()));
            }
            weights1.add(r);
        }
        System.out.println(weights1);

        for(int i = 0; i < this.hiddenAmount; i++){
            Vector<Double> r = new Vector<>();
            for(int j = 0;j < 1; j++){
                r.add(-1.0 + ((2.0) * generator.nextDouble()));
            }
            weights2.add(r);
        }
        System.out.println(weights2);
    }

    private double sigm(double value){
        return (1/( 1 + Math.pow(Math.E,(-1*value))));
    }


    public double predict(Vector<Double> inputs){
        Vector<Double> firstLayer = new Vector<>();
        // np.dot(inputs, weigts1)
        for(int i=0;i<this.hiddenAmount;i++) {
            double x = (inputs.get(0)*weights1.get(0).get(i) + inputs.get(1)*weights1.get(1).get(i));
            firstLayer.add(this.sigm(x));
        }

        double prediction = 0;
        for(int i = 0;i < hiddenAmount; i++)
            prediction += firstLayer.get(i) * weights2.get(i).get(0);

        return this.sigm(prediction);





    }

}
