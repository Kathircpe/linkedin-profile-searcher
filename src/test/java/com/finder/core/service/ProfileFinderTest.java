package com.finder.core.service;

import com.finder.core.model.LinkedInProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileFinderTest {

    @Mock
    PhantomClient phantomClient;
    ProfileFinder finder;

    @BeforeEach
    void setUp() throws Exception {
        finder = new ProfileFinder();
        setPrivateField(finder, "phantomClient", phantomClient);
    }

    @Test
    void getProfile_ReduceUrlsToThreshold() {
        List<String> manyUrls = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            manyUrls.add("https://linkedin.com/profile/" + i);
        }

        when(phantomClient.launchSearchExport(anyString())).thenReturn(manyUrls);
        when(phantomClient.launchProfileScraper(anyList())).thenReturn("scraper123");
        when(phantomClient.pollProfileResults("scraper123")).thenReturn(List.of(createSampleProfile()));

        List<LinkedInProfile> result = finder.getProfile("https://linkedin.com/search");

        assertEquals(1, result.size());
        verify(phantomClient).launchSearchExport(anyString());
        verify(phantomClient).pollProfileResults("scraper123");
    }

    private LinkedInProfile createSampleProfile() {
        LinkedInProfile p = new LinkedInProfile();
        p.setFullName("Test User");
        return p;
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
