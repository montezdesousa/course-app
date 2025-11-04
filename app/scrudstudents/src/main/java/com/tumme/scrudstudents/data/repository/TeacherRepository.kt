package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepository @Inject constructor(
    private val courseDao: CourseDao
) {

    // Get all courses assigned to a specific teacher
    fun getCoursesByTeacher(teacherId: String): Flow<List<CourseEntity>> {
        return courseDao.getCoursesByTeacher(teacherId)
    }

    suspend fun addCourse(course: CourseEntity) {
        courseDao.insert(course)
    }

    suspend fun deleteCourse(course: CourseEntity) {
        courseDao.delete(course)
    }
}
