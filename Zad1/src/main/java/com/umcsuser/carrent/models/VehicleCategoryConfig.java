package com.umcsuser.carrent.models;

import lombok.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VehicleCategoryConfig {
    private String category;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, String> attributes = new HashMap<>();

    @Builder
    public VehicleCategoryConfig(String category,
                                 Map<String, String> attributes){
        this.category = category;
        this.attributes = attributes == null ? new HashMap<>() : new HashMap<>(attributes);
    }

    public Map<String, Object> getAttributes(){
        return Collections.unmodifiableMap(attributes);
    }

    public void addAttribute(String key, Object value) {
    }

    public void removeAttributes(String key) {
        attributes.remove(key);
    }

    public VehicleCategoryConfig copy(){
        return VehicleCategoryConfig.builder()
                .category(category)
                .attributes(new HashMap<>(attributes))
                .build();
    }


}
