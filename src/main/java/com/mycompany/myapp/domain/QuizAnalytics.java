package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuizAnalytics.
 */
@Entity
@Table(name = "quiz_analytics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizAnalytics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "total_attempts")
    private Integer totalAttempts;

    @Column(name = "average_score")
    private Double averageScore;

    @Column(name = "completion_rate")
    private Double completionRate;

    @JsonIgnoreProperties(value = { "quizAnalytics", "creator", "category" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "quizAnalytics")
    private Quiz quiz;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuizAnalytics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalAttempts() {
        return this.totalAttempts;
    }

    public QuizAnalytics totalAttempts(Integer totalAttempts) {
        this.setTotalAttempts(totalAttempts);
        return this;
    }

    public void setTotalAttempts(Integer totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public Double getAverageScore() {
        return this.averageScore;
    }

    public QuizAnalytics averageScore(Double averageScore) {
        this.setAverageScore(averageScore);
        return this;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getCompletionRate() {
        return this.completionRate;
    }

    public QuizAnalytics completionRate(Double completionRate) {
        this.setCompletionRate(completionRate);
        return this;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {
        if (this.quiz != null) {
            this.quiz.setQuizAnalytics(null);
        }
        if (quiz != null) {
            quiz.setQuizAnalytics(this);
        }
        this.quiz = quiz;
    }

    public QuizAnalytics quiz(Quiz quiz) {
        this.setQuiz(quiz);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizAnalytics)) {
            return false;
        }
        return getId() != null && getId().equals(((QuizAnalytics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizAnalytics{" +
            "id=" + getId() +
            ", totalAttempts=" + getTotalAttempts() +
            ", averageScore=" + getAverageScore() +
            ", completionRate=" + getCompletionRate() +
            "}";
    }
}
