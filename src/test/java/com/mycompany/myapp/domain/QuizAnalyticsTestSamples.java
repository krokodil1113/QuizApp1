package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuizAnalyticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuizAnalytics getQuizAnalyticsSample1() {
        return new QuizAnalytics().id(1L).totalAttempts(1);
    }

    public static QuizAnalytics getQuizAnalyticsSample2() {
        return new QuizAnalytics().id(2L).totalAttempts(2);
    }

    public static QuizAnalytics getQuizAnalyticsRandomSampleGenerator() {
        return new QuizAnalytics().id(longCount.incrementAndGet()).totalAttempts(intCount.incrementAndGet());
    }
}
