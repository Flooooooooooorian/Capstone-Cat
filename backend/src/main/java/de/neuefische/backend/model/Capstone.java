package de.neuefische.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
}
