package study.cse499.socialpostscheduler.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import study.cse499.socialpostscheduler.data.local.ScheduleData

class FakeRepositoryAndroidTest : ScheduleRepository {
    private val scheduleItems =  mutableListOf<ScheduleData>()
    private val observeScheduleItems = MutableLiveData<List<ScheduleData>>(scheduleItems)
    private var shouldReturnNetworkError = false

    fun shouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value;
    }
    private fun refreshLiveData(){
        observeScheduleItems.postValue(scheduleItems)
    }
    override suspend fun insertScheduleItem(scheduleData: ScheduleData) {
        scheduleItems.add(scheduleData)
        refreshLiveData()
    }

    override suspend fun deleteScheduleItem(scheduleData: ScheduleData) {
        scheduleItems.remove(scheduleData)
        refreshLiveData()
    }

    override fun observeAllScheduleItem(): LiveData<List<ScheduleData>> {
        return observeScheduleItems
    }
}