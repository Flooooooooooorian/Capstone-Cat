package de.neuefische.backend.services;

import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.model.Course;
import de.neuefische.backend.repos.CourseRepo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private final CourseRepo repo = mock(CourseRepo.class);
    private final CapstoneService capstoneService = mock(CapstoneService.class);
    private final CourseService courseService = new CourseService(repo, capstoneService);

    @Test
    void getAllCourses() {
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
        Capstone capstone3 = Capstone.builder()
                .id("3")
                .qualityBadgeUrl("badge-url-3")
                .coverageBadgeUrl("coverage-url-3")
                .githubApiUrl("githubApi-url-3")
                .build();
        Capstone capstone4 = Capstone.builder()
                .id("4")
                .qualityBadgeUrl("badge-url-4")
                .coverageBadgeUrl("coverage-url-4")
                .githubApiUrl("githubApi-url-4")
                .build();

        Course course1 = Course.builder()
                .id("1")
                .name("java 21-4")
                .capstones(List.of(capstone1, capstone2))
                .build();

        Course course2 = Course.builder()
                .id("2")
                .name("java 21-5")
                .capstones(List.of(capstone3, capstone4))
                .build();

        when(repo.findAll()).thenReturn(List.of(course1, course2));

        //WHEN
        List<Course> actual = courseService.getAllCourses();

        //THEN
        assertThat(actual, Matchers.containsInAnyOrder(course1, course2));
        verify(repo).findAll();
    }

    @Test
    void getCourseById() {
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

        Course course1 = Course.builder()
                .id("1")
                .name("java 21-4")
                .capstones(List.of(capstone1, capstone2))
                .build();

        when(repo.findById(course1.getId())).thenReturn(java.util.Optional.of(course1));

        //WHEN
        Course actual = courseService.getCourseById(course1.getId());

        //THEN
        assertThat(actual, Matchers.is(course1));
        verify(repo).findById(course1.getId());
    }

    @Test
    void getRefreshedCourse() {
        //GIVEN
        LocalDateTime dateTime = LocalDateTime.now();

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

        Course course1 = Course.builder()
                .id("1")
                .name("java 21-4")
                .capstones(List.of(capstone1, capstone2))
                .build();

        Capstone capstoneDto1 = Capstone.builder()
                .openPulls(11)
                .allPulls(12)
                .mainCommits(13)
                .allCommits(14)
                .studentName("name1")
                .updatedAt(dateTime)
                .url("url1")
                .build();

        Capstone capstoneDto2 = Capstone.builder()
                .openPulls(112)
                .allPulls(122)
                .mainCommits(132)
                .allCommits(142)
                .studentName("name2")
                .updatedAt(dateTime)
                .url("url2")
                .build();

        Course expected = Course.builder()
                .id(course1.getId())
                .name(course1.getName())
                .capstones(List.of(capstoneDto1, capstoneDto2))
                .build();

        when(capstoneService.refreshCapstone(capstone1)).thenReturn(capstoneDto1);
        when(capstoneService.refreshCapstone(capstone2)).thenReturn(capstoneDto2);
        when(repo.findById(course1.getId())).thenReturn(java.util.Optional.of(course1));
        when(repo.save(expected)).thenReturn(expected);

        //WHEN
        Course actual = courseService.getRefreshedCourse(course1.getId());

        //THEN

        assertThat(actual, Matchers.is(expected));
        verify(repo).findById(course1.getId());
        verify(capstoneService).refreshCapstone(capstone1);
        verify(capstoneService).refreshCapstone(capstone2);
    }

    @Test
    void getRefreshedCapstone() {
    }
}