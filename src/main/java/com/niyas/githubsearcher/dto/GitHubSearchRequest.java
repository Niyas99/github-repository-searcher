
package com.niyas.githubsearcher.dto;

import jakarta.validation.constraints.NotBlank;

public class GitHubSearchRequest {

    @NotBlank(message = "Search query cannot be empty")
    private String query;

    private String language;

    private String sort;

    private String order;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
