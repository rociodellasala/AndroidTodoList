package com.example.pam_project.utils;

public enum TaskStatus {
    DONE,
    PENDING;

    public static final String STRING_DONE = "done";
    public static final String STRING_PENDING = "pending";

    public static TaskStatus getStatus(String status) {
        switch (status){
            case "done": return TaskStatus.DONE;
            case "pending": return TaskStatus.PENDING;
            default: return null;
        }
    }

    public static String statusToString(TaskStatus status){
        switch (status){
            case DONE: return STRING_DONE;
            case PENDING: return STRING_PENDING;
            default: return null;
        }
    }
}
