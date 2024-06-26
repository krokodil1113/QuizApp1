package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UserStatisticsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserStatisticsAllPropertiesEquals(UserStatistics expected, UserStatistics actual) {
        assertUserStatisticsAutoGeneratedPropertiesEquals(expected, actual);
        assertUserStatisticsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserStatisticsAllUpdatablePropertiesEquals(UserStatistics expected, UserStatistics actual) {
        assertUserStatisticsUpdatableFieldsEquals(expected, actual);
        assertUserStatisticsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserStatisticsAutoGeneratedPropertiesEquals(UserStatistics expected, UserStatistics actual) {
        assertThat(expected)
            .as("Verify UserStatistics auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserStatisticsUpdatableFieldsEquals(UserStatistics expected, UserStatistics actual) {
        assertThat(expected)
            .as("Verify UserStatistics relevant properties")
            .satisfies(e -> assertThat(e.getTotalQuizzesTaken()).as("check totalQuizzesTaken").isEqualTo(actual.getTotalQuizzesTaken()))
            .satisfies(e -> assertThat(e.getTotalScore()).as("check totalScore").isEqualTo(actual.getTotalScore()))
            .satisfies(e -> assertThat(e.getAverageScore()).as("check averageScore").isEqualTo(actual.getAverageScore()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserStatisticsUpdatableRelationshipsEquals(UserStatistics expected, UserStatistics actual) {
        assertThat(expected)
            .as("Verify UserStatistics relationships")
            .satisfies(e -> assertThat(e.getQuizUser()).as("check quizUser").isEqualTo(actual.getQuizUser()));
    }
}
