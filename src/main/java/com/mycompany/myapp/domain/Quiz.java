package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quiz.
 */
@Entity
@Table(name = "quiz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    @NotNull
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @JsonIgnoreProperties(value = { "quiz" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private QuizAnalytics quizAnalytics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "userStatistics" }, allowSetters = true)
    private QuizUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quizzes" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quiz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Quiz title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Quiz description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDifficultyLevel() {
        return this.difficultyLevel;
    }

    public Quiz difficultyLevel(Integer difficultyLevel) {
        this.setDifficultyLevel(difficultyLevel);
        return this;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Boolean getIsPublished() {
        return this.isPublished;
    }

    public Quiz isPublished(Boolean isPublished) {
        this.setIsPublished(isPublished);
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public LocalDate getPublishDate() {
        return this.publishDate;
    }

    public Quiz publishDate(LocalDate publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public QuizAnalytics getQuizAnalytics() {
        return this.quizAnalytics;
    }

    public void setQuizAnalytics(QuizAnalytics quizAnalytics) {
        this.quizAnalytics = quizAnalytics;
    }

    public Quiz quizAnalytics(QuizAnalytics quizAnalytics) {
        this.setQuizAnalytics(quizAnalytics);
        return this;
    }

    public QuizUser getCreator() {
        return this.creator;
    }

    public void setCreator(QuizUser quizUser) {
        this.creator = quizUser;
    }

    public Quiz creator(QuizUser quizUser) {
        this.setCreator(quizUser);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Quiz category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        return getId() != null && getId().equals(((Quiz) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quiz{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", difficultyLevel=" + getDifficultyLevel() +
            ", isPublished='" + getIsPublished() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            "}";
    }
}
