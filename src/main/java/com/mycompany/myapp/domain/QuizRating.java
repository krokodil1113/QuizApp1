package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuizRating.
 */
@Entity
@Table(name = "quiz_rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rating")
    private Integer rating;

    @Lob
    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quizAnalytics", "creator", "category" }, allowSetters = true)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "userStatistics" }, allowSetters = true)
    private QuizUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuizRating id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public QuizRating rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public QuizRating comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public QuizRating quiz(Quiz quiz) {
        this.setQuiz(quiz);
        return this;
    }

    public QuizUser getUser() {
        return this.user;
    }

    public void setUser(QuizUser quizUser) {
        this.user = quizUser;
    }

    public QuizRating user(QuizUser quizUser) {
        this.setUser(quizUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizRating)) {
            return false;
        }
        return getId() != null && getId().equals(((QuizRating) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizRating{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
