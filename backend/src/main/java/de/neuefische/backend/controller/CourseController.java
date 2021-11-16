package de.neuefische.backend.controller;

import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.model.Course;
import de.neuefische.backend.services.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id);
    }


    @GetMapping("/refresh/{id}")
    public Course refreshCourseById(@PathVariable String id) {
        return courseService.getRefreshedCourse(id);
    }

    @GetMapping("/capstone/{courseId}/refresh/{capstoneId}")
    public Course refreshCourseCapstoneById(@PathVariable String courseId, @PathVariable String capstoneId) {
        return courseService.getRefreshedCapstone(courseId, capstoneId);
    }
}
