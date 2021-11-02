package de.neuefische.backend.security.service;

import de.neuefische.backend.security.api.GitHubApiService;
import de.neuefische.backend.security.model.GitHubUserDto;
import de.neuefische.backend.security.service.GitHubLoginService;
import de.neuefische.backend.security.service.JWTUtilService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class GitHubLoginServiceTest {

    private final GitHubApiService gitHubApiService = mock(GitHubApiService.class);
    private final JWTUtilService jwtUtilService = mock(JWTUtilService.class);

    private final GitHubLoginService gitHubLoginService = new GitHubLoginService(gitHubApiService, jwtUtilService);

    @Test
    void verifyGitHubCode() {
        //GIVEN
        String code = "someCode";

        when(gitHubApiService.retrieveGitHubToken(code)).thenReturn("someAccessToken");
        when(gitHubApiService.retrieveUserInfo("someAccessToken")).thenReturn(new GitHubUserDto("someLogin"));
        when(jwtUtilService.createToken(new HashMap<>(), "someLogin")).thenReturn("someJwt");

        //WHEN
        String jwt = gitHubLoginService.verifyGitHubCode(code);

        //THEN
        assertThat(jwt, Matchers.is("someJwt"));

        verify(gitHubApiService).retrieveGitHubToken(code);
        verify(gitHubApiService).retrieveUserInfo("someAccessToken");
        verify(jwtUtilService).createToken(new HashMap<>(), "someLogin");
    }
}