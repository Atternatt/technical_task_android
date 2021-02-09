package com.m2f.sliidetest.SliideTest.presentation.feature.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.GetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.addThreadPolicy
import com.m2f.sliidetest.SliideTest.core_architecture.error.DataNotFoundException
import com.m2f.sliidetest.SliideTest.presentation.FailureType
import com.m2f.sliidetest.SliideTest.presentation.ViewModelState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@ActivityRetainedScoped
class UserViewModel @ViewModelInject constructor(private val getAllUsersInteractor: GetAllUsersInteractor) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _state: MutableLiveData<ViewModelState<List<User>>> =
        MutableLiveData(ViewModelState.Loading)
    val state: LiveData<ViewModelState<List<User>>> = _state

    //region public calls

    fun loadUsers(forceRefresh: Boolean = true) {
        compositeDisposable.add(
            getAllUsersInteractor(forceRefresh)
                .addThreadPolicy()
                .subscribeBy(
                    onError = {
                        //todo: @Marc -> Create a generic mapping.
                        _state.value = ViewModelState.Error(
                            when (it) {
                                is DataNotFoundException -> FailureType.DataNotFound
                                else -> FailureType.UnknownError
                            }
                        )
                    },
                    onNext = {
                        _state.value = if (it.isEmpty()) {
                            ViewModelState.Empty
                        } else {
                            ViewModelState.Success(it)
                        }
                    })
        )
    }

    fun addUser() {
        _state.value = ViewModelState.Error(FailureType.NotImplemented)
    }

    fun removeUser(userId: Long) {
        _state.value = ViewModelState.Error(FailureType.NotImplemented)
    }
//endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}