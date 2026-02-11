package com.finder.core.config;

import org.springframework.beans.factory.annotation.Value;

public class PhantomBusterApi {

//    simply using config instead of using environment variables, since it is just an assignment
    private static final String apiKey = "pKeyExAjpUvxIq349yGjkmaP7y6MR7GgvODqODolXEgKLdI";

    private static final String BaseUrl = "https://api.phantombuster.com/api/v2";

    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseUrl() {
        return BaseUrl;
    }


}
