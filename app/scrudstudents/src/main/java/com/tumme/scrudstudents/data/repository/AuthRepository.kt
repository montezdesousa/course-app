package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.UserRole
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val studentDao: StudentDao,
    private val teacherDao: TeacherDao
) {

    /**
     * Data class to return the result of a successful login attempt, including
     * the user's ID and their role (Student or Teacher).
     */
    data class AuthResult(
        val userId: Int,
        val username: String,
        val role: UserRole,
        val levelOfStudy: LevelCourse? = null
    )

    /**
     * Performs a dummy login check against both Student and Teacher tables.
     *
     * @return AuthResult if credentials match a user, or null if login fails.
     */
    suspend fun login(username: String, password: String): AuthResult? {
        // 1. Check Student table
        val student = studentDao.getStudentByUsernameAndPassword(username, password)
        if (student != null) {
            return AuthResult(student.idStudent, username, UserRole.STUDENT, student.levelOfStudy)
        }

        // 2. Check Teacher table
        val teacher = teacherDao.getTeacherByUsernameAndPassword(username, password)
        if (teacher != null) {
            return AuthResult(teacher.idTeacher, username, UserRole.TEACHER)
        }

        return null // Login failed
    }

    /**
     * Registers a new Student user.
     * @param student The new StudentEntity to insert.
     */
    suspend fun registerStudent(student: StudentEntity) {
        studentDao.insert(student)
    }

    /**
     * Registers a new Teacher user.
     * @param teacher The new TeacherEntity to insert.
     */
    suspend fun registerTeacher(teacher: TeacherEntity) {
        teacherDao.insert(teacher)
    }
}
