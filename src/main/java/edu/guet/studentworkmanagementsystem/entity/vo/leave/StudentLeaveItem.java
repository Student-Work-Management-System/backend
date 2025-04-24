package edu.guet.studentworkmanagementsystem.entity.vo.leave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveItem implements Serializable {
    private String leaveId;
    private String studentId;
    private String name;
    private String gradeName;
    private String majorName;
    private String type;
    private String reason;
    private String target;
    private String targetDetail;
    private boolean internship;
    /**
     * 请假时间, YYYY-MM-DD
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDay;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDay;
    private boolean destroyed;
    private String auditId;
    /**
     * 辅导员审核信息
     */
    private String counselorId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate counselorHandleTime;
    private String counselorHandleState;
    /**
     * 副书记审核信息
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String leaderId;
    private LocalDate leaderHandleTime;
    private String leaderHandleState;
    private List<String> evidences;
}
