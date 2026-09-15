package com.hindi4all.h4j.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@AllArgsConstructor 
public class User{

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;

    private String name ;
    
    @Column(name = "display_name")
    private String displayName ;

    private String email;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}