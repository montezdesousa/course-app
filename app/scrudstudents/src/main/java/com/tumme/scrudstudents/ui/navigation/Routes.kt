package com.tumme.scrudstudents.ui.navigation

/**
 * Object that defines all the navigation routes (unique identifiers for each screen).
 * These constants are used by the NavHost and composable destinations to navigate
 * between screens in a type-safe and readable way.
 */
object Routes {
    // --- Student routes ---
    const val STUDENT_LIST = "student_list"
    const val STUDENT_FORM = "student_form"
    const val STUDENT_DETAIL = "student_detail/{studentId}"

    // --- Course routes ---
    const val COURSE_LIST = "course_list"
    const val COURSE_FORM = "course_form"
    const val COURSE_DETAIL = "course_detail/{courseId}"

    // --- Subscription routes ---
    const val SUBSCRIBE_LIST = "subscribe_list"
    const val SUBSCRIBE_FORM = "subscribe_form"

    // --- Authentication routes ---
    const val LOGIN = "login"
    const val REGISTER = "register"
}