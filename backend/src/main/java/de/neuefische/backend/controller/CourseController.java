package de.neuefische.backend.controller;

import de.neuefische.backend.dtos.CourseCreationDto;
import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.model.Course;
import de.neuefische.backend.services.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping
    public Course createCourse(@RequestBody CourseCreationDto courseDto) {
        return courseService.createCourse(courseDto);
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id);
    }

    @GetMapping("/{id}/refresh")
    public Course refreshCourseById(@PathVariable String id) {
        return courseService.refreshCourse(id);
    }

    @GetMapping("/{courseId}/capstones/{capstoneId}/refresh")
    public Capstone refreshCourseCapstoneById(@PathVariable String capstoneId) {
        return courseService.refreshCapstone(capstoneId);
    }
}
