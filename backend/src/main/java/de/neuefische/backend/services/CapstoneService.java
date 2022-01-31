package de.neuefische.backend.services;

import de.neuefische.backend.dtos.CapstoneCreationDto;
import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.repos.CapstoneRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CapstoneService {

    private final GithubApiService githubApiService;
    private final CapstoneRepo capstoneRepo;

    public CapstoneService(GithubApiService githubApiService, CapstoneRepo capstoneRepo) {
        this.githubApiService = githubApiService;
        this.capstoneRepo = capstoneRepo;
    }

    public Capstone addCapstone(CapstoneCreationDto capstoneCreationDto) {
        return capstoneRepo.save(Capstone.builder()
                .githubApiUrl(capstoneCreationDto.getGithubApiUrl())
                .studentName(capstoneCreationDto.getName())
                .build());
    }

    public Capstone refreshCapstone(Capstone capstone) {
        Capstone refreshedCapstone = githubApiService.getRepoData(capstone.getGithubApiUrl())
                .orElseGet(Capstone::new);

        if (capstone.getStudentName() == null) {
            capstone.setStudentName(refreshedCapstone.getStudentName());
        }
        if (capstone.getUrl() == null) {
            capstone.setUrl(refreshedCapstone.getUrl());
        }

        capstone.setAllCommits(refreshedCapstone.getAllCommits());
        capstone.setMainCommits(refreshedCapstone.getMainCommits());
        capstone.setAllPulls(refreshedCapstone.getAllPulls());
        capstone.setOpenPulls(refreshedCapstone.getOpenPulls());
        capstone.setUpdatedAt(refreshedCapstone.getUpdatedAt());
        capstone.setUpdatedDefaultAt(refreshedCapstone.getUpdatedDefaultAt());
        return capstoneRepo.save(capstone);
    }

    public Optional<Capstone> getCapstoneById(String capstoneId) {
        return capstoneRepo.findById(capstoneId);
    }
}
