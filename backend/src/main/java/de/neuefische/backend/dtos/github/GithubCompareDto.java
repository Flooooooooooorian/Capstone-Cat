package de.neuefische.backend.dtos.github;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubCompareDto {

    @JsonProperty("ahead_by")
    private int aheadBy;

    private String branchName;
    private List<GithubCommitDto> commits;
}
