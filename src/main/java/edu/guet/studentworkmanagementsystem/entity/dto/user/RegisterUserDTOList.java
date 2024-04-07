package edu.guet.studentworkmanagementsystem.entity.dto.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTOList implements Serializable {
    @Valid
    private List<RegisterUserDTO> registerUserDTOList;
}
