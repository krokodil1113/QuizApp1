package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAnswer.
 */
@Entity
@Table(name = "user_answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "custom_answer_text")
    private String customAnswerText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quiz", "user" }, allowSetters = true)
    private QuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quiz" }, allowSetters = true)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Answer selectedAnswer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAnswer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomAnswerText() {
        return this.customAnswerText;
    }

    public UserAnswer customAnswerText(String customAnswerText) {
        this.setCustomAnswerText(customAnswerText);
        return this;
    }

    public void setCustomAnswerText(String customAnswerText) {
        this.customAnswerText = customAnswerText;
    }

    public QuizAttempt getAttempt() {
        return this.attempt;
    }

    public void setAttempt(QuizAttempt quizAttempt) {
        this.attempt = quizAttempt;
    }

    public UserAnswer attempt(QuizAttempt quizAttempt) {
        this.setAttempt(quizAttempt);
        return this;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public UserAnswer question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public Answer getSelectedAnswer() {
        return this.selectedAnswer;
    }

    public void setSelectedAnswer(Answer answer) {
        this.selectedAnswer = answer;
    }

    public UserAnswer selectedAnswer(Answer answer) {
        this.setSelectedAnswer(answer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAnswer)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAnswer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAnswer{" +
            "id=" + getId() +
            ", customAnswerText='" + getCustomAnswerText() + "'" +
            "}";
    }
}
