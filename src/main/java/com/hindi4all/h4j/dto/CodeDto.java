package com.hindi4all.h4j.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDto {

    String ID;

    String code;

    @Override
    public String toString() {
        return "{ 'ID' : '" + this.ID + "' , 'code' : ' " + this.code + " ' }" ;
    }
    
}
