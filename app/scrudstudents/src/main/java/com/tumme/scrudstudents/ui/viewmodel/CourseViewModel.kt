package com.tumme.scrudstudents.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val repo: CourseRepository
) : ViewModel() {

    private val _courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())

    val courses: StateFlow<List<CourseEntity>> = _courses

    // UI event / error Flow
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun deleteCourse(course: CourseEntity) = viewModelScope.launch {
        repo.deleteCourse(course)
        _events.emit("Course deleted")
    }

    fun insertCourse(course: CourseEntity) = viewModelScope.launch {
        repo.insertCourse(course)
        _events.emit("Course inserted")
    }

    suspend fun findCourse(id: Int) = repo.getCourseById(id)

    fun getCoursesByLevel(level: LevelCourse) =
        repo.getCoursesByLevel(level.value)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}