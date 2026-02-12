package com.finder.core.service;


import com.finder.core.config.LinkedIn;
import com.finder.core.config.PhantomBusterApi;
import com.finder.core.model.LinkedInProfile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class handles the profile scraping from phantom buster Api.
 * Methods are efficiently implemented.
 */

public final class PhantomClient {
    //This class is just about phantom Buster Api
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String BASE_URL;
    private final String linkedInCookie;

    public PhantomClient() {
        this.restTemplate = new RestTemplate();
        linkedInCookie = LinkedIn.getLinkedInCookie();
        apiKey = PhantomBusterApi.getApiKey();
        BASE_URL = PhantomBusterApi.getBaseUrl();
    }

    public List<String> launchSearchExport(String searchUrl) {
        String json = """
                {"id": "%s", "bonusArguments": {"searchUrl": "%s"}}
                """.formatted(PhantomBusterApi.profileUrlSearchId,searchUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Phantombuster-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_URL + "/agents/launch", entity, String.class);

        JsonObject jsonResponse = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        return pollSearchResults(jsonResponse.get("containerId").getAsString());
    }

    public List<String> pollSearchResults(String agentId) {
        int maxAttempts = 10;
        int attempt = 0;
        System.out.println("4");

        while (attempt < maxAttempts) {
            try {
                Thread.sleep(3000); // Wait 3 seconds
                AgentStatus status = fetchAgentStatus(agentId);

                if ("done".equals(status.getState())) {
                    return parseProfileUrls(status.getDownloadUrls());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Polling interrupted", e);
            }
            attempt++;
        }
        throw new RuntimeException("Search export timeout after " + maxAttempts + " attempts");
    }

    private AgentStatus fetchAgentStatus(String agentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Phantombuster-Key-1", apiKey);
        System.out.println("5");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = BASE_URL + "/agents/fetch?id=" + agentId + "&downloadResults=true";
        System.out.println("6");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("7");

        JsonObject json = JsonParser.parseString(response.getBody()).getAsJsonObject();
        return new AgentStatus(json.get("state").getAsString(),
                json.get("output").getAsString());
    }

    private List<String> parseProfileUrls(String csvDownloadUrl) {
        // Download CSV from S3 URL
        ResponseEntity<byte[]> csvResponse = restTemplate.getForEntity(csvDownloadUrl, byte[].class);
        String csvContent = new String(csvResponse.getBody());

        // Parse with OpenCSV
        try (CSVReader reader = new CSVReader(new StringReader(csvContent))) {
            List<String> profileUrls = new ArrayList<>();
            reader.readNext(); // Skip header

            String[] row;
            int count = 0;
            while ((row = reader.readNext()) != null && count++ < ProfileFinder.MAX_THRESHOLD) {
                profileUrls.add(row[0]); // profileUrl is first column
            }
            return profileUrls;
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("CSV parsing failed", e);
        }
    }

    public String launchProfileScraper(List<String> profileUrls) {
        String profileList = profileUrls.stream()
                .map(url -> Map.of("profileUrl", url))
                .collect(Collectors.toList()).toString()
                .replaceAll("'", "\"");

        String json = """
                {
                    "id": "%s",
                    "bonusArguments": {
                        "profileArray": %s,
                        "sessionCookie": "%s",
                        "asAPI": true
                    }
                }
                """.formatted(PhantomBusterApi.profileScrapperId,profileList, linkedInCookie);

        return executePost("/agents/launch", json);
    }

    private String executePost(String endpoint, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Phantombuster-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_URL + endpoint, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("PhantomBuster API failed: " + response.getBody());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        return jsonResponse.get("agentId").getAsString();
    }

    public List<LinkedInProfile> pollProfileResults(String agentId) {
        int maxAttempts = 30;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                // Profile scraping takes longer
                Thread.sleep(10000);
                AgentStatus status = fetchAgentStatus(agentId);

                if ("done".equals(status.getState())) {
                    return parseProfileData(status.getDownloadUrls());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Polling interrupted", e);
            }
            attempt++;
        }
        throw new RuntimeException("Profile scraper timeout after " + maxAttempts + " attempts");
    }

    private List<LinkedInProfile> parseProfileData(String csvDownloadUrl) {
        ResponseEntity<byte[]> csvResponse = restTemplate.getForEntity(csvDownloadUrl, byte[].class);
        String csvContent = new String(csvResponse.getBody());

        try (CSVReader reader = new CSVReader(new StringReader(csvContent))) {
            List<LinkedInProfile> profiles = new ArrayList<>();
            // Read headers
            String[] headers = reader.readNext();

            String[] row;
            int count = 0;
            while ((row = reader.readNext()) != null && count++ < ProfileFinder.MAX_THRESHOLD) {
                LinkedInProfile profile = new LinkedInProfile();
                profile.setFullName(getValue(row, headers, "fullName"));
                profile.setLinkedInHeadLine(getValue(row, headers, "headline"));
                profile.setProfileUrl(getValue(row, headers, "profileUrl"));
                profile.setUniversity(getValue(row, headers, "linkedinSchoolName"));
                profile.setCurrentRole(getValue(row, headers, "linkedinJobTitle"));
                profile.setLocation(getValue(row, headers, "location"));

                // Extract passout year from "2018 - 2022"
                String dateRange = getValue(row, headers, "linkedinSchoolDateRange");
                profile.setPassoutYear(Integer.parseInt(extractYear(dateRange)));

                profiles.add(profile);
            }
            return profiles;
        } catch (Exception e) {
            throw new RuntimeException("Profile CSV parsing failed", e);
        }
    }

    private String getValue(String[] row, String[] headers, String headerName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i] != null && headers[i].equalsIgnoreCase(headerName)) {
                return row.length > i ? row[i] : "";
            }
        }
        return "";
    }

    private String extractYear(String dateRange) {
        if (dateRange == null || dateRange.isEmpty()) {
            return "";
        }
        String[] parts = dateRange.split(" - ");
        return parts.length > 1 ? parts[1].trim() : dateRange;
    }

    private static class AgentStatus {
        private final String state;
        private final String output;

        public AgentStatus(String state, String output) {
            this.state = state;
            this.output = output;
        }

        public String getState() {
            return state;
        }

        public String getDownloadUrls() {
            return output;
        }
    }

}