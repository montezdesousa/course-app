package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * StudentEntity represents a student in the local Room database.
 *
 * Purpose:
 * - Defines the schema of the "students" table.
 * - Acts as the source of truth for student data in the database layer.
 *
 * Role in MVVM data flow:
 * 1. DAO reads/writes instances of StudentEntity from/to the database.
 * 2. Repository exposes data to the ViewModel as domain objects.
 * 3. ViewModel holds the UI state and communicates changes to Compose UI.
 *
 * Variables:
 * @param idStudent Unique identifier of the student. Primary key for Room.
 * @param lastName Student's last name.
 * @param firstName Student's first name.
 * @param dateOfBirth Student's date of birth.
 * @param gender Student's gender (likely an enum type: Male, Female, Not concerned).
 *
 * Note: For `Date` and `Gender`, Room requires TypeConverters to store these properly in the database.
 */@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val idStudent: Int,
    val lastName: String,
    val firstName: String,
    val dateOfBirth: Date,
    val gender: Gender
)