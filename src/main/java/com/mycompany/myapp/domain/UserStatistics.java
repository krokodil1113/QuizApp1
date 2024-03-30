package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserStatistics.
 */
@Entity
@Table(name = "user_statistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "total_quizzes_taken")
    private Integer totalQuizzesTaken;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "average_score")
    private Double averageScore;

    @JsonIgnoreProperties(value = { "user", "userStatistics" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private QuizUser quizUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserStatistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalQuizzesTaken() {
        return this.totalQuizzesTaken;
    }

    public UserStatistics totalQuizzesTaken(Integer totalQuizzesTaken) {
        this.setTotalQuizzesTaken(totalQuizzesTaken);
        return this;
    }

    public void setTotalQuizzesTaken(Integer totalQuizzesTaken) {
        this.totalQuizzesTaken = totalQuizzesTaken;
    }

    public Integer getTotalScore() {
        return this.totalScore;
    }

    public UserStatistics totalScore(Integer totalScore) {
        this.setTotalScore(totalScore);
        return this;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Double getAverageScore() {
        return this.averageScore;
    }

    public UserStatistics averageScore(Double averageScore) {
        this.setAverageScore(averageScore);
        return this;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public QuizUser getQuizUser() {
        return this.quizUser;
    }

    public void setQuizUser(QuizUser quizUser) {
        this.quizUser = quizUser;
    }

    public UserStatistics quizUser(QuizUser quizUser) {
        this.setQuizUser(quizUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserStatistics)) {
            return false;
        }
        return getId() != null && getId().equals(((UserStatistics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserStatistics{" +
            "id=" + getId() +
            ", totalQuizzesTaken=" + getTotalQuizzesTaken() +
            ", totalScore=" + getTotalScore() +
            ", averageScore=" + getAverageScore() +
            "}";
    }
}
