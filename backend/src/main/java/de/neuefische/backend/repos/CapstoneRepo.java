package de.neuefische.backend.repos;

import de.neuefische.backend.model.Capstone;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapstoneRepo extends PagingAndSortingRepository<Capstone, String> {
    List<Capstone> findAll();
}
