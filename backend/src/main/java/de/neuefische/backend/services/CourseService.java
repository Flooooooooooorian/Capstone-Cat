package de.neuefische.backend.services;

import de.neuefische.backend.dtos.CapstoneCreationDto;
import de.neuefische.backend.dtos.CourseCreationDto;
import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.model.Course;
import de.neuefische.backend.repos.CourseRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseService {

    private final CourseRepo repo;
    private final CapstoneService capstoneService;

    public CourseService(CourseRepo repo, CapstoneService capstoneService) {
        this.repo = repo;
        this.capstoneService = capstoneService;
    }

    public Course createCourse(CourseCreationDto courseDto) {
        List<Capstone> capstones = new ArrayList<>();
        for (CapstoneCreationDto capstone : courseDto.getCapstones()) {
            capstones.add(Capstone.builder()
                            .id(UUID.randomUUID().toString())
                            .githubApiUrl(capstone.getGithubApiUrl())
                            .studentName(capstone.getName())
                    .build());
        }

        return repo.save(Course.builder()
                .name(courseDto.getName())
                .capstones(capstones)
                .build());
    }

    public List<Course> getAllCourses() {
        return repo.findAll();
    }

    public Course getCourseById(String id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Course with id: " + id + " does not exists!"));
    }

    public Course getRefreshedCourse(String id) {
        Course course = getCourseById(id);

        course.setCapstones(course.getCapstones().stream()
                .map(capstoneService::refreshCapstone)
                .toList());
        return repo.save(course);
    }

    public Course getRefreshedCapstone(String courseId, String capstoneId) {
        Course course = getCourseById(courseId);

        course.setCapstones(course.getCapstones().stream()
                .map(capstone -> {
                    if (capstone.getId().equals(capstoneId)) {
                        return capstoneService.refreshCapstone(capstone);
                    }
                    return capstone;
                })
                .toList());

        return repo.save(course);
    }
}
