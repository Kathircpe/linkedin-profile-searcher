package com.finder.core.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    @Test
    void validInput_AllRequiredFieldsPresentAndNonEmpty() {
        Map<String, String> input = Map.of(
                "university", "Stanford",
                "designation", "Engineer",
                "passoutYear", "2023"
        );
        assertTrue(InputValidator.isValidInput(input));
    }

    @Test
    void validInput_OptionalFieldMissing() {
        Map<String, String> input = Map.of(
                "university", "MIT",
                "designation", "Scientist"
        );
        assertTrue(InputValidator.isValidInput(input));
    }

    @Test
    void validInput_OptionalFieldEmptyString() {
        Map<String, String> input = Map.of(
                "university", "Harvard",
                "designation", "Professor",
                "passoutYear", ""
        );
        assertFalse(InputValidator.isValidInput(input));
    }

    @ParameterizedTest
    @MethodSource("invalidRequiredFieldProvider")
    void invalidInput_MissingOrEmptyRequiredField(Map<String, String> input) {
        assertFalse(InputValidator.isValidInput(input));
    }

    static List<Arguments> invalidRequiredFieldProvider() {
        return Arrays.asList(
                Arguments.of(Map.of("designation", "Engineer")),  // Missing university
                Arguments.of(Map.of("university", "Stanford")),   // Missing designation
                Arguments.of(Map.of("university", "", "designation", "Engineer")),  // Empty university
                Arguments.of(Map.of("university", "Stanford", "designation", " ")), // Blank designation
                Arguments.of(Map.of("university", "Stanford", "designation", ""))   // Empty designation
        );
    }

    @Test
    void invalidInput_OptionalFieldWithOnlySpaces() {
        Map<String, String> input = Map.of(
                "university", "Stanford",
                "designation", "Engineer",
                "passoutYear", "   "
        );
        assertFalse(InputValidator.isValidInput(input));
    }

    @Test
    void nullInput_ThrowsException() {
        assertThrows(NullPointerException.class,
                () -> InputValidator.isValidInput(null));
    }
}
