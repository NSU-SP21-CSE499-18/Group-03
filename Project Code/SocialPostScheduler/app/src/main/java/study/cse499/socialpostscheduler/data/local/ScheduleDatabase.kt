package study.cse499.socialpostscheduler.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ScheduleData::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun shoppingDao() : ScheduleDao
}