package com.finder.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BaseUrlUtilTest {

    @Test
    @DisplayName("Encodes spaces correctly")
    void swapSpaceWithReplacement() {
        assertEquals("Stanford%20University",
                BaseUrlUtil.swapSpaceWithReplacement("Stanford University"));
        assertEquals("Software%20Engineer",
                BaseUrlUtil.swapSpaceWithReplacement("Software Engineer"));
        assertEquals("2023",
                BaseUrlUtil.swapSpaceWithReplacement("2023"));  // No spaces
    }

    @Test
    @DisplayName("Builds URL without year")
    void buildSearchUrl_NoYear() {
        String url = BaseUrlUtil.buildSearchUrl("Stanford University", "Software Engineer");
        assertEquals("https://www.linkedin.com/search/results/people/?keywords=Software%20Engineer+Stanford%20University", url);
    }

    @Test
    @DisplayName("Builds URL with year")
    void buildSearchUrl_WithYear() {
        String url = BaseUrlUtil.buildSearchUrl("Stanford University", "Software Engineer", "2023");
        assertEquals("https://www.linkedin.com/search/results/people/?keywords=Software%20Engineer+Stanford%20University+2023", url);
    }

    @Test
    @DisplayName("Ignores empty year")
    void buildSearchUrl_EmptyYear() {
        String url = BaseUrlUtil.buildSearchUrl("Stanford University", "Software Engineer", "");
        assertFalse(url.contains("+%"));  // No year in URL
        assertTrue(url.contains("Software%20Engineer+Stanford%20University"));
    }

    @Test
    @DisplayName("Ignores blank year")
    void buildSearchUrl_BlankYear() {
        String url = BaseUrlUtil.buildSearchUrl("MIT", "Data Scientist", "   ");
        assertEquals("https://www.linkedin.com/search/results/people/?keywords=Data%20Scientist+MIT", url);
    }

}

