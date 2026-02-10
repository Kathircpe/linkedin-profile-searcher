package com.finder.core.controller;

import com.finder.core.service.ProfileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private ResponseEntity<?> searchAlumniProfiles(@RequestBody Map<String,String> body){
        return profileSearchService.searchAlumniProfiles(body);
    }

    @GetMapping("/all")
    private ResponseEntity<?> getSavedAlumniProfiles(){
        return profileSearchService.getSavedAlumniProfiles();
    }


}
