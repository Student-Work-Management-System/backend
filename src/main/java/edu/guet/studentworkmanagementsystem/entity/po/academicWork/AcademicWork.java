package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StudentPaper.class, name = "paper"),
        @JsonSubTypes.Type(value = StudentSoft.class, name = "soft"),
        @JsonSubTypes.Type(value = StudentPatent.class, name = "patent")
})
public interface AcademicWork {

}
