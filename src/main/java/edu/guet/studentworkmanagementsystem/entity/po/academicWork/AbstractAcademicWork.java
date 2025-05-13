package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Paper.class, name = "paper"),
        @JsonSubTypes.Type(value = Soft.class, name = "soft"),
        @JsonSubTypes.Type(value = Patent.class, name = "patent")
})
public interface AbstractAcademicWork {

}
