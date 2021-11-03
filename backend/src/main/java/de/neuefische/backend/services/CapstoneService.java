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
