package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    val subscriptions: StateFlow<List<SubscribeEntity>> =
        repo.getAllSubscribes().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun insertSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.insertSubscribe(subscribe)
        _events.emit("Subscription saved")
    }

    fun deleteSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.deleteSubscribe(subscribe)
        _events.emit("Subscription deleted")
    }

    suspend fun findSubscribe(idStudent: Int, idCourse: Int) =
        repo.getSubscribesByStudent(idStudent).firstOrNull()?.find { it.courseId == idCourse }

}
