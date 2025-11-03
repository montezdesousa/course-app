package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao

class TeacherRepository(
    private val teacherDao: TeacherDao,
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao,
) {
}