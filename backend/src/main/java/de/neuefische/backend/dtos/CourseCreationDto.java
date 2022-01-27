package de.neuefische.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreationDto {
    private String name;
    private List<CapstoneCreationDto> capstones;
}
