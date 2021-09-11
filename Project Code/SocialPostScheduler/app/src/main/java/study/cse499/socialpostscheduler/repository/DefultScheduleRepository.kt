package study.cse499.socialpostscheduler.repository

import androidx.lifecycle.LiveData
import study.cse499.socialpostscheduler.data.local.ScheduleDao
import study.cse499.socialpostscheduler.data.local.ScheduleData
import javax.inject.Inject

class DefaultScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    override suspend fun insertScheduleItem(scheduleData: ScheduleData) {
        scheduleDao.insertScheduleItem(scheduleData)
    }

    override suspend fun deleteScheduleItem(scheduleData: ScheduleData) {
        scheduleDao.deleteScheduleItem(scheduleData)
    }

    override fun observeAllScheduleItem(): LiveData<List<ScheduleData>> {
        return scheduleDao.observeAllScheduleItem()
    }

}