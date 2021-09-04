package study.cse499.socialpostscheduler.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_landing.*
import study.cse499.socialpostscheduler.R
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LandingFragment : Fragment(R.layout.fragment_landing) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btSchedulePage.setOnClickListener{
            val action  = LandingFragmentDirections.actionLandingFragmentToScheduleFragment3();
            findNavController().navigate(action)
        }

        btLoginPage.setOnClickListener {
            val action  = LandingFragmentDirections.actionLandingFragmentToLoginFragment();
            findNavController().navigate(action)
        }
    }

}