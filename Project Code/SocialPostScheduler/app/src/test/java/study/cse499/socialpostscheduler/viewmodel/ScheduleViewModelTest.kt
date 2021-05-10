package study.cse499.socialpostscheduler.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import study.cse499.socialpostscheduler.MainCoroutineRule
import study.cse499.socialpostscheduler.getOrAwaitValueTest
import study.cse499.socialpostscheduler.other.Status
import study.cse499.socialpostscheduler.repository.FakeScheduleRepository
import java.util.*


@ExperimentalCoroutinesApi
class ScheduleViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ScheduleViewModel

    @Before
    fun setup(){
        viewModel = ScheduleViewModel(FakeScheduleRepository())
    }

    @Test
    fun `insert schedule post with empty content, returns error`(){
        viewModel.insertSchedulePost("", Calendar.getInstance().time)
        var value = viewModel.insertScheduleItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
}