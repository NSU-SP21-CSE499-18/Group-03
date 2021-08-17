package study.cse499.socialpostscheduler.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class TestScheduleFragmentFactory @Inject constructor(): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            LoginFragment::class.java.name -> LoginFragment()
            ScheduleFragment::class.java.name -> ScheduleFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}