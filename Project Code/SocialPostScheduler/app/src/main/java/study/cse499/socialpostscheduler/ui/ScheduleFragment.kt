package study.cse499.socialpostscheduler.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_schedule.*
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.viewmodel.ScheduleViewModel
import java.util.*

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    lateinit var viewModel : ScheduleViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        val callback =  object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        btSavePost.setOnClickListener {
            viewModel.insertSchedulePost(etPostContent.text.toString(), Calendar.getInstance().time)
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}