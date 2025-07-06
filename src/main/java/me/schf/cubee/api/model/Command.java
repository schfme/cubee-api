package me.schf.cubee.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Command(Transformation transformation) {
    @JsonCreator
    public Command(
        @JsonProperty("transformation") Transformation transformation
    ) {
        this.transformation = transformation;
    }
}


