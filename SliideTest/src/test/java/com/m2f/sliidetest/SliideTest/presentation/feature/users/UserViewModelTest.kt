package com.m2f.sliidetest.SliideTest.presentation.feature.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.AddUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.DeleteUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.GetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.SchedulerProvider
import com.m2f.sliidetest.SliideTest.core_architecture.error.DataNotFoundException
import com.m2f.sliidetest.SliideTest.core_architecture.error.DeleteObjectFailException
import com.m2f.sliidetest.SliideTest.core_architecture.error.PutObjectFailException
import com.m2f.sliidetest.SliideTest.presentation.FailureType
import com.m2f.sliidetest.SliideTest.presentation.ViewModelState
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UserViewModelTest {

    @get:Rule
    val instantExecutor: TestRule = InstantTaskExecutorRule()

    //region mock values
    private val observer = mockk<Observer<ViewModelState<List<User>>>>()

    private val getAllUsersInteractor: GetAllUsersInteractor = mockk()

    private val addUserInteractor: AddUserInteractor = mockk()

    private val deleteUserInteractor: DeleteUserInteractor = mockk()

    private val schedulerProvider : SchedulerProvider = object : SchedulerProvider {
        override fun ui(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun computation(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun trampoline(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun newThread(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun io(): Scheduler {
            return Schedulers.trampoline()
        }
    }

    private val viewModel: UserViewModel = UserViewModel(getAllUsersInteractor, addUserInteractor, deleteUserInteractor, schedulerProvider)

    private val expectedUsers: List<User> = listOf(
        User(1, "a", "a@a", Gender.MALE),
        User(2, "b", "b@b", Gender.FEMALE)
    )

    private val addedUser = User(3, "c", "c@c", Gender.MALE)

    private val removedUser = User(4, "d", "d@d", Gender.MALE)
    private val failedRemovedUser = User(5, "e", "e@e", Gender.MALE)
    private val failedAddUser = User(6, "f", "f@f", Gender.MALE)
    //endregion

    @Before
    fun setUp() {
        every { getAllUsersInteractor.invoke(any()) } returns Observable.just(expectedUsers)
        every { addUserInteractor.invoke(addedUser.name, addedUser.email, any()) } returns Single.just(addedUser)
        every { addUserInteractor.invoke(failedAddUser.name, failedAddUser.email, any()) } returns Single.error(PutObjectFailException())
        every { deleteUserInteractor.invoke(removedUser.id) } returns Completable.complete()
        every { deleteUserInteractor.invoke(failedRemovedUser.id) } returns Completable.error(DeleteObjectFailException())
        every { observer.onChanged(any()) } just Runs
        viewModel.state.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.state.removeObserver(observer)
    }

    @Test
    fun `Given a ViewModel When loading users viewmodel send correct states`() {

        //When
        viewModel.loadUsers()

        //Then
        verifySequence {
            observer.onChanged(nrefEq(ViewModelState.Loading(true)))
            observer.onChanged(nrefEq(ViewModelState.Success(expectedUsers)))
            observer.onChanged(nrefEq(ViewModelState.Loading(false)))
        }
    }

    @Test
    fun `ViewModel calls for GetAllUsersInteractor to retrieve the users retrieving them from cache if any`() {

        //When
        viewModel.loadUsers(forceRefresh = false)

        //Then
        verify(exactly = 1) { getAllUsersInteractor(false) }

    }

    @Test
    fun `Add user creates a user and updates the user list forcing a refresh`() {
        //When
        viewModel.addUser(addedUser.name, addedUser.email, addedUser.gender.value)

        //Then
        verifySequence {
            addUserInteractor(any(), any(), any())
            getAllUsersInteractor(true)
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Add user send correct states`() = runBlockingTest {
        //When
        viewModel.addUser(addedUser.name, addedUser.email, addedUser.gender.value)

        //Then
        verifySequence {
            observer.onChanged(nrefEq(ViewModelState.Loading(true)))
            observer.onChanged(nrefEq(ViewModelState.Success(expectedUsers)))
            observer.onChanged(nrefEq(ViewModelState.Loading(false)))
        }
    }

    @Test
    fun `Fail to Add user send correct states`() {

        //When
        viewModel.addUser(failedAddUser.name, failedAddUser.email, failedAddUser.gender.value)

        //Then
        verifySequence {
            observer.onChanged(nrefEq(ViewModelState.Loading(true)))
            observer.onChanged(nrefEq(ViewModelState.Error(FailureType.ActionFailed)))
            observer.onChanged(nrefEq(ViewModelState.Loading(false)))
        }
    }

    @Test
    fun `Remove user send correct states`() {

        //When
        viewModel.removeUser(removedUser.id)

        //Then
        verifySequence {
            observer.onChanged(nrefEq(ViewModelState.Loading(true)))
            observer.onChanged(nrefEq(ViewModelState.Success(expectedUsers)))
            observer.onChanged(nrefEq(ViewModelState.Loading(false)))
        }
    }

    @Test
    fun `Fail to Remove user send correct states`() {

        //When
        viewModel.removeUser(failedRemovedUser.id)

        //Then
        verifySequence {
            observer.onChanged(nrefEq(ViewModelState.Loading(true)))
            observer.onChanged(nrefEq(ViewModelState.Error(FailureType.ActionFailed)))
            observer.onChanged(nrefEq(ViewModelState.Loading(false)))
        }
    }
}