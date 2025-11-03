package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Int,
    viewModel: CourseViewModel = hiltViewModel(),
    onBack: ()->Unit = {}
) {
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    LaunchedEffect(courseId) {
        course = viewModel.findCourse(courseId)
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Course details") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
        })
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (course == null) {
                Text("Loading...")
            } else {
                Text("Name: ${course!!.nameCourse}")
                Text("ECTS: ${course!!.ectsCourse}")
                Text("Level: ${course!!.levelCourse.value}")
            }
        }
    }
}
