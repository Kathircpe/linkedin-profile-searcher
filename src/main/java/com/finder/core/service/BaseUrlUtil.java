package com.finder.core.service;

/**
 * This class contains static methods which creates search link for the given input
 */
public final class BaseUrlUtil {

    public static String buildSearchUrl(String university, String designation, String passoutYear) {

        university = swapSpaceWithReplacement(university);
        designation = swapSpaceWithReplacement(designation);

        if (passoutYear.isEmpty() || passoutYear.isBlank()) {
            return buildSearchUrl(university, designation);
        }

        passoutYear = swapSpaceWithReplacement(passoutYear);

        return String.format(
                "https://www.linkedin.com/search/results/people/?keywords=%s+%s+%s",
                designation, university, passoutYear);
    }

    public static String buildSearchUrl(String university, String designation) {
        return String.format(
                "https://www.linkedin.com/search/results/people/?keywords=%s+%s",
                designation, university);
    }

    private static String swapSpaceWithReplacement(String str) {
        return str.replace(" ", "%20");
    }
}
