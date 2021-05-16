package study.cse499.socialpostscheduler.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.viewmodel.LoginViewModel
import study.cse499.socialpostscheduler.viewmodel.ScheduleViewModel

class ScheduleFragment () : Fragment(R.layout.fragment_schedule) {
    lateinit var viewModel : ScheduleViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
    }
}