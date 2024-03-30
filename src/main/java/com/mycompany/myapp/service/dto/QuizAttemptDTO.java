package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.QuizAttempt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizAttemptDTO implements Serializable {

    private Long id;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private Integer score;

    private QuizDTO quiz;

    private QuizUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public QuizDTO getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizDTO quiz) {
        this.quiz = quiz;
    }

    public QuizUserDTO getUser() {
        return user;
    }

    public void setUser(QuizUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizAttemptDTO)) {
            return false;
        }

        QuizAttemptDTO quizAttemptDTO = (QuizAttemptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizAttemptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizAttemptDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", score=" + getScore() +
            ", quiz=" + getQuiz() +
            ", user=" + getUser() +
            "}";
    }
}
