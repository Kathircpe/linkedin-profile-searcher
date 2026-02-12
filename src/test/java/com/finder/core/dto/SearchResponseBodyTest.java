package com.finder.core.dto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class SearchResponseBodyTest {
    private SearchResponse sampleProfile;

    @BeforeEach
    void setUp() {
        sampleProfile = new SearchResponse();
        sampleProfile.name = "John Doe";
        sampleProfile.university = "MIT";
    }
    @Test
    void constructor_SetsStatusAndData() {
        List<SearchResponse> data = Arrays.asList(sampleProfile);
        SearchResponseBody body = new SearchResponseBody("success", data);

        assertEquals("success", body.status);
        assertEquals(1, body.data.size());
        assertEquals("John Doe", body.data.get(0).name);
    }

    @Test
    void constructor_NullData_ThrowsNoException() {
        SearchResponseBody body = new SearchResponseBody("success", null);
        assertNull(body.data);
        assertEquals("success", body.status);
    }

    @Test
    void constructor_EmptyList() {
        List<SearchResponse> data = new ArrayList<>();
        SearchResponseBody body = new SearchResponseBody("error", data);

        assertEquals("error", body.status);
        assertTrue(body.data.isEmpty());
    }
    @Test
    void fields_ArePubliclyAccessible() {
        List<SearchResponse> data = Arrays.asList(sampleProfile);
        SearchResponseBody body = new SearchResponseBody("success", data);

        // Direct field access (public fields)
        assertEquals("success", body.status);
        assertEquals("John Doe", body.data.get(0).name);
    }

}
