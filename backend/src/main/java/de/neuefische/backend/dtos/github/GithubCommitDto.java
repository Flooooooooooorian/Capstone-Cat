package de.neuefische.backend.dtos.github;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubCommitDto {

    private GithubDetailedCommitDto commit;
}
