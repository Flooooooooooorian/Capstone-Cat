package de.neuefische.backend.services;

import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.repos.CapstoneRepo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

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

        //WHEN

        List<Capstone> capstoneDtos = service.getCapstones();

        //THEN
        Capstone expected1 = Capstone.builder()
                .id("1")
                .qualityBadgeUrl("badge-url-1")
                .coverageBadgeUrl("coverage-url-1")
                .githubApiUrl("githubApi-url-1")
                .build();

        Capstone expected2 = Capstone.builder()
                .id("2")
                .coverageBadgeUrl("coverage-url-2")
                .qualityBadgeUrl("badge-url-2")
                .githubApiUrl("githubApi-url-2")
                .build();

        verify(repo).findAll();
        assertThat(capstoneDtos, Matchers.contains(expected1, expected2));
    }

    @Test
    void refreshCapstone() {
        //GIVEN
        Capstone capstone1 = Capstone.builder()
                .id("1")
                .qualityBadgeUrl("badge-url-1")
                .coverageBadgeUrl("coverage-url-1")
                .githubApiUrl("githubApi-url-1")
                .build();

        when(repo.findById("1")).thenReturn(Optional.of(capstone1));
        when(repo.save(capstone1)).thenReturn(capstone1);

        LocalDateTime date = LocalDateTime.now();

                Capstone capstoneDto1 = Capstone.builder()
                        .openPulls(11)
                        .allPulls(12)
                        .mainCommits(13)
                        .allCommits(14)
                        .studentName("name1")
                        .updatedAt(date)
                        .url("url1")
                        .build();

        when(apiService.getRepoData("githubApi-url-1")).thenReturn(Optional.of(capstoneDto1));

        //WHEN

        Capstone capstoneDto = service.refreshCapstone("1");

        //THEN
        Capstone expected1 = Capstone.builder()
                .id("1")
                .qualityBadgeUrl("badge-url-1")
                .coverageBadgeUrl("coverage-url-1")
                .githubApiUrl("githubApi-url-1")
                .openPulls(11)
                .allPulls(12)
                .mainCommits(13)
                .allCommits(14)
                .studentName("name1")
                .updatedAt(date)
                .url("url1")
                .build();

        verify(repo).findById("1");
        verify(repo).save(capstone1);
        verify(apiService).getRepoData("githubApi-url-1");
        assertThat(capstoneDto, Matchers.is(expected1));
    }
}