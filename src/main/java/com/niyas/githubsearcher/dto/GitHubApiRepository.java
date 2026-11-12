
package com.niyas.githubsearcher.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitHubApiRepository {

    private Long id;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    private Owner owner;

    private String language;

    @JsonProperty("stargazers_count")
    private Integer stars;

    @JsonProperty("forks_count")
    private Integer forks;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getStars() {
        return stars;
    }

    public Integer getForks() {
        return forks;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static class Owner {

        private String login;

        public String getLogin() {
            return login;
        }
    }
}
