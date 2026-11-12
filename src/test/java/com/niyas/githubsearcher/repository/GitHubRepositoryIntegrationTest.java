
package com.niyas.githubsearcher.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.niyas.githubsearcher.entity.GitHubRepository;

@SpringBootTest
@Transactional
class GitHubRepositoryIntegrationTest {

    @Autowired
    private GitHubRepositoryRepository repository;

    // Clear database records before each test
    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    // Test 1: Save and retrieve a repository
    @Test
    void saveRepository_shouldSaveAndRetrieveSuccessfully() {

        GitHubRepository repo = createRepository(
                900001L,
                "spring-boot-demo",
                "Java",
                1500,
                250
        );

        repository.save(repo);

        GitHubRepository saved =
                repository.findById(900001L).orElseThrow();

        assertEquals("spring-boot-demo", saved.getName());
        assertEquals("Java", saved.getLanguage());
        assertEquals(1500, saved.getStars());
        assertEquals(250, saved.getForks());
    }

    // Test 2: Filter repositories by language
    @Test
    void findRepositories_shouldFilterByLanguage() {

        repository.save(createRepository(
                900002L, "java-project", "Java", 1500, 200));

        repository.save(createRepository(
                900003L, "python-project", "Python", 2000, 300));

        List<GitHubRepository> result =
                repository.findRepositoriesByFilters(
                        "java",
                        null,
                        Sort.by(Sort.Direction.DESC, "stars")
                );

        assertEquals(1, result.size());

        assertTrue(result.stream()
                .allMatch(repo ->
                        "Java".equalsIgnoreCase(repo.getLanguage())));
    }

    // Test 3: Filter by minimum stars
    @Test
    void findRepositories_shouldFilterByMinimumStars() {

        repository.save(createRepository(
                900004L, "small-project", "Java", 100, 20));

        repository.save(createRepository(
                900005L, "popular-project", "Java", 5000, 500));

        List<GitHubRepository> result =
                repository.findRepositoriesByFilters(
                        null,
                        1000,
                        Sort.by(Sort.Direction.DESC, "stars")
                );

        assertEquals(1, result.size());
        assertTrue(result.get(0).getStars() >= 1000);
    }

    // Test 4: Verify descending sorting
    @Test
    void findRepositories_shouldSortByStarsDescending() {

        repository.save(createRepository(
                900006L, "project-one", "Java", 100, 10));

        repository.save(createRepository(
                900007L, "project-two", "Java", 500, 50));

        repository.save(createRepository(
                900008L, "project-three", "Java", 1000, 100));

        List<GitHubRepository> result =
                repository.findRepositoriesByFilters(
                        null,
                        null,
                        Sort.by(Sort.Direction.DESC, "stars")
                );

        assertEquals(3, result.size());

        assertEquals(1000, result.get(0).getStars());
        assertEquals(500, result.get(1).getStars());
        assertEquals(100, result.get(2).getStars());
    }

    // Helper method to create repository objects
    private GitHubRepository createRepository(
            Long id,
            String name,
            String language,
            Integer stars,
            Integer forks) {

        GitHubRepository repo = new GitHubRepository();

        repo.setGithubId(id);
        repo.setName(name);
        repo.setDescription("Integration test repository");
        repo.setOwner("niyas");
        repo.setLanguage(language);
        repo.setStars(stars);
        repo.setForks(forks);
        repo.setUpdatedAt(OffsetDateTime.now());

        return repo;
    }
}
