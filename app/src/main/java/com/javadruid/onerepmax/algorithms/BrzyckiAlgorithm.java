package com.javadruid.onerepmax.algorithms;

public class BrzyckiAlgorithm implements OneRepMaxAlgorithm {
    @Override
    public String name() {
        return "Brzycki";
    }

    @Override
    public String description() {
        return "Highly accurate for lower rep ranges. Excellent for heavy sets of 5 reps or fewer.";
    }

    @Override
    public double getOneRepMax(double weight, int reps) {
        if (reps >= 37) return weight; // Prevent division by zero or negative
        return weight * (36.0 / (37.0 - reps));
    }

    public String toString() {
        return name();
    }

}
