package com.javadruid.onerepmax.algorithms;

public class LanderAlgorithm implements OneRepMaxAlgorithm {
    @Override
    public String name() {
        return "Lander";
    }

    @Override
    public String description() {
        return "Offers a balanced linear curve, widely favored in powerlifting programming.";
    }

    @Override
    public double getOneRepMax(double weight, int reps) {
        return (100.0 * weight) / (101.3 - (2.67123 * reps));
    }

    public String toString() {
        return name();
    }

}
