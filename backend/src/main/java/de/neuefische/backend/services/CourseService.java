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
        for (CapstoneCreationDto capstoneDto : courseDto.getCapstones()) {
            capstones.add(capstoneService.addCapstone(capstoneDto));
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

    public Course refreshCourse(String id) {
        Course course = getCourseById(id);

        List<Capstone> refreshedCapstone = new ArrayList<>();

        for (Capstone capstone : course.getCapstones()) {
            refreshedCapstone.add(capstoneService.refreshCapstone(capstone));
        }

        course.setCapstones(refreshedCapstone);

        return course;
    }

    public Capstone refreshCapstone(String courseId, String capstoneId) {
        Optional<Capstone> optionalCapstone = capstoneService.getCapstoneById(capstoneId);
        Capstone capstone = optionalCapstone.orElseThrow(() -> new NoSuchElementException("No Capstone with id: " + capstoneId + " found!"));
        return capstoneService.refreshCapstone(capstone);
    }
}
