package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.QuizUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizUserDTO implements Serializable {

    private Long id;

    private String nickname;

    @Lob
    private String bio;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizUserDTO)) {
            return false;
        }

        QuizUserDTO quizUserDTO = (QuizUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizUserDTO{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", bio='" + getBio() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
