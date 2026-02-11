package com.finder.core.service;

import com.finder.core.dto.SearchResponse;
import com.finder.core.dto.SearchResponseBody;
import com.finder.core.model.LinkedInProfile;
import com.finder.core.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProfileSearchService {

    private final ProfileFinder profileFinder;
    @Autowired
    private final ProfileRepository profileRepository;


    public ProfileSearchService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        this.profileFinder = new ProfileFinder();
    }

    /**
     * top level method which is called from the controller
     *
     * @param body
     * @return
     */
    public ResponseEntity<SearchResponseBody> searchAlumniProfiles(Map<String, String> body) {
        if (!InputValidator.isValidInput(body)) {
            return new ResponseEntity<>(new SearchResponseBody("Failed(Invalid input)", new ArrayList<>()),
                    HttpStatus.NOT_ACCEPTABLE);
        }
        List<SearchResponse> profiles = scrapeFromApi(body);
        return new ResponseEntity<>(new SearchResponseBody("success", profiles), HttpStatus.FOUND);
    }

    /**
     * top level method which is called from the controller
     *
     * @return
     */
    public ResponseEntity<SearchResponseBody> getSavedAlumniProfiles() {
        List<SearchResponse> profiles = getAllSavedAlumniProfiles();
        return new ResponseEntity<>(new SearchResponseBody("success", profiles), HttpStatus.FOUND);
    }

    /**
     * Scrapes the profiles from the linked in
     * and store it in the DB (Concurrently)
     *
     * @param body
     * @return
     */
    private List<SearchResponse> scrapeFromApi(Map<String, String> body) {

        String university = body.get("university"), designation = body.get("designation"), passoutYear =
                body.getOrDefault("passoutYear", "");
        String searchUrl = BaseUrlUtil.buildSearchUrl(university, designation, passoutYear);
        List<LinkedInProfile> profiles = profileFinder.getProfile(searchUrl);

        //new user Thread for concurrent task(saving the response in the db)
        Thread repoThread = new Thread(() -> profileRepository.saveAll(profiles));
        repoThread.start();

        return convertToResponse(profiles);
    }

    /**
     * finds all from the DB
     *
     * @return
     */
    private List<SearchResponse> getAllSavedAlumniProfiles() {
        List<LinkedInProfile> profiles = profileRepository.findAll();
        return convertToResponse(profiles);
    }

    /**
     * This method converts LinkedInProfile objects to SearchResponse objects for sending
     * as a response
     *
     * @param list
     * @return
     */
    private List<SearchResponse> convertToResponse(List<LinkedInProfile> list) {
        List<SearchResponse> profiles = list.stream().map(profile -> {
            SearchResponse response = new SearchResponse();
            response.name = profile.getFullName();
            response.currentRole = profile.getCurrentRole();
            response.university = profile.getUniversity();
            response.location = profile.getLocation();
            response.linkedInHeadLine = profile.getLinkedInHeadLine();
            response.passoutYear = profile.getPassoutYear();
            return response;
        }).toList();
        return profiles;
    }
}
