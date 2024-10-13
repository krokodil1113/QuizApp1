package com.mycompany.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Answer;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.Quiz;
import com.mycompany.myapp.repository.AnswerRepository;
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.repository.QuizRepository;
import com.mycompany.myapp.service.dto.QuizDTO;
import com.mycompany.myapp.service.mapper.QuizMapper;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import main.java.com.mycompany.myapp.service.dtoLLM.LLMQuestionDTO;
import main.java.com.mycompany.myapp.service.dtoLLM.LLMQuizDTO;
import main.java.com.mycompany.myapp.service.dtoLLM.LLMQuizWrapperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Quiz}.
 */
@Service
@Transactional
public class QuizService {

    private final Logger log = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final QuizMapper quizMapper;

    private final WebClient webClient;

    public QuizService(
        QuizRepository quizRepository,
        QuestionRepository questionRepository,
        AnswerRepository answerRepository,
        QuizMapper quizMapper,
        WebClient.Builder webClientBuilder
    ) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizMapper = quizMapper;
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000").build();
    }

    /*  public Mono<Object> generateQuiz(String topic) {
        // Construct the URI and initiate the request
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
        Mono<Object> quizMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/process_topic")
                        .queryParam("topic", topic)
                        .build())
                .retrieve() // Initiate the request
                .bodyToMono(String.class) // Extract the body to Mono
                .doOnNext(response -> {
                    // Print the response, which is assumed to be JSON
                    System.out.println("Received JSON Response: " + response);
                })
                .map(response -> {
                    // Additional transformation if necessary
                    return response; // Return the processed response
                });
                System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBbbbbb");
        // Return the Mono containing the quiz data
        return quizMono;
    } */

    /*   public Mono<Object> generateQuiz(String topic) {
        System.out.println("**************************** " + topic);
        return webClient.get()
                .uri(uriBuilder -> {
                    // Build the URI with a path variable instead of a query parameter
                    URI uri = uriBuilder.path("/process_topic/{topic}")
                                        .build(topic); // Use the 'build' method with variable substitution
                    // Print the complete URI
                    System.out.println("Requesting URL: " + uri.toString());
                    return uri;
                })
                .header("Accept", "application/json")
                .retrieve() // Initiate the request
                .onStatus(status -> status.isError(), response -> 
                    Mono.error(new RuntimeException("Error occurred with status: " + response.statusCode())))
                .bodyToMono(String.class) // Extract the body to Mono
                .doOnNext(response -> {
                    // Print the response, which is assumed to be JSON
                    System.out.println("Received JSON Response: " + response);
                })
                .map(response -> {
                    // Additional transformation if necessary
                    return response; // Return the processed response
                });
    } */

    public LLMQuizDTO generateQuizData(String topic) {
        System.out.println("**************************** " + topic);
        try {
            String responseBody = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/process_topic").queryParam("topic", topic).build())
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(
                    status -> status.isError(),
                    clientResponse -> Mono.error(new RuntimeException("Error occurred with status: " + clientResponse.statusCode()))
                )
                .bodyToMono(String.class)
                .block();

            System.out.println("Received JSON Response: " + responseBody);

            ObjectMapper mapper = new ObjectMapper();
            LLMQuizWrapperDTO quizWrapper = mapper.readValue(responseBody, LLMQuizWrapperDTO.class);
            String nestedJson = quizWrapper.getMessage(); // this should be the actual JSON string

            // Parse the nested JSON string
            LLMQuizDTO quiz = mapper.readValue(nestedJson, LLMQuizDTO.class);
            printQuizDetails(quiz);

            return quiz;
        } catch (Exception e) {
            System.err.println("Error during API call: " + e.getMessage());
            throw new RuntimeException("Failed to generate quiz due to server error.", e);
        }
    }

    @Transactional
    public Quiz createAndSaveQuizFromExternalSource(String topic) {
        try {
            LLMQuizDTO quizData = generateQuizData(topic); // This now correctly receives a DTO

            // Create a new Quiz entity
            Quiz quiz = new Quiz();
            quiz.setTitle(topic);
            quiz.setIsPublished(true);
            quiz.setPublishDate(LocalDate.now());
            quizRepository.save(quiz);

            // Process and save each question and its answers
            for (LLMQuestionDTO questionDTO : quizData.getQuestions()) {
                Question question = new Question();
                question.setText(questionDTO.getQuestion());
                question.setQuiz(quiz);
                questionRepository.save(question);

                for (String option : questionDTO.getOptions()) {
                    Answer answer = new Answer();
                    answer.setText(option);
                    answer.setIsCorrect(option.equals(questionDTO.getCorrectAnswer()));
                    answer.setQuestion(question);
                    answerRepository.save(answer);
                }
            }

            return quiz; // Return the newly created quiz with ID
        } catch (Exception e) {
            throw new RuntimeException("Failed to create quiz", e);
        }
    }

    private void printQuizDetails(LLMQuizDTO quiz) {
        System.out.println("Quiz Data:");
        for (LLMQuestionDTO question : quiz.getQuestions()) {
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Options: " + question.getOptions());
            System.out.println("Correct Answer: " + question.getCorrectAnswer());
        }
    }

    /**
     * Save a quiz.
     *
     * @param quizDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizDTO save(QuizDTO quizDTO) {
        log.debug("Request to save Quiz : {}", quizDTO);
        Quiz quiz = quizMapper.toEntity(quizDTO);
        quiz = quizRepository.save(quiz);
        return quizMapper.toDto(quiz);
    }

    /**
     * Update a quiz.
     *
     * @param quizDTO the entity to save.
     * @return the persisted entity.
     */
    public QuizDTO update(QuizDTO quizDTO) {
        log.debug("Request to update Quiz : {}", quizDTO);
        Quiz quiz = quizMapper.toEntity(quizDTO);
        quiz = quizRepository.save(quiz);
        return quizMapper.toDto(quiz);
    }

    /**
     * Partially update a quiz.
     *
     * @param quizDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuizDTO> partialUpdate(QuizDTO quizDTO) {
        log.debug("Request to partially update Quiz : {}", quizDTO);

        return quizRepository
            .findById(quizDTO.getId())
            .map(existingQuiz -> {
                quizMapper.partialUpdate(existingQuiz, quizDTO);

                return existingQuiz;
            })
            .map(quizRepository::save)
            .map(quizMapper::toDto);
    }

    /**
     * Get all the quizzes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Quizzes");
        return quizRepository.findAll(pageable).map(quizMapper::toDto);
    }

    /**
     * Get one quiz by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuizDTO> findOne(Long id) {
        log.debug("Request to get Quiz : {}", id);
        return quizRepository.findById(id).map(quizMapper::toDto);
    }

    /**
     * Delete the quiz by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Quiz : {}", id);
        quizRepository.deleteById(id);
    }
}
