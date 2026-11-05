package com.javadruid.onerepmax.algorithms;

public class LombardiAlgorithm implements OneRepMaxAlgorithm {
    @Override
    public String name() {
        return "Lombardi";
    }

    @Override
    public String description() {
        return "Utilizes an exponential curve. Stable and reliable for higher repetition sets.";
    }

    @Override
    public double getOneRepMax(double weight, int reps) {
        return weight * Math.pow(reps, 0.10);
    }

    public String toString() {
        return name();
    }
}
