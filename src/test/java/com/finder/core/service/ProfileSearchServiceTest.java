package com.finder.core.service;

import com.finder.core.dto.SearchResponseBody;
import com.finder.core.model.LinkedInProfile;
import com.finder.core.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileSearchServiceTest {

    @Mock
    ProfileRepository profileRepository;
    @Mock
    ProfileFinder profileFinder;
    ProfileSearchService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new ProfileSearchService(profileRepository);
        // Use reflection to inject ProfileFinder since constructor doesn't expose it
        setPrivateField(service, "profileFinder", profileFinder);
    }

    @Test
    void searchAlumniProfiles_ValidInput() {
        Map<String, String> input = Map.of("university", "Stanford", "designation", "Engineer");
        List<LinkedInProfile> fakeProfiles = List.of(createSampleProfile());
        when(profileFinder.getProfile(anyString())).thenReturn(fakeProfiles);

        ResponseEntity<SearchResponseBody> response = service.searchAlumniProfiles(input);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("success", response.getBody().status);
        assertEquals(1, response.getBody().data.size());
        verify(profileFinder).getProfile(anyString());
    }

    @Test
    void searchAlumniProfiles_InvalidInput() {
        Map<String, String> input = Map.of("university", "");  // Empty required field

        ResponseEntity<SearchResponseBody> response = service.searchAlumniProfiles(input);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Failed(Invalid input)", response.getBody().status);
    }

    @Test
    void getSavedAlumniProfiles() {
        List<LinkedInProfile> fakeProfiles = List.of(createSampleProfile());
        when(profileRepository.findAll()).thenReturn(fakeProfiles);

        ResponseEntity<SearchResponseBody> response = service.getSavedAlumniProfiles();

        assertEquals("success", response.getBody().status);
        assertEquals(1, response.getBody().data.size());
    }

    private LinkedInProfile createSampleProfile() {
        LinkedInProfile profile = new LinkedInProfile();
        profile.setFullName("John Doe");
        profile.setUniversity("Stanford");
        return profile;
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}

