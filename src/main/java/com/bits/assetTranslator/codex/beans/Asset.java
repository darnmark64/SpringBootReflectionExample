package com.bits.assetTranslator.codex.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset {
    
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("parentUuid")
    private String parentUuid;
    @JsonProperty("assetId")
    private String assetId;
    @JsonProperty("classification")
    private String classification; 
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("aliases")
    private List<String> aliases;
    @JsonProperty("statusReported")
    private boolean statusReported;
    @JsonProperty("moreInfoLink")
    private String moreInfoLink;
    @JsonProperty("countryTrigraph")
    private String countryTrigraph; 
    @JsonProperty("alignment")
    private String alignment;
    @JsonProperty("assetType")
    private String assetType;
    @JsonProperty("capabilityUuids")
    private List<String> capabilityUuids;

    public List<String> getAliases() {
        synchronized (this) {
            if (aliases == null) {
                aliases = new ArrayList();
            }
        }
        return aliases;
    }
    
    public String getAliasesAsString() {
        synchronized (this) {
            if (aliases == null || aliases.isEmpty()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            int size = aliases.size();
            int count = 0;
            for (String alias : aliases) {
                if (count < (size - 1)) {
                    sb.append(alias + ", ");
                } else {
                    sb.append(alias);
                }
                count++;
            }
            return sb.toString();
        }
    }

    public boolean aliasesSet() {
        return aliases != null;
    }

    public List<String> getCapabilityUuids() {
        synchronized (this) {
            if (capabilityUuids == null) {
                capabilityUuids = new ArrayList();
            }
        }
        return capabilityUuids;
    }

    public boolean capabilityUuidsSet() {
        return capabilityUuids != null;
    }    
}
