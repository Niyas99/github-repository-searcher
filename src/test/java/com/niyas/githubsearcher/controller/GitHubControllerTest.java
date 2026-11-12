
package com.niyas.githubsearcher.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.niyas.githubsearcher.entity.GitHubRepository;
import com.niyas.githubsearcher.service.GitHubService;

@WebMvcTest(GitHubController.class)
class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitHubService gitHubService;

    // Test 1: Successfully search repositories
    @Test
    void searchRepositories_shouldReturnRepositories() throws Exception {

        GitHubRepository repository = createRepository();

        when(gitHubService.searchAndSaveRepositories(
                "spring boot", "Java", "stars", "desc"))
                .thenReturn(List.of(repository));

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "query": "spring boot",
                            "language": "Java",
                            "sort": "stars",
                            "order": "desc"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].githubId").value(12345))
                .andExpect(jsonPath("$[0].name")
                        .value("spring-boot-demo"))
                .andExpect(jsonPath("$[0].language").value("Java"))
                .andExpect(jsonPath("$[0].stars").value(1500));

        verify(gitHubService).searchAndSaveRepositories(
                "spring boot", "Java", "stars", "desc");
    }

    // Test 2: Reject an empty search query
    @Test
    void searchRepositories_shouldReturnBadRequestForEmptyQuery()
            throws Exception {

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "query": "",
                            "language": "Java",
                            "sort": "stars",
                            "order": "desc"
                        }
                        """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(gitHubService);
    }

    // Test 3: Successfully retrieve all repositories
    @Test
    void getRepositories_shouldReturnAllRepositories() throws Exception {

        GitHubRepository repository = createRepository();

        when(gitHubService.getRepositories(
                null, null, "stars", "desc"))
                .thenReturn(List.of(repository));

        mockMvc.perform(get("/api/github/repositories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name")
                        .value("spring-boot-demo"));

        verify(gitHubService).getRepositories(
                null, null, "stars", "desc");
    }

    // Test 4: Retrieve repositories using filters
    @Test
    void getRepositories_shouldApplyFilters() throws Exception {

        GitHubRepository repository = createRepository();

        when(gitHubService.getRepositories(
                "Java", 1000, "stars", "desc"))
                .thenReturn(List.of(repository));

        mockMvc.perform(get("/api/github/repositories")
                .param("language", "Java")
                .param("minStars", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].stars").value(1500));

        verify(gitHubService).getRepositories(
                "Java", 1000, "stars", "desc");
    }

    // Test 5: Retrieve repositories using sorting
    @Test
    void getRepositories_shouldApplySorting() throws Exception {

        GitHubRepository repository = createRepository();

        when(gitHubService.getRepositories(
                null, null, "forks", "desc"))
                .thenReturn(List.of(repository));

        mockMvc.perform(get("/api/github/repositories")
                .param("sortBy", "forks")
                .param("order", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].forks").value(250));

        verify(gitHubService).getRepositories(
                null, null, "forks", "desc");
    }

    // Helper method to create sample repository
    private GitHubRepository createRepository() {

        GitHubRepository repository = new GitHubRepository();

        repository.setGithubId(12345L);
        repository.setName("spring-boot-demo");
        repository.setDescription("Spring Boot sample project");
        repository.setOwner("niyas");
        repository.setLanguage("Java");
        repository.setStars(1500);
        repository.setForks(250);

        return repository;
    }
}
