package de.neuefische.backend.security.controller;

import de.neuefische.backend.security.model.GitHubAccessTokenDto;
import de.neuefische.backend.security.model.GitHubLoginDto;
import de.neuefische.backend.security.model.GitHubOAuthCredentialsDto;
import de.neuefische.backend.security.model.GitHubUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubLoginControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${de.neuefische.todo.github.clientid}")
    private String clientId;

    @Value("${de.neuefische.todo.github.clientsecret}")
    private String clientSecret;

    @Value("${neuefische.todo.jwt.secret}")
    private String JWT_SECRET;

    @Test
    void login() {
        //GIVEN
        GitHubLoginDto gitHubLoginDto = new GitHubLoginDto("someCode");

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setBearerAuth("someAccessToken");

        when(restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(loginHeaders),
                GitHubUserDto.class)).thenReturn(ResponseEntity.ok(new GitHubUserDto("someLogin")));

        GitHubOAuthCredentialsDto credentialsDto = GitHubOAuthCredentialsDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(gitHubLoginDto.getCode())
                .build();
        HttpHeaders accessTokenHeader = new HttpHeaders();
        accessTokenHeader.setAccept(List.of(MediaType.APPLICATION_JSON));

        GitHubAccessTokenDto responseDto = new GitHubAccessTokenDto("someAccessToken");

        when(restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, accessTokenHeader),
                GitHubAccessTokenDto.class)
        ).thenReturn(ResponseEntity.ok(responseDto));

        //WHEN
        ResponseEntity<String> response = testRestTemplate.postForEntity("/auth/github/login", gitHubLoginDto, String.class);

        //THEN
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));

        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(response.getBody()).getBody();
        assertThat(claims.getSubject(), Matchers.is("someLogin"));

        verify(restTemplate).exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, accessTokenHeader),
                GitHubAccessTokenDto.class);
        verify(restTemplate).exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(loginHeaders),
                GitHubUserDto.class);
    }
}