package de.neuefische.backend.dtos.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GithubPullComment {
    private GithubPullCommentUser user;
    private String body;
}
