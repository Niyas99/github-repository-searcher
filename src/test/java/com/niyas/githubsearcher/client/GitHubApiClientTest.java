
package com.niyas.githubsearcher.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.niyas.githubsearcher.dto.GitHubApiResponse;

class GitHubApiClientTest {

    private GitHubApiClient gitHubApiClient;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {

        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://api.github.com");

        mockServer = MockRestServiceServer
                .bindTo(builder)
                .build();

        gitHubApiClient = new GitHubApiClient(builder.build());
    }

    // Test 1: Successfully fetch repositories
    @Test
    void searchRepositories_shouldReturnRepositories() {

        mockServer.expect(requestTo(
                        org.hamcrest.Matchers.containsString(
                                "/search/repositories")))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andExpect(queryParam("q",
                        "spring%20boot%20language:Java"))
                .andExpect(queryParam("per_page", "30"))
                .andExpect(queryParam("sort", "stars"))
                .andExpect(queryParam("order", "desc"))
                .andRespond(withSuccess("""
                        {
                          "total_count": 1,
                          "items": [
                            {
                              "id": 12345,
                              "name": "spring-boot-demo",
                              "full_name": "niyas/spring-boot-demo",
                              "description": "Spring Boot sample project",
                              "owner": {
                                "login": "niyas"
                              },
                              "language": "Java",
                              "stargazers_count": 1500,
                              "forks_count": 250,
                              "updated_at": "2026-11-12T10:00:00Z"
                            }
                          ]
                        }
                        """,
                        org.springframework.http.MediaType.APPLICATION_JSON));

        GitHubApiResponse response =
                gitHubApiClient.searchRepositories(
                        "spring boot",
                        "Java",
                        "stars",
                        "desc");

        assertNotNull(response);
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());

        assertEquals(12345L,
                response.getItems().get(0).getId());

        assertEquals("spring-boot-demo",
                response.getItems().get(0).getName());

        assertEquals("niyas",
                response.getItems().get(0).getOwner().getLogin());

        assertEquals("Java",
                response.getItems().get(0).getLanguage());

        assertEquals(1500,
                response.getItems().get(0).getStars());

        mockServer.verify();
    }

    // Test 2: Search without optional parameters
    @Test
    void searchRepositories_shouldWorkWithoutOptionalParameters() {

        mockServer.expect(requestTo(
                        org.hamcrest.Matchers.containsString(
                                "/search/repositories")))
                .andExpect(queryParam("q", "spring%20boot"))
                .andExpect(queryParam("per_page", "30"))
                .andRespond(withSuccess("""
                        {
                          "total_count": 0,
                          "items": []
                        }
                        """,
                        org.springframework.http.MediaType.APPLICATION_JSON));

        GitHubApiResponse response =
                gitHubApiClient.searchRepositories(
                        "spring boot",
                        null,
                        null,
                        null);

        assertNotNull(response);
        assertNotNull(response.getItems());
        assertTrue(response.getItems().isEmpty());

        mockServer.verify();
    }

    // Test 3: Handle GitHub API rate-limit response
    @Test
    void searchRepositories_shouldThrowExceptionWhenRateLimited() {

        mockServer.expect(requestTo(
                        org.hamcrest.Matchers.containsString(
                                "/search/repositories")))
                .andRespond(withStatus(
                        org.springframework.http.HttpStatus.FORBIDDEN)
                        .body("""
                                {
                                  "message": "API rate limit exceeded"
                                }
                                """)
                        .contentType(
                                org.springframework.http.MediaType.APPLICATION_JSON));

        assertThrows(
                org.springframework.web.client.HttpClientErrorException.Forbidden.class,
                () -> gitHubApiClient.searchRepositories(
                        "spring boot",
                        null,
                        null,
                        null)
        );

        mockServer.verify();
    }
}
