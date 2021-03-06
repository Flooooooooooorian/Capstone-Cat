package de.neuefische.backend.services;

import de.neuefische.backend.dtos.github.*;
import de.neuefische.backend.model.Capstone;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

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

        LocalDateTime date = LocalDateTime.now();

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


        GithubDetailedCommitDto detailedCommitDto = new GithubDetailedCommitDto(new GithubCommitterDto(date));
        GithubCommitDto commitDto = new GithubCommitDto(detailedCommitDto);

        GithubWorkflowsDto githubWorkflows = GithubWorkflowsDto.builder()
                .cout(2)
                .workflows(List.of(GithubWorkflowDto.builder()
                                .name("deploy")
                                .badgeUrl("wrong workflow")
                                .build(),
                        GithubWorkflowDto.builder()
                                .name("Java")
                                .badgeUrl("workflow-badge-url")
                                .build()))
                .build();

        when(restTemplate.exchange(repoUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubRepoDto.class))
                .thenReturn(ResponseEntity.ok(repoDto));
        when(restTemplate.exchange(repoUrl + "/branches", HttpMethod.GET, new HttpEntity<>(headers), GithubBranchDto[].class))
                .thenReturn(ResponseEntity.ok(branchDtos));
        when(restTemplate.exchange(repoUrl + "/commits?sha=main&per_page=100&page=0", HttpMethod.GET, new HttpEntity<>(headers), GithubCommitDto[].class))
                .thenReturn(ResponseEntity.ok(commit100Dtos));
        when(restTemplate.exchange(repoUrl + "/commits?sha=main&per_page=100&page=1", HttpMethod.GET, new HttpEntity<>(headers), GithubCommitDto[].class)).
                thenReturn(ResponseEntity.ok(commit50Dtos));
        when(restTemplate.exchange(repoUrl + "/compare/main...feature", HttpMethod.GET, new HttpEntity<>(headers), GithubCompareDto.class))
                .thenReturn(ResponseEntity.ok(new GithubCompareDto(5, "feature", List.of(commitDto))));
        when(restTemplate.exchange(repoUrl + "/pulls?state=all", HttpMethod.GET, new HttpEntity<>(headers), GithubPullDto[].class))
                .thenReturn(ResponseEntity.ok(pullDtos));
        when(restTemplate.exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(headers), GithubWorkflowsDto.class))
                .thenReturn(ResponseEntity.ok(githubWorkflows));
        //WHEN

        Optional<Capstone> optionalCapstone = githubApiService.getRepoData(repoUrl);

        //THEN

        assertThat(optionalCapstone.isPresent(), Matchers.is(true));

        Capstone capstoneDto = optionalCapstone.get();

        assertThat(capstoneDto.getAllCommits(), Matchers.is(106));
        assertThat(capstoneDto.getMainCommits(), Matchers.is(101));
        assertThat(capstoneDto.getAllPulls(), Matchers.is(2));
        assertThat(capstoneDto.getOpenPulls(), Matchers.is(1));
        assertThat(capstoneDto.getStudentName(), Matchers.is("me"));
        assertThat(capstoneDto.getUpdatedAt(), Matchers.is(date));
        assertThat(capstoneDto.getUrl(), Matchers.is("url"));
        assertThat(capstoneDto.getWorkflowBadgeUrl(), Matchers.is("workflow-badge-url"));

        verify(restTemplate).exchange(repoUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubRepoDto.class);
        verify(restTemplate).exchange(repoUrl + "/branches", HttpMethod.GET, new HttpEntity<>(headers), GithubBranchDto[].class);
        verify(restTemplate).exchange(repoUrl + "/commits?sha=main&per_page=100&page=0", HttpMethod.GET, new HttpEntity<>(headers), GithubCommitDto[].class);
        verify(restTemplate).exchange(repoUrl + "/commits?sha=main&per_page=100&page=1", HttpMethod.GET, new HttpEntity<>(headers), GithubCommitDto[].class);
        verify(restTemplate).exchange(repoUrl + "/compare/main...feature", HttpMethod.GET, new HttpEntity<>(headers), GithubCompareDto.class);
        verify(restTemplate).exchange(repoUrl + "/pulls?state=all", HttpMethod.GET, new HttpEntity<>(headers), GithubPullDto[].class);
        verify(restTemplate).exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(headers), GithubWorkflowsDto.class);
    }

    @Test
    void getRepoDataInvalidUrld() {
        //GIVEN
        String repoUrl = "invalidgithub.com/repo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);

        when(restTemplate.exchange(repoUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubRepoDto.class))
                .thenThrow(new RestClientException("Not Found"));

        //WHEN

        Optional<Capstone> capstoneDto = githubApiService.getRepoData(repoUrl);

        //THEN

        assertThat(capstoneDto.isEmpty(), Matchers.is(true));
    }


    @Test
    void reflectionCompareWithNullBody() {
        try {
            //GIVEN
            String repoUrl = "repo-url";

            Object githubApiService = GithubApiService.class.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);

            when(restTemplate.exchange(repoUrl + "/compare/main...feature", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubCompareDto.class))
                    .thenReturn(new ResponseEntity<>(HttpStatus.OK));

            //WHEN
            Method method = githubApiService.getClass().getDeclaredMethod("compareBranchWithDefault", String.class, String.class, String.class);
            method.setAccessible(true);
            Object result = method.invoke(githubApiService, repoUrl, "main", "feature");

            //THEN
            assertThat(result, Matchers.is(Optional.empty()));
            verify(restTemplate).exchange(repoUrl + "/compare/main...feature", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubCompareDto.class);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void reflectionCompareWithForbidden() {
        try {
            //GIVEN
            String repoUrl = "repo-url";

            Object githubApiService = GithubApiService.class.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);

            when(restTemplate.exchange(repoUrl + "/compare/main...feature", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubCompareDto.class))
                    .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));

            //WHEN
            Method method = githubApiService.getClass().getDeclaredMethod("compareBranchWithDefault", String.class, String.class, String.class);
            method.setAccessible(true);
            Object result = method.invoke(githubApiService, repoUrl, "main", "feature");

            //THEN
            assertThat(result, Matchers.is(Optional.empty()));
            verify(restTemplate).exchange(repoUrl + "/compare/main...feature", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubCompareDto.class);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void reflectionPullsNoBody() {
        try {
            //GIVEN
            String repoUrl = "repo-url";

            Object githubApiService = GithubApiService.class.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);

            when(restTemplate.exchange(repoUrl + "/pulls?state=all", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubPullDto[].class))
                    .thenReturn(new ResponseEntity<>(HttpStatus.OK));

            //WHEN
            Method method = githubApiService.getClass().getDeclaredMethod("getPulls", String.class);
            method.setAccessible(true);
            Object result = method.invoke(githubApiService, repoUrl);

            //THEN
            assertThat(result, Matchers.is(new ArrayList<>()));
            verify(restTemplate).exchange(repoUrl + "/pulls?state=all", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubPullDto[].class);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void reflectionWorkflowBadgeNoWorkflows() {
        try {
            //GIVEN
            String repoUrl = "repo-url";

            GithubWorkflowsDto githubWorkflows = GithubWorkflowsDto.builder()
                    .cout(0)
                    .workflows(List.of())
                    .build();

            Object githubApiService = GithubApiService.class.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);

            when(restTemplate.exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubWorkflowsDto.class))
                    .thenReturn(ResponseEntity.ok(githubWorkflows));
            //WHEN
            Method method = githubApiService.getClass().getDeclaredMethod("getWorkflowBadgeUrlIfAny", String.class);
            method.setAccessible(true);
            Object result = method.invoke(githubApiService, repoUrl);

            //THEN
            assertThat(result, Matchers.is(Optional.empty()));
            verify(restTemplate).exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubWorkflowsDto.class);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void reflectionWorkflowBadgeNoJavaWorkflowDetected() {
        try {
            //GIVEN
            String repoUrl = "repo-url";

            GithubWorkflowsDto githubWorkflows = GithubWorkflowsDto.builder()
                    .cout(1)
                    .workflows(List.of(GithubWorkflowDto.builder()
                            .name("Deploy workflow")
                            .badgeUrl("first-badge-url")
                            .build()))
                    .build();

            Object githubApiService = GithubApiService.class.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);

            when(restTemplate.exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubWorkflowsDto.class))
                    .thenReturn(ResponseEntity.ok(githubWorkflows));
            //WHEN
            Method method = githubApiService.getClass().getDeclaredMethod("getWorkflowBadgeUrlIfAny", String.class);
            method.setAccessible(true);
            Object result = method.invoke(githubApiService, repoUrl);

            //THEN
            assertThat(result, Matchers.is(Optional.of("first-badge-url")));
            verify(restTemplate).exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubWorkflowsDto.class);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void reflectionWorkflowBadgeMavenWorkflowDetected() {
        try {
            //GIVEN
            String repoUrl = "repo-url";

            GithubWorkflowsDto githubWorkflows = GithubWorkflowsDto.builder()
                    .cout(1)
                    .workflows(List.of(
                            GithubWorkflowDto.builder()
                                    .name("workflow deploy build")
                                    .badgeUrl("wrong-badge-url")
                                    .build(),
                            GithubWorkflowDto.builder()
                                    .name("workflow maven build")
                                    .badgeUrl("java-badge-url")
                                    .build()
                    ))
                    .build();

            Object githubApiService = GithubApiService.class.getDeclaredConstructor(RestTemplate.class).newInstance(restTemplate);

            when(restTemplate.exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubWorkflowsDto.class))
                    .thenReturn(ResponseEntity.ok(githubWorkflows));
            //WHEN
            Method method = githubApiService.getClass().getDeclaredMethod("getWorkflowBadgeUrlIfAny", String.class);
            method.setAccessible(true);
            Object result = method.invoke(githubApiService, repoUrl);

            //THEN
            assertThat(result, Matchers.is(Optional.of("java-badge-url")));
            verify(restTemplate).exchange(repoUrl + "/actions/workflows", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), GithubWorkflowsDto.class);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }
}
