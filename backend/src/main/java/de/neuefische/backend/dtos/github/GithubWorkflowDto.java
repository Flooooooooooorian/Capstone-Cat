package de.neuefische.backend.dtos.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GithubWorkflowDto {
    private String name;
    @JsonProperty("badge_url")
    private String badgeUrl;
}
