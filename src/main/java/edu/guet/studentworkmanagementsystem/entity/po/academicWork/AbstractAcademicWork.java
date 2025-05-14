package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AcademicWorkPaper.class, name = "paper"),
        @JsonSubTypes.Type(value = AcademicWorkSoft.class, name = "soft"),
        @JsonSubTypes.Type(value = AcademiciWorkPatent.class, name = "patent")
})
public interface AbstractAcademicWork {

}
