package com.bits.assetTranslator.scope.beans;

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
public class EntityClass {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("attributeKeys")
    private List<String> attributeKeys;

    @Override
    public String toString() {
        return "EntityClass{" + "id:'" + id + "', name:'" + name + "', descripiton:'" + description + "', attributeKeys:" + attributeKeys + "}";
    }

}
