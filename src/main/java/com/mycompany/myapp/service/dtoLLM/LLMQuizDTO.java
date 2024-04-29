package main.java.com.mycompany.myapp.service.dtoLLM;

import java.util.List;

public class LLMQuizDTO {

    private List<LLMQuestionDTO> questions;

    public List<LLMQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<LLMQuestionDTO> questions) {
        this.questions = questions;
    }
}
