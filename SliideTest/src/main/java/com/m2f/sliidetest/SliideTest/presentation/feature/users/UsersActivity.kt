package com.m2f.sliidetest.SliideTest.presentation.feature.users

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.m2f.sliidetest.SliideTest.R
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.databinding.ActivityUsersBinding
import com.m2f.sliidetest.SliideTest.presentation.FailureType
import com.m2f.sliidetest.SliideTest.presentation.LoadingDialog
import com.m2f.sliidetest.SliideTest.presentation.ViewModelState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UsersActivity : AppCompatActivity(), View.OnCreateContextMenuListener, CreateUserDialog.OnUserCreatedListener {

    private val usersViewModel by viewModels<UserViewModel>()

    private val binding by lazy { ActivityUsersBinding.inflate(layoutInflater) }

    private val adaper = UsersAdaper(this)

    private val loadingDialog = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        usersViewModel.state.observe({ lifecycle }, ::render)

        binding.usersList.adapter = adaper

        usersViewModel.loadUsers()

        binding.addUser.setOnClickListener {
            CreateUserDialog().apply {
                show(supportFragmentManager, CreateUserDialog.TAG, this@UsersActivity)
            }
        }

    }

    override fun onParametersSet(name: String, email: String, gender: String) {
        usersViewModel.addUser(name, email, gender)
    }

    private fun render(state: ViewModelState<List<User>>) {
        when (state) {
            is ViewModelState.Loading -> loadingState(true)
            is ViewModelState.Success -> onSuccessState(state.data)
            is ViewModelState.Error -> onErrorState(state.failureType)
            is ViewModelState.Empty -> onStateEmpty()
        }
    }

    private fun loadingState(visibility: Boolean) {
        try {
            if (visibility) {
                if(!loadingDialog.isVisible) {
                    loadingDialog.show(supportFragmentManager, LoadingDialog.TAG)
                }
            } else {
                if(loadingDialog.isVisible) {
                    loadingDialog.dismiss()
                }
            }
        } finally {

        }
    }

    private fun onStateEmpty() {
        Snackbar.make(
            binding.root,
            getString(R.string.no_elements),
            Snackbar.LENGTH_SHORT
        ).show()

    }

    private fun onErrorState(failureType: FailureType) {
        val text = when (failureType) {
            FailureType.NotImplemented -> R.string.feature_not_implemented
            else -> R.string.onboarding_login_generic_error
        }
        loadingState(false)
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_SHORT
        )
            .setBackgroundTint(ContextCompat.getColor(this, R.color.error))
            .show()
    }

    private fun onSuccessState(users: List<User>) {
        loadingState(false)
        adaper.submitList(users)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        //todo: select the id of the item and send it to the viewmodel to remove the user
        usersViewModel.removeUser(0)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

}