
package com.niyas.githubsearcher.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.niyas.githubsearcher.dto.GitHubApiResponse;

@Component
public class GitHubApiClient {

    private final RestClient restClient;

    public GitHubApiClient(RestClient githubRestClient) {
        this.restClient = githubRestClient;
    }


	public GitHubApiResponse searchRepositories(
	        String query,
	        String language,
	        String sort,
	        String order) {
	
	    final String searchQuery =
	            (language != null && !language.isBlank())
	                    ? query + " language:" + language
	                    : query;
	
	    return restClient.get()
	            .uri(uriBuilder -> {
	
	                var builder = uriBuilder
	                        .path("/search/repositories")
	                        .queryParam("q", searchQuery)
	                        .queryParam("per_page", 30);
	
	                if (sort != null && !sort.isBlank()) {
	                    builder.queryParam("sort", sort);
	                }
	
	                if (order != null && !order.isBlank()) {
	                    builder.queryParam("order", order);
	                }
	
	                return builder.build();
	            })
	            .retrieve()
	            .body(GitHubApiResponse.class);
	}

}
