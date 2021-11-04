package de.neuefische.backend.services;

import de.neuefische.backend.dtos.CapstoneDto;
import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.repos.CapstoneRepo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CapstoneServiceTest {

    private final CapstoneRepo repo = mock(CapstoneRepo.class);
    private final GithubApiService apiService = mock(GithubApiService.class);
    private final CapstoneService service = new CapstoneService(repo, apiService);

    @Test
    void getCapstones() {
        //GIVEN
        Capstone capstone1 = Capstone.builder()
                .id("1")
                .qualityBadgeUrl("badge-url-1")
                .coverageBadgeUrl("coverage-url-1")
                .githubApiUrl("githubApi-url-1")
                .build();

        Capstone capstone2 = Capstone.builder()
                .id("2")
                .qualityBadgeUrl("badge-url-2")
                .coverageBadgeUrl("coverage-url-2")
                .githubApiUrl("githubApi-url-2")
                .build();

        List<Capstone> capstones = List.of(capstone1, capstone2);
        when(repo.findAll()).thenReturn(capstones);
        CapstoneDto capstoneDto1 = CapstoneDto.builder()
                .openPulls(11)
                .allPulls(12)
                .mainCommits(13)
                .allCommits(14)
                .studentName("name1")
                .updatedAt(LocalDate.now())
                .url("url1")
                .build();

        CapstoneDto capstoneDto2 = CapstoneDto.builder()
                .openPulls(21)
                .allPulls(22)
                .mainCommits(23)
                .allCommits(24)
                .studentName("name1")
                .updatedAt(LocalDate.now())
                .url("url2")
                .build();

        when(apiService.getRepoData("githubApi-url-1")).thenReturn(Optional.of(capstoneDto1));
        when(apiService.getRepoData("githubApi-url-2")).thenReturn(Optional.of(capstoneDto2));
        //WHEN

        List<CapstoneDto> capstoneDtos = service.getCapstones();

        //THEN
        CapstoneDto expected1 = CapstoneDto.builder()
                .openPulls(11)
                .allPulls(12)
                .mainCommits(13)
                .allCommits(14)
                .studentName("name1")
                .updatedAt(LocalDate.now())
                .url("url1")
                .id("1")
                .qualityBadgeUrl("badge-url-1")
                .coverageBadgeUrl("coverage-url-1")
                .build();

        CapstoneDto expected2 = CapstoneDto.builder()
                .openPulls(21)
                .allPulls(22)
                .mainCommits(23)
                .allCommits(24)
                .studentName("name1")
                .updatedAt(LocalDate.now())
                .url("url2")
                .id("2")
                .coverageBadgeUrl("coverage-url-2")
                .qualityBadgeUrl("badge-url-2")
                .build();

        assertThat(capstoneDtos, Matchers.contains(expected1, expected2));
    }
}