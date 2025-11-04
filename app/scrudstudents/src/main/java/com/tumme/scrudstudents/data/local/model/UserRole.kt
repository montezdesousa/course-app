package com.tumme.scrudstudents.data.local.model

/**
 * Defines the roles a user can hold in the university system.
 * This enum is essential for managing access rights and dynamic navigation flow.
 *
 * - STUDENT and TEACHER are the main roles required by the assignment.
 * - NONE represents the logged-out or initial loading state.
 */
enum class UserRole {
    STUDENT,
    TEACHER,
    ADMIN,
    NONE
}
