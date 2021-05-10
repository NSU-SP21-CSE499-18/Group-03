package study.cse499.socialpostscheduler.data.local

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import study.cse499.socialpostscheduler.other.DateTimeConverter
import java.util.*

@Entity(tableName = "schedules")
data class ScheduleData(
    var postContent: String,
    @TypeConverters(DateTimeConverter::class)
    var scheduleTime: Date
)