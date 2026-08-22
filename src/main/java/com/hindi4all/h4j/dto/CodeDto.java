package com.hindi4all.h4j.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeDto {

    String ID;

    String code;

    @Override
    public String toString() {
        return "{ 'ID' : '" + this.ID + "' , 'code' : ' " + this.code + " ' }" ;
    }
    
}
