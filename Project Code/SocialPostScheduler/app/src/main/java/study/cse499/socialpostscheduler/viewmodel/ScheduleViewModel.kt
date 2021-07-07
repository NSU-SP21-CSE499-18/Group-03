package study.cse499.socialpostscheduler.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import study.cse499.socialpostscheduler.data.local.ScheduleData
import study.cse499.socialpostscheduler.other.Constants.MAX_CHARACTER_LIMIT
import study.cse499.socialpostscheduler.other.Event
import study.cse499.socialpostscheduler.other.Resource
import study.cse499.socialpostscheduler.repository.ScheduleRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel(){
    val schedulePosts = repository.observeAllScheduleItem()

    private val _insertScheduleItemStatus = MutableLiveData<Event<Resource<ScheduleData>>>()
    val insertScheduleItemStatus : LiveData<Event<Resource<ScheduleData>>> = _insertScheduleItemStatus

    fun insertSchedulePostIntoDb(scheduleData: ScheduleData) = viewModelScope.launch {
        repository.insertScheduleItem(scheduleData)
    }

    fun deleteSchedulePost(scheduleData: ScheduleData) = viewModelScope.launch{
        repository.deleteScheduleItem(scheduleData)
    }

    fun insertSchedulePost(postContent: String, scheduleTime: Date?) {
        if(postContent.isEmpty() || scheduleTime == null){
            _insertScheduleItemStatus.postValue(Event(Resource.error("data null or empty", null)))
            return
        }
        if(postContent.length > MAX_CHARACTER_LIMIT){
            _insertScheduleItemStatus.postValue(Event(Resource.error("content max length exceed", null)))
            return
        }

//        if(isDateLessThanNow(scheduleTime)){
//            _insertScheduleItemStatus.postValue(Event(Resource.error("schedule date is less than current time", null)))
//            return
//        }

        scheduleTime?.let {
            val scheduleData = ScheduleData(postContent, scheduleTime)
            insertSchedulePostIntoDb(scheduleData)
            _insertScheduleItemStatus.postValue(Event(Resource.success(scheduleData)))
        }
    }

    private fun isDateLessThanNow(scheduleDate: Date?): Boolean{
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        if(currentTime.before(scheduleDate))
            return false;
        return true;
    }

}