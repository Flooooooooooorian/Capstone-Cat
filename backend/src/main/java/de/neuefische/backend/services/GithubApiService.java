package de.neuefische.backend.services;

import de.neuefische.backend.dtos.*;
import de.neuefische.backend.model.Capstone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GithubApiService {

    private final RestTemplate restTemplate;

    @Value("${de.neuefische.capstonecat.github.token}")
    private String githubToken;

    private final HttpHeaders headers = new HttpHeaders();


    public GithubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Capstone> getRepoData(String repoUrl) {
        headers.setBearerAuth(githubToken);
        Optional<GithubRepoDto> optionalGithubRepoDto = getRepoDetails(repoUrl);

        if (optionalGithubRepoDto.isEmpty()) {
            return Optional.empty();
        }

        GithubRepoDto repoDto = optionalGithubRepoDto.get();

        List<GithubBranchDto> githubBranchDtos = getBranches(repoUrl);

        int mainCommits = getCommitCountFromDefault(repoUrl, repoDto.getDefaultBranch());

        int commitsAhead = githubBranchDtos.stream()
                .map(githubBranchDto -> getCommitDiffToDefault(repoUrl, repoDto.getDefaultBranch(), githubBranchDto.getName()))
                .mapToInt(Integer::valueOf)
                .sum();

        List<GithubPullDto> allPulls = getPulls(repoUrl);

        int openPulls = Math.toIntExact(allPulls.stream().filter(githubPullDto -> "open".equals(githubPullDto.getState())).count());

        return Optional.of(Capstone.builder()
                .studentName(repoDto.getOwner().getName())
                .allCommits(commitsAhead + mainCommits)
                .mainCommits(mainCommits)
                .allPulls(allPulls.size())
                .openPulls(openPulls)
                .url(repoDto.getUrl())
                .updatedAt(repoDto.getUpdatedAt())
                .build());
    }

    private Optional<GithubRepoDto> getRepoDetails(String repoUrl) {
        try {
            ResponseEntity<GithubRepoDto> repoResponse = restTemplate.exchange(repoUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubRepoDto.class);
            return Optional.ofNullable(repoResponse.getBody());
        }
        catch (RestClientException ex) {
            log.warn("Github Repo not found! " + repoUrl, ex);
            return Optional.empty();
        }
    }

    private List<GithubBranchDto> getBranches(String repoUrl) {
        ResponseEntity<GithubBranchDto[]> branchResponse = restTemplate.exchange(repoUrl + "/branches", HttpMethod.GET, new HttpEntity<>(headers), GithubBranchDto[].class);
        if (branchResponse.getBody() != null) {
            return Arrays.stream(branchResponse.getBody()).toList();
        } else {
            return new ArrayList<>();
        }
    }

    private int getCommitCountFromDefault(String repoUrl, String branch) {
        ArrayList<GithubCommitDto> commits = new ArrayList<>();
        for (int page = 0; commits.size() % 100 == 0; page++) {
            ResponseEntity<GithubCommitDto[]> commitResponse = restTemplate.exchange(repoUrl + "/commits?sha=" + branch + "&per_page=100&page=" + page, HttpMethod.GET, new HttpEntity<>(headers), GithubCommitDto[].class);
            if (commitResponse.getBody() != null) {
                commits.addAll(Arrays.stream(commitResponse.getBody()).toList());
            }
        }
        return commits.size();
    }

    @SuppressWarnings({"java:S2259"})
    private int getCommitDiffToDefault(String repoUrl, String defaultBranch, String branch) {
        try {
            ResponseEntity<GithubCompareDto> compareResponse = restTemplate.exchange(repoUrl + "/compare/" + defaultBranch + "..." + branch, HttpMethod.GET, new HttpEntity<>(headers), GithubCompareDto.class);
            if (compareResponse.getBody() != null) {
                return compareResponse.getBody().getAheadBy();
            }
            return 0;
        }
        catch (Exception e) {
            return 0;
        }
    }

    private List<GithubPullDto> getPulls(String repoUrl) {
        ResponseEntity<GithubPullDto[]> pullsResponse = restTemplate.exchange(repoUrl + "/pulls?state=all", HttpMethod.GET, new HttpEntity<>(headers), GithubPullDto[].class);
        if (pullsResponse.getBody() != null) {
            return Arrays.stream(pullsResponse.getBody()).toList();
        } else {
            return new ArrayList<>();
        }
    }
}
