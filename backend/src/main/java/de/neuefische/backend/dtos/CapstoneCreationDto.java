package de.neuefische.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapstoneCreationDto {
    private String id = UUID.randomUUID().toString();
    private String name;
    private String githubRepoUrl;
}
