package com.hindi4all.h4j.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
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