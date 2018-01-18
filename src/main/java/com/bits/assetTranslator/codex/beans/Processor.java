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
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class Processor extends Asset {
    
    @JsonProperty("description")
    private String description;
    
    @Override
    public String toString() {
        return "Processor {uuid:'" + getUuid() + "', parentUuid:'" + getParentUuid() + "', assetId:'" + getAssetId() + "', "
                + "classification:'" + getClassification() + "', shortName:'" + getShortName() + "', fullName:'" + getFullName() + "', "
                + "aliases:" + getAliases() + ", statusReported:" + isStatusReported() + ", moreInfoLink:'" + getMoreInfoLink() + "', "
                + "countryTrigraph:'" + getCountryTrigraph() + "', alignment:'" + getAlignment() + "', assetType:'" + getAssetType() + "', "
                + "capabilityUuids:" + getCapabilityUuids() + ", description:'" + getDescription()  + "'}";
    }    
}
