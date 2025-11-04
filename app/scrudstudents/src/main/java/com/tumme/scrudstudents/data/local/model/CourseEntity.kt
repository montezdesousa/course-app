package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val idCourse: Int = 0,
    val name: String,
    val ects: Float,
    val level: LevelCourse,
    val teacherId: Int? = null
)