package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserStatistics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserStatisticsDTO implements Serializable {

    private Long id;

    private Integer totalQuizzesTaken;

    private Integer totalScore;

    private Double averageScore;

    private QuizUserDTO quizUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalQuizzesTaken() {
        return totalQuizzesTaken;
    }

    public void setTotalQuizzesTaken(Integer totalQuizzesTaken) {
        this.totalQuizzesTaken = totalQuizzesTaken;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public QuizUserDTO getQuizUser() {
        return quizUser;
    }

    public void setQuizUser(QuizUserDTO quizUser) {
        this.quizUser = quizUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserStatisticsDTO)) {
            return false;
        }

        UserStatisticsDTO userStatisticsDTO = (UserStatisticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userStatisticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserStatisticsDTO{" +
            "id=" + getId() +
            ", totalQuizzesTaken=" + getTotalQuizzesTaken() +
            ", totalScore=" + getTotalScore() +
            ", averageScore=" + getAverageScore() +
            ", quizUser=" + getQuizUser() +
            "}";
    }
}
