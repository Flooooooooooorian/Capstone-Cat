package de.neuefische.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreationDto {
    private String name;
    private List<CapstoneCreationDto> capstones;
}
