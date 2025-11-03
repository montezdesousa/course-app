package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.CourseEntity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save

@Composable
fun CourseFormScreen(
    viewModel: CourseViewModel = hiltViewModel(),
    onSaved: ()->Unit = {}
) {
    var id by remember { mutableStateOf((0..10000).random()) }
    var nameCourse by remember { mutableStateOf("") }
    var ectsCourse by remember { mutableStateOf("") }
    var levelCourse by remember { mutableStateOf(LevelCourse.P1) }   // Course level selection

    var ectsError by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Form fields
        TextField(value = nameCourse, onValueChange = { nameCourse = it }, label = { Text("Course Name") })
        if (nameError) {
            Text(
                text = "Course name required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(8.dp))
        TextField(
            value = ectsCourse,
            onValueChange = {
                ectsCourse = it
                val f = it.toFloatOrNull()
                ectsError = f == null || f <= 0f
            },
            label = { Text("Course ECTS") },
            isError = ectsError,
            modifier = Modifier.fillMaxWidth()
        )
        if (ectsError) {
            Text(
                text = "ECTS must be positive",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(8.dp))

        // Level selector simple
        Row {
            listOf(
                LevelCourse.P1,
                LevelCourse.P2,
                LevelCourse.P3,
                LevelCourse.B1,
                LevelCourse.B2,
                LevelCourse.B3,
                LevelCourse.A1,
                LevelCourse.A2,
                LevelCourse.A3,
                LevelCourse.MS,
                LevelCourse.PhD
            ).forEach { l->
                Button(onClick = { levelCourse = l }, modifier = Modifier.padding(end = 8.dp)) {
                    Text(l.value)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // Save Button
        Button(onClick = {
            // Create CourseEntity
            val ectsFloat = ectsCourse.toFloatOrNull()
            if (nameCourse.isBlank()) {
                return@Button
            }

            if (ectsFloat == null || ectsFloat <= 0f) {
                return@Button
            }
            val course = CourseEntity(
                idCourse = id,
                nameCourse = nameCourse,
                ectsCourse = ectsCourse.toFloat(),
                levelCourse = levelCourse
            )
            viewModel.insertCourse(course)
            onSaved()
        }) {
            Icon(Icons.Default.Save, contentDescription = "Save course")
            Spacer(Modifier.width(8.dp))
            Text("Save")
        }
    }
}
