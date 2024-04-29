package main.java.com.mycompany.myapp.service.dtoLLM;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class LLMQuestionDTO {

    private String question;
    private List<String> options;

    @JsonProperty("correct_answer")
    private String correctAnswer;

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
