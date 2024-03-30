package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryAllPropertiesEquals(Category expected, Category actual) {
        assertCategoryAutoGeneratedPropertiesEquals(expected, actual);
        assertCategoryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryAllUpdatablePropertiesEquals(Category expected, Category actual) {
        assertCategoryUpdatableFieldsEquals(expected, actual);
        assertCategoryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryAutoGeneratedPropertiesEquals(Category expected, Category actual) {
        assertThat(expected)
            .as("Verify Category auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryUpdatableFieldsEquals(Category expected, Category actual) {
        assertThat(expected)
            .as("Verify Category relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryUpdatableRelationshipsEquals(Category expected, Category actual) {}
}
