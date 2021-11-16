package de.neuefische.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubDetailedCommitDto {
   private GithubCommitterDto committer;
}
