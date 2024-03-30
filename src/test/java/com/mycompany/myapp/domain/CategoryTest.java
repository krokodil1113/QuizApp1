package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoryTestSamples.*;
import static com.mycompany.myapp.domain.QuizTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void quizTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        category.addQuiz(quizBack);
        assertThat(category.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getCategory()).isEqualTo(category);

        category.removeQuiz(quizBack);
        assertThat(category.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getCategory()).isNull();

        category.quizzes(new HashSet<>(Set.of(quizBack)));
        assertThat(category.getQuizzes()).containsOnly(quizBack);
        assertThat(quizBack.getCategory()).isEqualTo(category);

        category.setQuizzes(new HashSet<>());
        assertThat(category.getQuizzes()).doesNotContain(quizBack);
        assertThat(quizBack.getCategory()).isNull();
    }
}
