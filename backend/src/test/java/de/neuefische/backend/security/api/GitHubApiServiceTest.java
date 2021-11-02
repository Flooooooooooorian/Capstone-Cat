package de.neuefische.backend.security.api;

import de.neuefische.backend.security.api.GitHubApiService;
import de.neuefische.backend.security.model.GitHubAccessTokenDto;
import de.neuefische.backend.security.model.GitHubOAuthCredentialsDto;
import de.neuefische.backend.security.model.GitHubUserDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class GitHubApiServiceTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private GitHubApiService gitHubApiService = new GitHubApiService(restTemplate);

    @Value("${de.neuefische.todo.github.clientid}")
    private String clientId;

    @Value("${de.neuefische.todo.github.clientsecret}")
    private String clientSecret;

    @Test
    void retrieveGitHubToken() {
        //GIVEN
        String code = "someCode";

        GitHubOAuthCredentialsDto credentialsDto = GitHubOAuthCredentialsDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        GitHubAccessTokenDto responseDto = new GitHubAccessTokenDto("someAccessToken");

        when(restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, httpHeaders),
                GitHubAccessTokenDto.class)
        ).thenReturn(ResponseEntity.ok(responseDto));

        //WHEN
        String token = gitHubApiService.retrieveGitHubToken(code);

        //THEN
        assertThat(token, Matchers.is("someAccessToken"));

        verify(restTemplate).exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, httpHeaders),
                GitHubAccessTokenDto.class);

    }

    @Test
    void retrieveUserInfo() {

        //GIVEN
        String token = "someAccessToken";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        when(restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                GitHubUserDto.class)).thenReturn(ResponseEntity.ok(new GitHubUserDto("someLogin")));

        //WHEN
        GitHubUserDto gitHubUserDto = gitHubApiService.retrieveUserInfo(token);

        //THEN
        assertThat(gitHubUserDto.getLogin(), Matchers.is("someLogin"));

        verify(restTemplate).exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                GitHubUserDto.class);
    }
}