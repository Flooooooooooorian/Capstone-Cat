package de.neuefische.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GithubRepoDto {
    @JsonProperty("default_branch")
    public String defaultBranch;

    private GithubRepoOwnerDto owner;

    @JsonProperty("html_url")
    private String url;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
