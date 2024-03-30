package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserAnswer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAnswerDTO implements Serializable {

    private Long id;

    private String customAnswerText;

    private QuizAttemptDTO attempt;

    private QuestionDTO question;

    private AnswerDTO selectedAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomAnswerText() {
        return customAnswerText;
    }

    public void setCustomAnswerText(String customAnswerText) {
        this.customAnswerText = customAnswerText;
    }

    public QuizAttemptDTO getAttempt() {
        return attempt;
    }

    public void setAttempt(QuizAttemptDTO attempt) {
        this.attempt = attempt;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public AnswerDTO getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(AnswerDTO selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAnswerDTO)) {
            return false;
        }

        UserAnswerDTO userAnswerDTO = (UserAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAnswerDTO{" +
            "id=" + getId() +
            ", customAnswerText='" + getCustomAnswerText() + "'" +
            ", attempt=" + getAttempt() +
            ", question=" + getQuestion() +
            ", selectedAnswer=" + getSelectedAnswer() +
            "}";
    }
}
