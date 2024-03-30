package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuizRatingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuizRating getQuizRatingSample1() {
        return new QuizRating().id(1L).rating(1);
    }

    public static QuizRating getQuizRatingSample2() {
        return new QuizRating().id(2L).rating(2);
    }

    public static QuizRating getQuizRatingRandomSampleGenerator() {
        return new QuizRating().id(longCount.incrementAndGet()).rating(intCount.incrementAndGet());
    }
}
