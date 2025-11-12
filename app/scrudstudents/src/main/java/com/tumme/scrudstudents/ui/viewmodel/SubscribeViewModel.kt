package com.tumme.scrudstudents.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.CourseRepository
import com.tumme.scrudstudents.data.repository.StudentRepository
import com.tumme.scrudstudents.data.repository.SubscribeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val subscribeRepo: SubscribeRepository,
    private val studentRepo: StudentRepository,
    private val courseRepo: CourseRepository
) : ViewModel() {

    val subscriptions: StateFlow<List<SubscribeEntity>> =
        subscribeRepo.getAllSubscribes()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val students: StateFlow<List<StudentEntity>> =
        studentRepo.getAllStudents().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val courses: StateFlow<List<CourseEntity>> =
        courseRepo.getAllCourses().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun getSubscribesByStudent(studentId: Int): Flow<List<SubscribeEntity>> =
        subscribeRepo.getSubscribesByStudent(studentId)

    fun insertSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        subscribeRepo.insertSubscribe(subscribe)
        _events.emit("Subscription saved")
    }

    fun deleteSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        subscribeRepo.deleteSubscribe(subscribe)
        _events.emit("Subscription deleted")
    }

    suspend fun findSubscribe(studentId: Int, courseId: Int): SubscribeEntity? =
        subscribeRepo.getSubscribesByStudent(studentId).firstOrNull()?.find { it.courseId == courseId }

    fun getSubscribesByCourse(courseId: Int): Flow<List<SubscribeEntity>> =
        subscribeRepo.getSubscribesByCourse(courseId)

    fun updateSubscribeScore(subscribe: SubscribeEntity, newScore: Float) = viewModelScope.launch {
        val updatedSubscribe = subscribe.copy(score = newScore)
        subscribeRepo.insertSubscribe(updatedSubscribe) // REPLACE strategy updates score
        _events.emit("Grade updated for student ${subscribe.studentId}")
    }

    fun getAvailableCourses(studentId: Int, level: com.tumme.scrudstudents.data.local.model.LevelCourse): Flow<List<CourseEntity>> =
        courseRepo.getAllCourses().combine(getSubscribesByStudent(studentId)) { allCourses, enrolled ->
            val enrolledIds = enrolled.map { it.courseId }.toSet()
            allCourses.filter { it.level == level && it.idCourse !in enrolledIds }
        }

    fun getEnrolledCourses(studentId: Int): Flow<List<CourseEntity>> =
        getSubscribesByStudent(studentId).combine(courses) { subs, allCourses ->
            val courseMap = allCourses.associateBy { it.idCourse }
            subs.mapNotNull { courseMap[it.courseId] }
        }
}
