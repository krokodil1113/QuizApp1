package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Quiz} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String description;

    private Integer difficultyLevel;

    @NotNull
    private Boolean isPublished;

    private LocalDate publishDate;

    private QuizAnalyticsDTO quizAnalytics;

    private QuizUserDTO creator;

    private CategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public QuizAnalyticsDTO getQuizAnalytics() {
        return quizAnalytics;
    }

    public void setQuizAnalytics(QuizAnalyticsDTO quizAnalytics) {
        this.quizAnalytics = quizAnalytics;
    }

    public QuizUserDTO getCreator() {
        return creator;
    }

    public void setCreator(QuizUserDTO creator) {
        this.creator = creator;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizDTO)) {
            return false;
        }

        QuizDTO quizDTO = (QuizDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", difficultyLevel=" + getDifficultyLevel() +
            ", isPublished='" + getIsPublished() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", quizAnalytics=" + getQuizAnalytics() +
            ", creator=" + getCreator() +
            ", category=" + getCategory() +
            "}";
    }
}
