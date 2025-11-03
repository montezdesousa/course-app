package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import kotlinx.coroutines.flow.Flow

class CourseRepository(
    private val courseDao: CourseDao,
) {
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)
}