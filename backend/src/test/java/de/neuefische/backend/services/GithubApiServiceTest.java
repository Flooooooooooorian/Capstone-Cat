package de.neuefische.backend.services;

import de.neuefische.backend.dtos.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GithubApiServiceTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final GithubApiService githubApiService = new GithubApiService(restTemplate);

    @Value("${de.neuefische.capstonecat.github.token}")
    private String githubToken;

    @Test
    void getRepoData() {
        //GIVEN
        String repoUrl = "github.com/repo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);

        LocalDate date = LocalDate.now();

        GithubRepoDto repoDto = GithubRepoDto.builder()
                .defaultBranch("main")
                .owner(new GithubRepoOwnerDto("me"))
                .updatedAt(date)
                .url("url")
                .build();

        GithubBranchDto[] branchDtos = new GithubBranchDto[]{new GithubBranchDto("main"), new GithubBranchDto("feature")};

        GithubCommitDto[] commit100Dtos = new GithubCommitDto[100];
        GithubCommitDto[] commit50Dtos = new GithubCommitDto[1];

        GithubPullDto[] pullDtos = new GithubPullDto[]{new GithubPullDto("open"), new GithubPullDto("closed")};

        when(restTemplate.exchange(repoUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubRepoDto.class))
                .thenReturn(ResponseEntity.ok(repoDto));
        when(restTemplate.exchange(repoUrl + "/branches", HttpMethod.GET, new HttpEntity<>(headers), GithubBranchDto[].class))
                .thenReturn(ResponseEntity.ok(branchDtos));
        when(restTemplate.exchange(repoUrl + "/commits?sha=main&per_page=100&page=0", HttpMethod.GET, new HttpEntity<>(headers), GithubCommitDto[].class))
                .thenReturn(ResponseEntity.ok(commit100Dtos));
        when(restTemplate.exchange(repoUrl + "/commits?sha=main&per_page=100&page=1", HttpMethod.GET, new HttpEntity<>(headers), GithubCommitDto[].class)).
                thenReturn(ResponseEntity.ok(commit50Dtos));
        when(restTemplate.exchange(repoUrl + "/compare/main...feature", HttpMethod.GET, new HttpEntity<>(headers), GithubCompareDto.class))
                .thenReturn(ResponseEntity.ok(new GithubCompareDto(5)));
        when(restTemplate.exchange(repoUrl + "/pulls?state=all", HttpMethod.GET, new HttpEntity<>(headers), GithubPullDto[].class))
                .thenReturn(ResponseEntity.ok(pullDtos));

        //WHEN

        CapstoneDto capstoneDto = githubApiService.getRepoData(repoUrl);

        //THEN

        assertThat(capstoneDto.getAllCommits(), Matchers.is(106));
        assertThat(capstoneDto.getMainCommits(), Matchers.is(101));
        assertThat(capstoneDto.getAllPulls(), Matchers.is(2));
        assertThat(capstoneDto.getOpenPulls(), Matchers.is(1));
        assertThat(capstoneDto.getStudentName(), Matchers.is("me"));
        assertThat(capstoneDto.getUpdatedAt(), Matchers.is(date));
        assertThat(capstoneDto.getUrl(), Matchers.is("url"));
    }
}