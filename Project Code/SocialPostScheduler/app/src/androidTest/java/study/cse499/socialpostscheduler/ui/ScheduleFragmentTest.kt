package study.cse499.socialpostscheduler.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.data.local.ScheduleData
import study.cse499.socialpostscheduler.getOrAwaitValue
import study.cse499.socialpostscheduler.launchFragmentInHiltContainer
import study.cse499.socialpostscheduler.repository.FakeRepositoryAndroidTest
import study.cse499.socialpostscheduler.viewmodel.ScheduleViewModel
import java.util.*
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ScheduleFragmentTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: TestScheduleFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun addScheduleItemInDatabase(){
        val testViewModel = ScheduleViewModel(FakeRepositoryAndroidTest())
        launchFragmentInHiltContainer<ScheduleFragment> (fragmentFactory = fragmentFactory) {
            viewModel = testViewModel
        }
        onView(withId(R.id.etPostContent)).perform(ViewActions.replaceText("This is test contenet"))
        onView(withId(R.id.btSavePost)).perform(ViewActions.click())
        val scheduleData = ScheduleData("This is test contenet", Calendar.getInstance().time)
        assertThat(testViewModel.schedulePosts.getOrAwaitValue()).isNotEmpty()
    }

    @Test
    fun testBackButtonPress(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ScheduleFragment> (fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }
        pressBack()
        verify(navController).popBackStack()
    }
}