package com.bits.assetTranslator.codex.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class Capability {
    
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("parentUuid")
    private String parentUuid;
    @JsonProperty("capabilityId")
    private String capabilityId;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("classification")
    private String classification;

    @Override
    public String toString() {
        return "Capability {uuid:'" + uuid + "', parentUuid:'" + parentUuid + "', capabilityId:'" + capabilityId + "', "
                + "fullName:'" + fullName + "', classification:'" + classification + "'}";
    }
    
}
