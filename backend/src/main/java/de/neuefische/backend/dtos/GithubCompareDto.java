package de.neuefische.backend.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubCompareDto {

    @JsonProperty("ahead_by")
    private int aheadBy;
}
