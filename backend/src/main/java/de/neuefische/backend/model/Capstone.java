package de.neuefische.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("capstones")
public class Capstone {
    @Id
    private String id;
    private String githubApiUrl;
    private String qualityBadgeUrl;
    private String coverageBadgeUrl;

    private String studentName;
    private String url;
    private LocalDate updatedAt;
    private int allCommits;
    private int mainCommits;
    private int allPulls;
    private int openPulls;
}
