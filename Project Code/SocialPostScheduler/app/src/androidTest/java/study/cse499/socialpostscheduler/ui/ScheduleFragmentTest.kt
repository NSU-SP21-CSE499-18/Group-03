package study.cse499.socialpostscheduler.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.launchFragmentInHiltContainer
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ScheduleFragmentTest{
    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: TestScheduleFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
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