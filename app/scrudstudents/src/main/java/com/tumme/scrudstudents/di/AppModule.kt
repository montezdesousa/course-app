package com.tumme.scrudstudents.di

import android.content.Context
import androidx.room.Room
import com.tumme.scrudstudents.data.local.AppDatabase
import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
 import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.repository.AuthRepository
import com.tumme.scrudstudents.data.repository.CourseRepository
import com.tumme.scrudstudents.data.repository.StudentRepository
import com.tumme.scrudstudents.data.repository.SubscribeRepository
import com.tumme.scrudstudents.data.repository.TeacherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "scrud-db").build()

    @Provides fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()
    @Provides fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()
    @Provides fun provideSubscribeDao(db: AppDatabase): SubscribeDao = db.subscribeDao()

    @Provides
    @Singleton
    fun provideAuthRepository(
        studentDao: StudentDao,
        teacherDao: TeacherDao
    ): AuthRepository = AuthRepository(studentDao, teacherDao)

    @Provides
    @Singleton
    fun provideStudentRepository(studentDao: StudentDao): StudentRepository =
        StudentRepository(studentDao)

    @Provides
    @Singleton
    fun provideTeacherRepository(
        teacherDao: TeacherDao, courseDao: CourseDao, subscribeDao: SubscribeDao
    ): TeacherRepository {
        return TeacherRepository(teacherDao, courseDao, subscribeDao)
    }

    @Provides
    @Singleton
    fun provideCourseRepository(courseDao: CourseDao): CourseRepository =
        CourseRepository(courseDao)

    @Provides
    @Singleton
    fun provideSubscribeRepository(subscribeDao: SubscribeDao): SubscribeRepository =
        SubscribeRepository(subscribeDao)

}