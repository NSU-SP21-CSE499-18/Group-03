package study.cse499.socialpostscheduler.data

import androidx.room.Entity

@Entity(tableName = "schedule")
data class ScheduleData(
    var postContent: String
)