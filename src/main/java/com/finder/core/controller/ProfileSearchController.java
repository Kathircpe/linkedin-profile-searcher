package com.finder.core.controller;

import com.finder.core.dto.SearchResponseBody;
import com.finder.core.model.LinkedInProfile;
import com.finder.core.repository.ProfileRepository;
import com.finder.core.service.ProfileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumni")
public class ProfileSearchController {

    @Autowired
    private final ProfileSearchService profileSearchService;

    public ProfileSearchController(ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    @PostMapping("/search")
    private ResponseEntity<SearchResponseBody> searchAlumniProfiles(@RequestBody Map<String, String> body) {
        return profileSearchService.searchAlumniProfiles(body);
    }

    @GetMapping("/all")
    private ResponseEntity<SearchResponseBody> getSavedAlumniProfiles() {
        return profileSearchService.getSavedAlumniProfiles();
    }



}
