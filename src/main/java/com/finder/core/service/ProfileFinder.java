package com.finder.core.service;

import com.finder.core.model.LinkedInProfile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class handles scraping profile urls and
 * finding profile metadata using phantom client.
 * Top level controller for the PhantomClient
 */
@Service
public class ProfileFinder {
    private final PhantomClient phantomClient;
    //threshold is to set a standard output for the api call. Currently, it is very low since it is easy for assessment.
    static final int MAX_THRESHOLD=20;

    public ProfileFinder() {
        this.phantomClient = new PhantomClient();
    }

    public List<LinkedInProfile> getProfile(String searchUrl) {
        // Get profile URLs from search
        List<String> profileUrls = phantomClient.launchSearchExport(searchUrl);
        profileUrls = reduceTheUrls(profileUrls);

        // Launch profile scraper
        String scraperId = phantomClient.launchProfileScraper(profileUrls);

        // Get full enriched data
        List<LinkedInProfile> profiles = phantomClient.pollProfileResults(scraperId);

        return profiles;

    }

    private List<String> reduceTheUrls(List<String> profileUrls) {
        List<String> temp = profileUrls.stream()
                                        .limit(MAX_THRESHOLD)
                                        .toList();
        return temp;
    }

}
