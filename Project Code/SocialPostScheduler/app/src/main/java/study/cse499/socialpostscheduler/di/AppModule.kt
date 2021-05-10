package study.cse499.socialpostscheduler.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import study.cse499.socialpostscheduler.data.local.ScheduleDao
import study.cse499.socialpostscheduler.data.local.ScheduleData
import study.cse499.socialpostscheduler.data.local.ScheduleDatabase
import study.cse499.socialpostscheduler.other.Constants.DATABASE_NAME
import study.cse499.socialpostscheduler.repository.DefaultScheduleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideScheduleDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ScheduleDatabase::class.java, DATABASE_NAME)

    @Singleton
    @Provides
    fun provideDefultScheduleRepository(
        scheduleDao: ScheduleDao
    ) = DefaultScheduleRepository(scheduleDao)

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ScheduleDatabase
    ) = database.shoppingDao()
}