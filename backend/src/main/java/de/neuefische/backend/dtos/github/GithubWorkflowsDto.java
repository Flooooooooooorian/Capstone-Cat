package de.neuefische.backend.dtos.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GithubWorkflowsDto {
    @JsonProperty("total_count")
    private int cout;
    private List<GithubWorkflowDto> workflows;
}
