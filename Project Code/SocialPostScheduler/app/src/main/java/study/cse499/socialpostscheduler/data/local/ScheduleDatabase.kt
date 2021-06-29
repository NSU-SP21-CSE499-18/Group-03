package study.cse499.socialpostscheduler.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import study.cse499.socialpostscheduler.other.DateTimeConverter

@Database(
    entities = [ScheduleData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun shoppingDao() : ScheduleDao
}