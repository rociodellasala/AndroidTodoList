package com.example.pam_project.utils.constants

enum class TaskStatus {
    DONE, PENDING;

    companion object {
        const val STRING_DONE = "done"
        const val STRING_PENDING = "pending"
        fun getStatus(status: String?): TaskStatus? {
            return when (status) {
                "done" -> DONE
                "pending" -> PENDING
                else -> null
            }
        }

        fun statusToString(status: TaskStatus?): String? {
            return when (status) {
                DONE -> STRING_DONE
                PENDING -> STRING_PENDING
                else -> null
            }
        }
    }
}