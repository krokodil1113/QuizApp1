package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.QuizRating} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizRatingDTO implements Serializable {

    private Long id;

    private Integer rating;

    @Lob
    private String comment;

    private QuizDTO quiz;

    private QuizUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        if (!(o instanceof QuizRatingDTO)) {
            return false;
        }

        QuizRatingDTO quizRatingDTO = (QuizRatingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizRatingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizRatingDTO{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            ", quiz=" + getQuiz() +
            ", user=" + getUser() +
            "}";
    }
}
