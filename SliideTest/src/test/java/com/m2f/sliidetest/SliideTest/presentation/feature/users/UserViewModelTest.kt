package com.m2f.sliidetest.SliideTest.presentation.feature.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.AddUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.DeleteUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.GetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.presentation.FailureType
import com.m2f.sliidetest.SliideTest.presentation.ViewModelState
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
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

    private val testScheduler = TestScheduler()

    private val viewModel: UserViewModel = UserViewModel(getAllUsersInteractor, addUserInteractor, deleteUserInteractor, testScheduler)

    private val expectedUsers: List<User> = listOf(
        User(1, "a", "a@a", Gender.MALE),
        User(2, "b", "b@b", Gender.FEMALE)
    )

    private val addedUser = User(3, "c", "c@c", Gender.MALE)
    //endregion

    @Before
    fun setUp() {
        every { getAllUsersInteractor.invoke(any()) } returns Observable.just(expectedUsers)
        every { addUserInteractor.invoke(any(), any(), any()) } returns Single.just(addedUser)
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
            observer.onChanged(ViewModelState.Loading)
            observer.onChanged(nrefEq(ViewModelState.Success(expectedUsers)))
        }

    }

    @Test
    fun `ViewModel calls for GetAllUsersInteractor to retrieve the users`() {

        //When
        viewModel.loadUsers()

        //Then
        verify(exactly = 1) { getAllUsersInteractor(any()) }

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
    fun `Add user send correct states`() {
        //When
        viewModel.addUser(addedUser.name, addedUser.email, addedUser.gender.value)

        //Then
        verifySequence {
            observer.onChanged(ViewModelState.Loading)
            observer.onChanged(nrefEq(ViewModelState.Success(expectedUsers)))
        }
    }

    @Test
    fun `Remove user is not implemented`() {

        //When
        viewModel.removeUser(0)

        //Then
        verify {
            observer.onChanged(nrefEq(ViewModelState.Error(FailureType.NotImplemented)))
        }
    }
}