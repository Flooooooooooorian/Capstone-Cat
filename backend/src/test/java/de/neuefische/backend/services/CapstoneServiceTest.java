package de.neuefische.backend.services;

import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.repos.CapstoneRepo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class CapstoneServiceTest {

    private final GithubApiService apiService = mock(GithubApiService.class);
    private final CapstoneRepo capstoneRepo = mock(CapstoneRepo.class);
    private final CapstoneService service = new CapstoneService(apiService, capstoneRepo);

    @Test
    void refreshCapstone() {
        //GIVEN
        Capstone capstone1 = Capstone.builder()
                .id("1")
                .qualityBadgeUrl("badge-url-1")
                .coverageBadgeUrl("coverage-url-1")
                .githubApiUrl("githubApi-url-1")
                .build();

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

        when(apiService.getRepoData("githubApi-url-1")).thenReturn(Optional.of(capstoneDto1));
        when(capstoneRepo.save(expected1)).thenReturn(expected1);

        //WHEN

        Capstone capstoneDto = service.refreshCapstone(capstone1);

        //THEN

        verify(apiService).getRepoData("githubApi-url-1");
        verify(capstoneRepo).save(expected1);
        assertThat(capstoneDto, Matchers.is(expected1));
    }
}
