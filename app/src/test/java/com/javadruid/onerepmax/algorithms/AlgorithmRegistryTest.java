package com.javadruid.onerepmax.algorithms;

import junit.framework.TestCase;

import java.util.List;
import java.util.Optional;

public class AlgorithmRegistryTest extends TestCase {

    public void testGetAlgorithmIndex() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        int result = instance.getAlgorithmIndex("Brzycki");

        assertEquals(1, result);
    }

    public void testGetAlgorithmIndex_default() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        int result = instance.getAlgorithmIndex("");

        assertEquals(0, result);
    }

    public void testGetAlgorithmIndex_non_existing() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        final int result = instance.getAlgorithmIndex("THIS_IS_NOT_AN_ALGORITHM");

        assertEquals(0, result);
    }

    public void testGetAlgorithm_index() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        final OneRepMaxAlgorithm result = instance.getAlgorithm(1).get();

        assertEquals("Brzycki", result.name());
    }

    public void testGetAlgorithm_index_negative() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        final Optional<OneRepMaxAlgorithm> result = instance.getAlgorithm(-1);

        assertTrue(result.isEmpty());
    }

    public void testGetAlgorithm_index_above() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        final Optional<OneRepMaxAlgorithm> result = instance.getAlgorithm(Integer.MAX_VALUE);

        assertTrue(result.isEmpty());
    }

    public void testGetAlgorithm() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        final OneRepMaxAlgorithm result = instance.getAlgorithm();

        assertEquals("Epley", result.name());
    }

    public void testGetAlgorithms() {
        final AlgorithmRegistry instance = new AlgorithmRegistry();

        final List<OneRepMaxAlgorithm> result = instance.getAlgorithms();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}