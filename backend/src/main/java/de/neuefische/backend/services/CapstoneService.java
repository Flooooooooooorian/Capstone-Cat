package de.neuefische.backend.services;

import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.repos.CapstoneRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CapstoneService {

    private final CapstoneRepo capstoneRepo;
    private final GithubApiService githubApiService;

    public CapstoneService(CapstoneRepo capstoneRepo, GithubApiService githubApiService) {
        this.capstoneRepo = capstoneRepo;
        this.githubApiService = githubApiService;
    }

    public List<Capstone> getCapstones() {
        return capstoneRepo.findAll();
    }

    public Capstone refreshCapstone(String id) {
        Optional<Capstone> optionalCapstone = capstoneRepo.findById(id);
        Capstone capstone = optionalCapstone.orElseThrow(() -> new NoSuchElementException("Capstone with id: " + id + " not found!"));

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
        return capstoneRepo.save(capstone);
    }
}
