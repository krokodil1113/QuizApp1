package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuizUser.
 */
@Entity
@Table(name = "quiz_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Lob
    @Column(name = "bio")
    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "quizUser" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "quizUser")
    private UserStatistics userStatistics;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuizUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public QuizUser nickname(String nickname) {
        this.setNickname(nickname);
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return this.bio;
    }

    public QuizUser bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public QuizUser user(User user) {
        this.setUser(user);
        return this;
    }

    public UserStatistics getUserStatistics() {
        return this.userStatistics;
    }

    public void setUserStatistics(UserStatistics userStatistics) {
        if (this.userStatistics != null) {
            this.userStatistics.setQuizUser(null);
        }
        if (userStatistics != null) {
            userStatistics.setQuizUser(this);
        }
        this.userStatistics = userStatistics;
    }

    public QuizUser userStatistics(UserStatistics userStatistics) {
        this.setUserStatistics(userStatistics);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizUser)) {
            return false;
        }
        return getId() != null && getId().equals(((QuizUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizUser{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", bio='" + getBio() + "'" +
            "}";
    }
}
