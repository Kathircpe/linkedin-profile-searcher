package com.finder.core.config;

import org.springframework.beans.factory.annotation.Value;

public class PhantomBusterApi {

//    simply using config instead of using environment variables, since it is just an assignment
    private static final String apiKey = "6rk4TeYgg9K8IIvCtloMSc7LDodwXLMASjMDkUAvqbs";

    private static final String BaseUrl = "https://api.phantombuster.com/api/v2";
    public static final String profileScrapperId="4853495042738676";
    public static final String profileUrlSearchId="8438216981395250";
    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseUrl() {
        return BaseUrl;
    }


}
