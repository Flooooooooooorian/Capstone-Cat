package de.neuefische.backend.services;

import de.neuefische.backend.dtos.CapstoneDto;
import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.repos.CapstoneRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapstoneService {

    private final CapstoneRepo capstoneRepo;
    private final GithubApiService githubApiService;

    public CapstoneService(CapstoneRepo capstoneRepo, GithubApiService githubApiService) {
        this.capstoneRepo = capstoneRepo;
        this.githubApiService = githubApiService;
        capstoneRepo.save(Capstone.builder().id("1").githubApiUrl("https://api.github.com/repos/Haferkorn/Find-your-Wine").build());
        capstoneRepo.save(Capstone.builder().id("5").githubApiUrl("https://api.github.com/repos/Laifson/sanus-app").coverageBadgeUrl("https://sonarcloud.io/api/project_badges/measure?project=Laifson_sanus-app&metric=coverage").qualityBadgeUrl("https://sonarcloud.io/api/project_badges/measure?project=Laifson_sanus-app&metric=alert_status").build());
        capstoneRepo.save(Capstone.builder().id("2").githubApiUrl("https://api.github.com/repos/Cobas91/eventListener").coverageBadgeUrl("https://sonarcloud.io/api/project_badges/measure?project=Cobas91_eventListener&metric=coverage").qualityBadgeUrl("https://sonarcloud.io/api/project_badges/measure?project=Cobas91_eventListener&metric=alert_status").build());
        capstoneRepo.save(Capstone.builder().id("3").githubApiUrl("https://api.github.com/repos/Jan-OkeMettendorf/simple-sports-club").build());
        capstoneRepo.save(Capstone.builder().id("4").githubApiUrl("https://api.github.com/repos/larsschloegel/property-finder").build());
        capstoneRepo.save(Capstone.builder().id("6").githubApiUrl("https://api.github.com/repos/ankestein/medical-documentation").build());
        capstoneRepo.save(Capstone.builder().id("7").githubApiUrl("https://api.github.com/repos/romsenkabomsen/styled-logic").build());
        capstoneRepo.save(Capstone.builder().id("8").githubApiUrl("https://api.github.com/repos/ThiemoOrrico/tradomator").build());
        capstoneRepo.save(Capstone.builder().id("9").githubApiUrl("https://api.github.com/repos/lember103/capstone").build());
        capstoneRepo.save(Capstone.builder().id("10").githubApiUrl("https://api.github.com/repos/MarcelSchueren/my-depot").build());
        capstoneRepo.save(Capstone.builder().id("11").githubApiUrl("https://api.github.com/repos/nikeskin/application-manager").build());
        capstoneRepo.save(Capstone.builder().id("12").githubApiUrl("https://api.github.com/repos/engelkenf/magic-mirror-fitness").build());
        capstoneRepo.save(Capstone.builder().id("13").githubApiUrl("https://api.github.com/repos/steinerphil/capstone").build());
        capstoneRepo.save(Capstone.builder().id("14").githubApiUrl("https://api.github.com/repos/macben888/capstone").coverageBadgeUrl("https://sonarcloud.io/api/project_badges/measure?project=macben888_capstone&metric=coverage").qualityBadgeUrl("https://sonarcloud.io/api/project_badges/measure?project=macben888_capstone&metric=alert_status").build());
        capstoneRepo.save(Capstone.builder().id("15").githubApiUrl("https://api.github.com/repos/emmzet/event-calendar").build());
    }

    public List<CapstoneDto> getCapstones() {
        return capstoneRepo.findAll().stream()
                .map((capstone -> {
                    CapstoneDto dto = githubApiService.getRepoData(capstone.getGithubApiUrl());
                    dto.setCoverageBadgeUrl(capstone.getCoverageBadgeUrl());
                    dto.setQualityBadgeUrl(capstone.getQualityBadgeUrl());
                    dto.setId(capstone.getId());
                    return dto;
                }))
                .toList();
    }
}
