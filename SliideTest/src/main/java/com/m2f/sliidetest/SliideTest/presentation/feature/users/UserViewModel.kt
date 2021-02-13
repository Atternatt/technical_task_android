package com.m2f.sliidetest.SliideTest.presentation.feature.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.AddUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.GetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.DeleteUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.addThreadPolicy
import com.m2f.sliidetest.SliideTest.core_architecture.error.DataNotFoundException
import com.m2f.sliidetest.SliideTest.presentation.FailureType
import com.m2f.sliidetest.SliideTest.presentation.ViewModelState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy


@ActivityRetainedScoped
class UserViewModel @ViewModelInject constructor(
    private val getAllUsersInteractor: GetAllUsersInteractor,
    private val addUserInteractor: AddUserInteractor,
    private val deleteUserInteractor: DeleteUserInteractor,
    private val mainScheduler: Scheduler) :
        ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _state: MutableLiveData<ViewModelState<List<User>>> =
            MutableLiveData(ViewModelState.Loading)
    val state: LiveData<ViewModelState<List<User>>> = _state

    //region public calls

    fun loadUsers(forceRefresh: Boolean = true) {
        _state.value = ViewModelState.Loading
        compositeDisposable.add(
                getAllUsersInteractor(forceRefresh)
                        .addThreadPolicy(mainScheduler)
                        .subscribeBy(
                                onError = {
                                    _state.value = it.toViewState()
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

    fun addUser(name: String, email: String, gender: String) {
        _state.value = ViewModelState.Loading

        val genderObj = when (gender) {
            "Female" -> Gender.FEMALE
            else -> Gender.MALE
        }

        compositeDisposable += addUserInteractor(name, email, genderObj)
                .flatMapObservable { getAllUsersInteractor(true) }
                .addThreadPolicy(mainScheduler)
                .subscribeBy(
                        onError = { _state.value = it.toViewState() },
                        onNext = {
                            _state.value = if (it.isEmpty()) {
                                ViewModelState.Empty
                            } else {
                                ViewModelState.Success(it)
                            }
                        }
                )
    }

    fun removeUser(userId: Long) {
        _state.value = ViewModelState.Error(FailureType.NotImplemented)
    }
//endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun Throwable.toViewState(): ViewModelState.Error = ViewModelState.Error(when (this) {
        is DataNotFoundException -> FailureType.DataNotFound
        else -> FailureType.UnknownError
    })
}