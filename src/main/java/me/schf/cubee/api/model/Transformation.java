package me.schf.cubee.api.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "type"
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = Rotate.class, name = "ROTATE"),
	    @JsonSubTypes.Type(value = Translate.class, name = "TRANSLATE"),
	    @JsonSubTypes.Type(value = Scale.class, name = "SCALE"),
	    @JsonSubTypes.Type(value = Shear.class, name = "SHEAR")
	})
public sealed interface Transformation permits Rotate, Scale, Shear, Translate {

}

