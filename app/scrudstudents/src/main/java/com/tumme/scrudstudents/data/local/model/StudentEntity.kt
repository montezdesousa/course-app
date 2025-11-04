package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val idStudent: Int = 0,
    val username: String,
    val password: String,
    val lastName: String,
    val firstName: String,
    val dateOfBirth: Date,
    val gender: Gender
)