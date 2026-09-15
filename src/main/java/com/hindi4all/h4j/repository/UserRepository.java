package com.hindi4all.h4j.repository;

import java.util.UUID;
import com.hindi4all.h4j.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,UUID>{

    User findByName(String name);

}