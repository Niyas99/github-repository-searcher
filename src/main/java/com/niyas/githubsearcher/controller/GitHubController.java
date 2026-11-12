
package com.niyas.githubsearcher.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niyas.githubsearcher.dto.GitHubSearchRequest;
import com.niyas.githubsearcher.entity.GitHubRepository;
import com.niyas.githubsearcher.service.GitHubService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @PostMapping("/search")
    public List<GitHubRepository> searchRepositories(
            @Valid @RequestBody GitHubSearchRequest request) {

        return gitHubService.searchAndSaveRepositories(
                request.getQuery(),
                request.getLanguage(),
                request.getSort(),
                request.getOrder()
        );
    }

@GetMapping("/repositories")
public List<GitHubRepository> getRepositories(

        @RequestParam(required = false)
        String language,

        @RequestParam(required = false)
        Integer minStars,

        @RequestParam(defaultValue = "stars")
        String sortBy,

        @RequestParam(defaultValue = "desc")
        String order) {

    return gitHubService.getRepositories(
            language,
            minStars,
            sortBy,
            order
    );
}

}
