package de.neuefische.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapstoneDto {
    private String id;
    private String studentName;
    private String url;
    private LocalDate updatedAt;
    private int allCommits;
    private int mainCommits;
    private int allPulls;
    private int openPulls;
    private String qualityBadgeUrl;
    private String coverageBadgeUrl;
}
