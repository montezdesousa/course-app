package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao

class AuthRepository(
    private val studentDao: StudentDao,
    private val teacherDao: TeacherDao,
) {
}