package study.cse499.socialpostscheduler.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import study.cse499.socialpostscheduler.data.local.ShoppingItemDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}