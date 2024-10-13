package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GameSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static GameSession getGameSessionSample1() {
        return new GameSession().id(1L).status("status1").currentQuestionIndex(1);
    }

    public static GameSession getGameSessionSample2() {
        return new GameSession().id(2L).status("status2").currentQuestionIndex(2);
    }

    public static GameSession getGameSessionRandomSampleGenerator() {
        return new GameSession()
            .id(longCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .currentQuestionIndex(intCount.incrementAndGet());
    }
}
