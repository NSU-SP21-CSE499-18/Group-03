package study.cse499.socialpostscheduler.repository

import androidx.lifecycle.LiveData
import study.cse499.socialpostscheduler.data.local.ScheduleData

interface ScheduleRepository {
    suspend fun insertScheduleItem(scheduleData: ScheduleData)
    suspend fun deleteScheduleItem(scheduleData: ScheduleData)
    fun observeAllScheduleItem(): LiveData<List<ScheduleData>>
}