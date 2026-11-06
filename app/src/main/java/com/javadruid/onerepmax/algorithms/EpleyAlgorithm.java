package com.javadruid.onerepmax.algorithms;

public class EpleyAlgorithm implements OneRepMaxAlgorithm {
    @Override
    public String name() {
        return "Epley";
    }

    @Override
    public String description() {
        return "The most popular formula. Best for general strength training and moderate rep ranges (1-10 reps).";
    }

    @Override
    public double getOneRepMax(double weight, int reps) {
        return weight * (1.0 + (reps / 30.0));
    }

    public String toString() {
        return name();
    }

}
