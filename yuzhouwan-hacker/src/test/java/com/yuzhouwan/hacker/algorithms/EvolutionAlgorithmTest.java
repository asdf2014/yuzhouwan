package com.yuzhouwan.hacker.algorithms;

import com.circuitwall.ml.algorithm.evolution.EvolutionAlgorithm;
import com.circuitwall.ml.algorithm.util.RandomUtil;
import com.circuitwall.ml.platform.monolithic.evolution.LocalExecutor;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Evolution Algorithm Test
 *
 * @author Benedict Jin
 * @since 2017/2/16
 */
public class EvolutionAlgorithmTest implements EvolutionAlgorithm {

    private boolean bestChildFound = false;

    @Override
    public void processBestChild(Comparable[] bestChild, int round, double score) {
        if (!bestChildFound) {
            if (score == 0) {
                System.out.println("Best child found in round:" + round);
                bestChildFound = true;
            } else {
                System.out.println("Best child round:" + round + " score:" + scoreIndividual(bestChild) + " config:" + Arrays.toString(bestChild));
            }
        }
    }

    @Override
    public Comparable[] generateParent() {
        return new Comparable[]{
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100,
        };
    }

    @Override
    public Comparable[] mutate(Comparable[] orig) {
        orig[RandomUtil.getRandom().nextInt(orig.length)] = RandomUtil.getRandom().nextInt(100);
        return orig;
    }

    @Override
    public Double scoreIndividual(Comparable[] individual) {
        return 0 - Stream.of(individual).map(Integer.class::cast).mapToDouble(Integer::doubleValue).sum();
    }

    @Test
    public void test() {
        EvolutionAlgorithmTest simulator = new EvolutionAlgorithmTest();
        new LocalExecutor().execute(simulator, 1000, 5000, 100, 5D);
    }
}