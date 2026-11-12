
package com.niyas.githubsearcher.dto;

import java.util.List;

public class GitHubApiResponse {

    private List<GitHubApiRepository> items;

    public List<GitHubApiRepository> getItems() {
        return items;
    }

    public void setItems(List<GitHubApiRepository> items) {
        this.items = items;
    }
}
