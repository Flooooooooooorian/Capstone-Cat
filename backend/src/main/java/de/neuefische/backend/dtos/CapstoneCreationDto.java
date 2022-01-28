package de.neuefische.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapstoneCreationDto {
    private String name;
    private String githubApiUrl;
}
