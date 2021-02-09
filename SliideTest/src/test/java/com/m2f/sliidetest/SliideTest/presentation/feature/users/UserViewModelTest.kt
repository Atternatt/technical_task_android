package com.m2f.sliidetest.SliideTest.presentation.feature.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.GetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.presentation.FailureType
import com.m2f.sliidetest.SliideTest.presentation.ViewModelState
import io.mockk.*
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UserViewModelTest {

    @get:Rule
    val instantExecutor: TestRule = InstantTaskExecutorRule()

    private val observer = mockk<Observer<ViewModelState<List<User>>>>()

    private val getAllUsersInteractor: GetAllUsersInteractor = mockk()

    private val viewModel: UserViewModel = UserViewModel(getAllUsersInteractor)

    //region mock values
    private val expectedUsers: List<User> = listOf(
        User("a@a", 1, "a", "aaa"),
        User("b@b", 2, "b", "bbb")
    )
    //endregion

    @Before
    fun setUp() {
        every { getAllUsersInteractor.invoke(any()) } returns Observable.just(expectedUsers)
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
    fun `Add user is not implemented`() {
        //When
        viewModel.addUser()

        //Then
        verify {
            observer.onChanged(nrefEq(ViewModelState.Error(FailureType.NotImplemented)))
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