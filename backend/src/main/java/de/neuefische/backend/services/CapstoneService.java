package de.neuefische.backend.services;

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

    public List<Capstone> getCapstones() {
        return capstoneRepo.findAll().stream()
                .map((capstone -> {
                    Capstone capstoneDto = githubApiService.getRepoData(capstone.getGithubApiUrl())
                            .orElseGet(Capstone::new);

                    capstoneDto.setCoverageBadgeUrl(capstone.getCoverageBadgeUrl());
                    capstoneDto.setQualityBadgeUrl(capstone.getQualityBadgeUrl());
                    capstoneDto.setId(capstone.getId());
                    return capstoneDto;
                }))
                .toList();
    }
}
