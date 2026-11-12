
package com.niyas.githubsearcher.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.niyas.githubsearcher.client.GitHubApiClient;
import com.niyas.githubsearcher.dto.GitHubApiRepository;
import com.niyas.githubsearcher.dto.GitHubApiResponse;
import com.niyas.githubsearcher.entity.GitHubRepository;
import com.niyas.githubsearcher.repository.GitHubRepositoryRepository;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private GitHubApiClient gitHubApiClient;

    @Mock
    private GitHubRepositoryRepository repository;

    @InjectMocks
    private GitHubService gitHubService;

    // Test 1: Successfully search and save repositories
    @Test
    void searchAndSaveRepositories_shouldSaveRepositories() {

        GitHubApiRepository apiRepository =
                mock(GitHubApiRepository.class);

        GitHubApiRepository.Owner owner =
                mock(GitHubApiRepository.Owner.class);

        when(apiRepository.getId()).thenReturn(12345L);
        when(apiRepository.getName()).thenReturn("spring-boot-demo");
        when(apiRepository.getDescription())
                .thenReturn("Spring Boot sample project");
        when(apiRepository.getOwner()).thenReturn(owner);
        when(owner.getLogin()).thenReturn("niyas");
        when(apiRepository.getLanguage()).thenReturn("Java");
        when(apiRepository.getStars()).thenReturn(1500);
        when(apiRepository.getForks()).thenReturn(250);
        when(apiRepository.getUpdatedAt())
                .thenReturn(OffsetDateTime.parse(
                        "2026-11-12T10:00:00Z"));

        GitHubApiResponse apiResponse = new GitHubApiResponse();
        apiResponse.setItems(List.of(apiRepository));

        when(gitHubApiClient.searchRepositories(
                "spring boot", "Java", "stars", "desc"))
                .thenReturn(apiResponse);

        when(repository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<GitHubRepository> result =
                gitHubService.searchAndSaveRepositories(
                        "spring boot", "Java", "stars", "desc");

        assertEquals(1, result.size());

        GitHubRepository savedRepository = result.get(0);

        assertEquals(12345L, savedRepository.getGithubId());
        assertEquals("spring-boot-demo", savedRepository.getName());
        assertEquals("Spring Boot sample project",
                savedRepository.getDescription());
        assertEquals("niyas", savedRepository.getOwner());
        assertEquals("Java", savedRepository.getLanguage());
        assertEquals(1500, savedRepository.getStars());
        assertEquals(250, savedRepository.getForks());

        verify(gitHubApiClient).searchRepositories(
                "spring boot", "Java", "stars", "desc");

        verify(repository).saveAll(anyList());
    }

    // Test 2: Handle null GitHub API response
    @Test
    void searchAndSaveRepositories_shouldThrowExceptionWhenResponseIsNull() {

        when(gitHubApiClient.searchRepositories(
                "spring boot", "Java", "stars", "desc"))
                .thenReturn(null);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> gitHubService.searchAndSaveRepositories(
                        "spring boot", "Java", "stars", "desc")
        );

        assertEquals(
                "Invalid response received from GitHub API",
                exception.getMessage());

        verify(repository, never()).saveAll(anyList());
    }

    // Test 3: Handle null items in GitHub API response
    @Test
    void searchAndSaveRepositories_shouldThrowExceptionWhenItemsAreNull() {

        GitHubApiResponse emptyResponse = new GitHubApiResponse();
        emptyResponse.setItems(null);

        when(gitHubApiClient.searchRepositories(
                "spring boot", "Java", "stars", "desc"))
                .thenReturn(emptyResponse);

        assertThrows(
                IllegalStateException.class,
                () -> gitHubService.searchAndSaveRepositories(
                        "spring boot", "Java", "stars", "desc")
        );

        verify(repository, never()).saveAll(anyList());
    }

    // Test 4: Retrieve repositories with filters and sorting
    @Test
    void getRepositories_shouldApplyFiltersAndSorting() {

        List<GitHubRepository> expected =
                List.of(new GitHubRepository());

        Sort expectedSort =
                Sort.by(Sort.Direction.DESC, "stars");

        // IMPORTANT: The service passes lowercase "java"
        when(repository.findRepositoriesByFilters(
                eq("java"),
                eq(1000),
                eq(expectedSort)))
                .thenReturn(expected);

        List<GitHubRepository> result =
                gitHubService.getRepositories(
                        "Java", 1000, "stars", "desc");

        assertEquals(expected, result);

        verify(repository).findRepositoriesByFilters(
                eq("java"),
                eq(1000),
                eq(expectedSort));
    }

    // Test 5: Reject negative minimum stars
    @Test
    void getRepositories_shouldThrowExceptionForNegativeStars() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gitHubService.getRepositories(
                        null, -100, "stars", "desc")
        );

        assertEquals(
                "Minimum stars cannot be negative",
                exception.getMessage());

        verifyNoInteractions(repository);
    }

    // Test 6: Reject invalid sorting field
    @Test
    void getRepositories_shouldThrowExceptionForInvalidSortField() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gitHubService.getRepositories(
                        null, null, "invalid", "desc")
        );

        assertTrue(exception.getMessage().contains(
                "Invalid sort field"));

        verifyNoInteractions(repository);
    }

    // Test 7: Reject invalid sorting direction
    @Test
    void getRepositories_shouldThrowExceptionForInvalidOrder() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gitHubService.getRepositories(
                        null, null, "stars", "wrong")
        );

        assertEquals(
                "Order must be either asc or desc",
                exception.getMessage());

        verifyNoInteractions(repository);
    }
}
