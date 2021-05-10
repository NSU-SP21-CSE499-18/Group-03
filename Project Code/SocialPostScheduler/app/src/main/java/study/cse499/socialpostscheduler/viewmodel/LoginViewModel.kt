package study.cse499.socialpostscheduler.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import study.cse499.socialpostscheduler.repository.ScheduleRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ScheduleRepository
) {
}