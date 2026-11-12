
package com.niyas.githubsearcher.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niyas.githubsearcher.client.GitHubApiClient;
import com.niyas.githubsearcher.dto.GitHubApiRepository;
import com.niyas.githubsearcher.dto.GitHubApiResponse;
import com.niyas.githubsearcher.entity.GitHubRepository;
import com.niyas.githubsearcher.repository.GitHubRepositoryRepository;

@Service
public class GitHubService {

    private final GitHubApiClient gitHubApiClient;
    private final GitHubRepositoryRepository repository;

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("stars", "forks", "updatedAt");

    public GitHubService(
            GitHubApiClient gitHubApiClient,
            GitHubRepositoryRepository repository) {
        this.gitHubApiClient = gitHubApiClient;
        this.repository = repository;
    }

    // Search GitHub and save repositories in PostgreSQL
    @Transactional
    public List<GitHubRepository> searchAndSaveRepositories(
            String query,
            String language,
            String sort,
            String order) {

        GitHubApiResponse response =
                gitHubApiClient.searchRepositories(
                        query, language, sort, order);

        // Check response before accessing its items
        if (response == null || response.getItems() == null) {
            throw new IllegalStateException(
                    "Invalid response received from GitHub API");
        }

        // Reject null repository items
        if (response.getItems().stream().anyMatch(Objects::isNull)) {
            throw new IllegalStateException(
                    "GitHub API returned an invalid repository item");
        }

        List<GitHubRepository> repositories =
                response.getItems()
                        .stream()
                        .map(this::mapToEntity)
                        .toList();

        return repository.saveAll(repositories);
    }

    // Convert GitHub API response into database entity
    private GitHubRepository mapToEntity(
            GitHubApiRepository apiRepository) {

        GitHubRepository entity = new GitHubRepository();

        entity.setGithubId(apiRepository.getId());
        entity.setName(apiRepository.getName());
        entity.setDescription(apiRepository.getDescription());

        if (apiRepository.getOwner() != null) {
            entity.setOwner(apiRepository.getOwner().getLogin());
        }

        entity.setLanguage(apiRepository.getLanguage());
        entity.setStars(apiRepository.getStars());
        entity.setForks(apiRepository.getForks());
        entity.setUpdatedAt(apiRepository.getUpdatedAt());

        return entity;
    }

    // Retrieve repositories with filters and sorting
    @Transactional(readOnly = true)
    public List<GitHubRepository> getRepositories(
            String language,
            Integer minStars,
            String sortBy,
            String order) {

        if (minStars != null && minStars < 0) {
            throw new IllegalArgumentException(
                    "Minimum stars cannot be negative");
        }

        // Normalize language
        String normalizedLanguage =
                (language == null || language.isBlank())
                        ? null
                        : language.trim().toLowerCase();

        // Validate sorting field
        String sortField =
                (sortBy == null || sortBy.isBlank())
                        ? "stars"
                        : sortBy.trim();

        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed: stars, forks, updatedAt");
        }

        // Validate sorting direction
        String direction =
                (order == null || order.isBlank())
                        ? "desc"
                        : order.trim();

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Order must be either asc or desc");
        }

        return repository.findRepositoriesByFilters(
                normalizedLanguage,
                minStars,
                Sort.by(sortDirection, sortField));
    }
}
