package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GameSessionCriteriaTest {

    @Test
    void newGameSessionCriteriaHasAllFiltersNullTest() {
        var gameSessionCriteria = new GameSessionCriteria();
        assertThat(gameSessionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void gameSessionCriteriaFluentMethodsCreatesFiltersTest() {
        var gameSessionCriteria = new GameSessionCriteria();

        setAllFilters(gameSessionCriteria);

        assertThat(gameSessionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void gameSessionCriteriaCopyCreatesNullFilterTest() {
        var gameSessionCriteria = new GameSessionCriteria();
        var copy = gameSessionCriteria.copy();

        assertThat(gameSessionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(gameSessionCriteria)
        );
    }

    @Test
    void gameSessionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var gameSessionCriteria = new GameSessionCriteria();
        setAllFilters(gameSessionCriteria);

        var copy = gameSessionCriteria.copy();

        assertThat(gameSessionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(gameSessionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var gameSessionCriteria = new GameSessionCriteria();

        assertThat(gameSessionCriteria).hasToString("GameSessionCriteria{}");
    }

    private static void setAllFilters(GameSessionCriteria gameSessionCriteria) {
        gameSessionCriteria.id();
        gameSessionCriteria.startTime();
        gameSessionCriteria.endTime();
        gameSessionCriteria.status();
        gameSessionCriteria.currentQuestionIndex();
        gameSessionCriteria.distinct();
    }

    private static Condition<GameSessionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartTime()) &&
                condition.apply(criteria.getEndTime()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCurrentQuestionIndex()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GameSessionCriteria> copyFiltersAre(GameSessionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartTime(), copy.getStartTime()) &&
                condition.apply(criteria.getEndTime(), copy.getEndTime()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCurrentQuestionIndex(), copy.getCurrentQuestionIndex()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
