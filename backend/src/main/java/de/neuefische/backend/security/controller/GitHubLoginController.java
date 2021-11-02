package de.neuefische.backend.security.controller;

import de.neuefische.backend.security.model.GitHubLoginDto;
import de.neuefische.backend.security.service.GitHubLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/github/login")
public class GitHubLoginController {

    private final GitHubLoginService gitHubLoginService;

    public GitHubLoginController(GitHubLoginService gitHubLoginService) {
        this.gitHubLoginService = gitHubLoginService;
    }

    @PostMapping
    public String login(@RequestBody GitHubLoginDto gitLoginDto){
        return gitHubLoginService.verifyGitHubCode(gitLoginDto.getCode());
    }
}
