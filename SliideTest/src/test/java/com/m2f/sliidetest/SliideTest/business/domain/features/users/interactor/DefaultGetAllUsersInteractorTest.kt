package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.UsersQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.GetRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.CacheSyncOperation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.MainSyncOperation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


class DefaultGetAllUsersInteractorTest {

    private val getRepository: GetRepository<User> = mockk()

    val useCaseTest = DefaultGetAllUsersInteractor(getRepository)

    private val expectedUsers: List<User> = listOf(
        User("a@a", 1, "a", "aaa"),
        User("b@b", 2, "b", "bbb")
    )

    @Before
    fun setUp() {
        coEvery { getRepository.getAll(any(), any()) } returns Observable.defer {
            Observable.just(
                expectedUsers
            )
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use UsersQuery`() = runBlockingTest {

        val result = useCaseTest(false)

        coVerify(exactly = 1) {
            getRepository.getAll(ofType(UsersQuery::class), any())
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use CacheSyncOperation if refresh flag is set to false`() =
        runBlockingTest {

            val result = useCaseTest(false)

            coVerify(exactly = 1) {
                getRepository.getAll(any(), CacheSyncOperation)
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use MainSyncOperation if refresh flag is set to true`() =
        runBlockingTest {

            val result = useCaseTest(true)

            coVerify(exactly = 1) {
                getRepository.getAll(any(), MainSyncOperation)
            }
        }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor returns a list of users`() = runBlockingTest {

        val ts = TestObserver.create<List<User>>()
        useCaseTest(false).subscribe(ts)
        ts.assertValues(expectedUsers)
    }

}