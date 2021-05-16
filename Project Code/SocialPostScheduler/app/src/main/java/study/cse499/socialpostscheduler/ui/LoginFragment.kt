package study.cse499.socialpostscheduler.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_login.*
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.viewmodel.LoginViewModel

class LoginFragment (): Fragment(R.layout.fragment_login) {
    lateinit var viewModel: LoginViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        loginButton.setOnClickListener {
            LoginFragmentDirections.actionLoginFragmentToScheduleFragment()
        }

    }
}