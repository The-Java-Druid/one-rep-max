package com.javadruid.onerepmax.algorithms;

public interface OneRepMaxAlgorithm {

    String name();
    String description();
    double getOneRepMax(double weight, int reps);

}
