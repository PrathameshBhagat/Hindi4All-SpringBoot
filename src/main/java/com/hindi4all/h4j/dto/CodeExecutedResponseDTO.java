package com.hindi4all.h4j.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CodeExecutedResponseDTO {
    
    String status;

    String stdout;

}
