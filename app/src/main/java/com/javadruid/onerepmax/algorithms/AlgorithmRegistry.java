package com.javadruid.onerepmax.algorithms;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AlgorithmRegistry {
    public static final List<OneRepMaxAlgorithm> ALGORITHMS = List.of(
            new EpleyAlgorithm(),
            new BrzyckiAlgorithm(),
            new LanderAlgorithm(),
            new LombardiAlgorithm()
    );

    private static final Map<String, Integer> NAME_TO_INDEX = IntStream.range(0, ALGORITHMS.size())
            .boxed()
            .collect(Collectors.toMap(i -> ALGORITHMS.get(i).name(), i -> i));

    public int getAlgorithmIndex(String name) {
        return NAME_TO_INDEX.getOrDefault(name, 0);
    }

    public Optional<OneRepMaxAlgorithm> getAlgorithm(int index) {
        if(index < 0 || index >= ALGORITHMS.size())
            return Optional.empty();
        return Optional.of(ALGORITHMS.get(index));
    }

    public OneRepMaxAlgorithm getAlgorithm() {
        return getAlgorithm(0).get();
    }

    public List<OneRepMaxAlgorithm> getAlgorithms() {
        return Collections.unmodifiableList(ALGORITHMS);
    }

}
