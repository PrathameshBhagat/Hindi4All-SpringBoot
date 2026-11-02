package com.hindi4all.h4j.entity;

public enum ExecutionStatus {
    SUCCESS,
    PENDING,
    RUNNING,
    TRANSPILING, // Kept just for future use
    EXECUTING,
    TIMEOUT,
    FAILED,
    CANCELLED,
}
