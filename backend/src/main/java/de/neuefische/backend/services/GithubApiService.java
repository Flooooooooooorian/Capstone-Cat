package de.neuefische.backend.services;

import de.neuefische.backend.dtos.github.*;
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

import java.time.LocalDateTime;
import java.util.*;

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

        List<GithubCompareDto> compareDtos = githubBranchDtos.stream()
                .filter(githubBranchDto -> !githubBranchDto.getName().equals(repoDto.defaultBranch))
                .map(githubBranchDto -> compareBranchWithDefault(repoUrl, repoDto.getDefaultBranch(), githubBranchDto.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        int commitsAhead = compareDtos.stream()
                .map(GithubCompareDto::getAheadBy)
                .mapToInt(Integer::valueOf)
                .sum();


        LocalDateTime mostRecentCommitDate = compareDtos.stream()
                .flatMap(githubCompareDto -> githubCompareDto.getCommits().stream())
                .map(githubCommitDto -> githubCommitDto.getCommit().getCommitter().getDate())
                .max(Comparator.naturalOrder())
                .orElse(null);

        List<GithubPullDto> allPulls = getPulls(repoUrl);

        int openPulls = Math.toIntExact(allPulls.stream().filter(githubPullDto -> "open".equals(githubPullDto.getState())).count());

        Optional<String> workflowBadgeUrl = getWorkflowBadgeUrlIfAny(repoUrl);

        return Optional.of(Capstone.builder()
                .studentName(repoDto.getOwner().getName())
                .allCommits(commitsAhead + mainCommits)
                .mainCommits(mainCommits)
                .allPulls(allPulls.size())
                .openPulls(openPulls)
                .url(repoDto.getUrl())
                .updatedAt(mostRecentCommitDate != null ? mostRecentCommitDate : repoDto.getUpdatedAt())
                .updatedDefaultAt(repoDto.getUpdatedAt())
                .workflowBadgeUrl(workflowBadgeUrl.orElse(""))
                .build());
    }

    private Optional<GithubRepoDto> getRepoDetails(String repoUrl) {
        try {
            ResponseEntity<GithubRepoDto> repoResponse = restTemplate.exchange(repoUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubRepoDto.class);
            return Optional.ofNullable(repoResponse.getBody());
        } catch (RestClientException ex) {
            log.warn("Unable to access GitHub Repo: " + repoUrl, ex);
            return Optional.empty();
        }
    }

    private List<GithubBranchDto> getBranches(String repoUrl) {
        ResponseEntity<GithubBranchDto[]> branchResponse = restTemplate.exchange(repoUrl + "/branches", HttpMethod.GET, new HttpEntity<>(headers), GithubBranchDto[].class);
        if (branchResponse.getBody() != null) {
            return Arrays.stream(branchResponse.getBody()).toList();
        } else {
            log.debug("No Branches found! " + repoUrl);
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

    private Optional<GithubCompareDto> compareBranchWithDefault(String repoUrl, String defaultBranch, String branch) {
        String url = repoUrl + "/compare/" + defaultBranch + "..." + branch;
        try {
            ResponseEntity<GithubCompareDto> compareResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), GithubCompareDto.class);
            GithubCompareDto compareDto = compareResponse.getBody();
            if (compareDto != null) {
                compareDto.setBranchName(branch);
                return Optional.of(compareDto);
            }
            log.debug("No Comparison! " + url);
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Could not compare git branches! (" + url + ")", e);
        }
        return Optional.empty();
    }

    private List<GithubPullDto> getPulls(String repoUrl) {
        ResponseEntity<GithubPullDto[]> pullsResponse = restTemplate.exchange(repoUrl + "/pulls?state=all", HttpMethod.GET, new HttpEntity<>(headers), GithubPullDto[].class);
        if (pullsResponse.getBody() != null) {
            return Arrays.stream(pullsResponse.getBody()).toList();
        } else {
            log.debug("No Pulls! " + repoUrl);
            return new ArrayList<>();
        }
    }

    private Optional<String> getWorkflowBadgeUrlIfAny(String repoUrl) {
        ResponseEntity<GithubWorkflowsDto> workflowsResponse = restTemplate.exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(headers), GithubWorkflowsDto.class);
        GithubWorkflowsDto body = workflowsResponse.getBody();
        if (body != null) {
            List<GithubWorkflowDto> workflows = body.getWorkflows();

            for (GithubWorkflowDto workflow : workflows) {
                if (workflow.getName().toLowerCase().contains("java") || workflow.getName().toLowerCase().contains("maven")) {
                    return Optional.of(workflow.getBadgeUrl());
                }
            }
            if (!workflows.isEmpty()) {
                return Optional.ofNullable(workflows.get(0).getBadgeUrl());
            }
        }

        return Optional.empty();
    }
}
