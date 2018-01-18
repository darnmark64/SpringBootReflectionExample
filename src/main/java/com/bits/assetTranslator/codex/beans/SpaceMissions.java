package com.bits.assetTranslator.codex.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class SpaceMissions {
    
    @JsonProperty("spaceMissions")
    private List<SpaceMission> spaceMissions;
}
