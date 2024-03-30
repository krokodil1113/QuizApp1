package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.QuizAnalytics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizAnalyticsDTO implements Serializable {

    private Long id;

    private Integer totalAttempts;

    private Double averageScore;

    private Double completionRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(Integer totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizAnalyticsDTO)) {
            return false;
        }

        QuizAnalyticsDTO quizAnalyticsDTO = (QuizAnalyticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizAnalyticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizAnalyticsDTO{" +
            "id=" + getId() +
            ", totalAttempts=" + getTotalAttempts() +
            ", averageScore=" + getAverageScore() +
            ", completionRate=" + getCompletionRate() +
            "}";
    }
}
