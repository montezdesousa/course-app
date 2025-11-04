package com.tumme.scrudstudents.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val repository: TeacherRepository
) : ViewModel() {

    // Flow of courses for a specific teacher
    fun getCoursesForTeacher(teacherId: Int): StateFlow<List<CourseEntity>> {
        return repository.getCoursesByTeacher(teacherId.toString())
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun addCourse(course: CourseEntity) = viewModelScope.launch {
        repository.addCourse(course)
    }

    fun deleteCourse(course: CourseEntity) = viewModelScope.launch {
        repository.deleteCourse(course)
    }
}
