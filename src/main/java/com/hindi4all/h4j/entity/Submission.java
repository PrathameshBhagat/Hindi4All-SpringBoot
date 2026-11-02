package com.hindi4all.h4j.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "submission_id", nullable = false)
    private UUID submissionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_status", nullable = false)
    private ExecutionStatus status;


    @Lob
    @Column(name = "code", nullable = false)
    private String code;

    @Lob
    @Column(name = "std_out", nullable = false)
    private String stdOut;

    @Lob
    @Column(name = "std_err")
    private String stdErr;

    @Column(name = "std_in")
    private String stdIn;


    @Column(name = "completed_at", nullable = false)
    private Instant completedAt;

    @Column(name = "execution_time_millis", nullable = false)
    private Long executionTimeMillis;

    @Column(name = "updated_at_utc", nullable = false)
    private Instant updatedAtUTC;

    @Column(name = "created_at_utc", nullable = false)
    private Instant createdAtUTC;

}

