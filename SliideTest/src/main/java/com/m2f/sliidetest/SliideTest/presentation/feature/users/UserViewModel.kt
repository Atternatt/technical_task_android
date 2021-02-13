package com.m2f.sliidetest.SliideTest.presentation.feature.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.AddUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.DeleteUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.GetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.SchedulerProvider
import com.m2f.sliidetest.SliideTest.core_architecture.addThreadPolicy
import com.m2f.sliidetest.SliideTest.core_architecture.error.DataNotFoundException
import com.m2f.sliidetest.SliideTest.core_architecture.error.DeleteObjectFailException
import com.m2f.sliidetest.SliideTest.core_architecture.error.PutObjectFailException
import com.m2f.sliidetest.SliideTest.presentation.FailureType
import com.m2f.sliidetest.SliideTest.presentation.ViewModelState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy


@ActivityRetainedScoped
class UserViewModel @ViewModelInject constructor(
    private val getAllUsersInteractor: GetAllUsersInteractor,
    private val addUserInteractor: AddUserInteractor,
    private val deleteUserInteractor: DeleteUserInteractor,
    private val schedulerProvider: SchedulerProvider
) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _state: MutableLiveData<ViewModelState<List<User>>> = MutableLiveData()
    val state: LiveData<ViewModelState<List<User>>> = _state

    //region public calls

    fun loadUsers(forceRefresh: Boolean = true) {
        _state.value = ViewModelState.Loading(true)
        compositeDisposable +=
            getAllUsersInteractor(forceRefresh)
                .addThreadPolicy(schedulerProvider)
                .subscribeBy(
                    onError = ::onError,
                    onNext = ::onSuccess
                )
    }

    fun addUser(name: String, email: String, gender: String) {
        _state.value = ViewModelState.Loading(true)

        val genderObj = when (gender) {
            "Female" -> Gender.FEMALE
            else -> Gender.MALE
        }

        compositeDisposable += addUserInteractor(name, email, genderObj)
            .flatMapObservable { getAllUsersInteractor(true) }
            .addThreadPolicy(schedulerProvider)
            .subscribeBy(
                onError = ::onError,
                onNext = ::onSuccess
            )
    }

    fun removeUser(userId: Long) {
        _state.value = ViewModelState.Loading(true)
        compositeDisposable += deleteUserInteractor(userId)
            .andThen(getAllUsersInteractor(forceRefresh = true))
            .addThreadPolicy(schedulerProvider)
            .subscribeBy(
                onError = ::onError,
                onNext = ::onSuccess
            )
    }
//endregion

    private fun onError(throwable: Throwable) {
        _state.value = throwable.toViewState()
        _state.value = ViewModelState.Loading(false)
    }

    private fun onSuccess(userList: List<User>) {
        _state.value = if (userList.isEmpty()) {
            ViewModelState.Empty
        } else {
            ViewModelState.Success(userList)
        }
        _state.value = ViewModelState.Loading(false)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun Throwable.toViewState(): ViewModelState.Error = ViewModelState.Error(
        when (this) {
            is DataNotFoundException -> FailureType.DataNotFound
            is PutObjectFailException, is DeleteObjectFailException -> FailureType.ActionFailed
            else -> FailureType.UnknownError
        }
    )
}