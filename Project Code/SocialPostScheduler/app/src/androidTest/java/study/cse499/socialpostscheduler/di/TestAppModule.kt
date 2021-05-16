package study.cse499.socialpostscheduler.di

import android.content.Context
import androidx.room.Room
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import study.cse499.socialpostscheduler.data.local.ScheduleDatabase
import javax.inject.Named

class TestAppModule {
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ScheduleDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}