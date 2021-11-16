package de.neuefische.backend.repos;

import de.neuefische.backend.model.Course;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends PagingAndSortingRepository<Course, String> {
    List<Course> findAll();
    Optional<Course> findById(String id);
}
