package de.neuefische.backend.services;

import de.neuefische.backend.model.Capstone;
import org.springframework.stereotype.Service;

@Service
public class CapstoneService {

    private final GithubApiService githubApiService;

    public CapstoneService(GithubApiService githubApiService) {
        this.githubApiService = githubApiService;
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
        return capstone;
    }
}
