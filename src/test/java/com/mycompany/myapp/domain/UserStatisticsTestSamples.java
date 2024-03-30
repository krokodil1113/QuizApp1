package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserStatisticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserStatistics getUserStatisticsSample1() {
        return new UserStatistics().id(1L).totalQuizzesTaken(1).totalScore(1);
    }

    public static UserStatistics getUserStatisticsSample2() {
        return new UserStatistics().id(2L).totalQuizzesTaken(2).totalScore(2);
    }

    public static UserStatistics getUserStatisticsRandomSampleGenerator() {
        return new UserStatistics()
            .id(longCount.incrementAndGet())
            .totalQuizzesTaken(intCount.incrementAndGet())
            .totalScore(intCount.incrementAndGet());
    }
}
