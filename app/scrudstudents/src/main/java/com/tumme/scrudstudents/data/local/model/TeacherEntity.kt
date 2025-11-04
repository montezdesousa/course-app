package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true)
    val idTeacher: Int = 0,
    val username: String,
    val password: String,
    val lastName: String,
    val firstName: String,
    val dateOfBirth: Date,
    val gender: Gender,
    val photoUri: String? = null
)
