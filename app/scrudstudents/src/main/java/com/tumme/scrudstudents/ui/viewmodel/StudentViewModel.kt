package com.tumme.scrudstudents.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.repository.CourseRepository
import com.tumme.scrudstudents.data.repository.SubscribeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val courseRepo: CourseRepository,
    private val subscribeRepo: SubscribeRepository
) : ViewModel() {

    // Courses the student is enrolled in
    fun getEnrolledCourses(studentId: Int): Flow<List<CourseEntity>> =
        subscribeRepo.getSubscribesByStudent(studentId)
            .combine(courseRepo.getAllCourses()) { subscribes, courses ->
                courses.filter { course ->
                    subscribes.any { it.courseId == course.idCourse }
                }
            }

    fun getAvailableCourses(studentId: Int, level: LevelCourse): Flow<List<CourseEntity>> =
        courseRepo.getCoursesByLevel(level.value)
            .combine(subscribeRepo.getSubscribesByStudent(studentId)) { courses, subscribes ->
                courses.filter { course ->
                    subscribes.none { it.courseId == course.idCourse }
                }
            }

    fun enrollStudent(studentId: Int, courseId: Int) = viewModelScope.launch {
        subscribeRepo.insertSubscribe(
            com.tumme.scrudstudents.data.local.model.SubscribeEntity(
                studentId = studentId,
                courseId = courseId,
                score = 0f // initial score
            )
        )
    }
}
