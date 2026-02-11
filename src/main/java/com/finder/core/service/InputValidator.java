package com.finder.core.service;

import java.util.Map;

/**
 * Contains static methods to validate the user input or user request
 */
public final class InputValidator {
    //optional and non-optional fields for validation
    private static final String[] nonOptionalFieldsForProfileSearch = {"university", "designation"};
    private static final String[] optionalFieldsForProfileSearch = {"passoutYear"};

    static boolean isValidInput(Map<String, String> input) {

        for (String field : nonOptionalFieldsForProfileSearch) {
            boolean isValid = input.containsKey(field)
                                && !input.get(field).isEmpty()
                                && !input.get(field).isBlank();

            if (!isValid) return false;
        }
        for (String field : optionalFieldsForProfileSearch) {
            boolean isValid = !input.containsKey(field) ||
                                (!input.get(field).isEmpty() && !input.get(field).isBlank());

            if (!isValid) return false;
        }

        return true;
    }

}
