package com.example.pam_project.utils;

import java.util.Arrays;
import java.util.List;

public class StatusMapper {

    public static TaskStatus toTaskStatusMapper(final String status) {
        switch(status) {
            case "pending":
                return TaskStatus.PENDING;
            case "done":
                return TaskStatus.DONE;
            default:
                return null;
        }
    }

    public static String toStringStatus(final TaskStatus status) {
        if(status.equals(TaskStatus.PENDING)) {
            return "pending";
        } else {
            return "done";
        }
    }
}
