package study.cse499.socialpostscheduler.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduleItem(scheduleData: ScheduleData)

    @Delete
    suspend fun deleteScheduleItem(scheduleData: ScheduleData)

    @Query("SELECT * FROM schedules")
    fun observeAllScheduleItem() : LiveData<List<ScheduleData>>

}