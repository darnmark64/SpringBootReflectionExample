package com.bits.assetTranslator.scope.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Entity {
    
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("capabilities")
    private String capabilities;
    @JsonProperty("vulnerabilities")
    private String vulnerabilities;
    @JsonProperty("susceptibilities")
    private String susceptibilities;
    @JsonProperty("classes")
    private List<EntityClass> classes;
    @JsonProperty("attributes")
    private Map<String, String> attributes;

    @Override
    public String toString() {
        return "Entity{" + "id:'" + id + "', name:'" + name + "', description:'" + description + "', owner:'" + owner + "',"
                + " capabilities:'" + capabilities + "', vulnerabilities:'" + vulnerabilities + "',"
                + " susceptibilities:'" + susceptibilities + "', classes:" + classes + ", attributes:" + attributes + "}";
    }
    
}
