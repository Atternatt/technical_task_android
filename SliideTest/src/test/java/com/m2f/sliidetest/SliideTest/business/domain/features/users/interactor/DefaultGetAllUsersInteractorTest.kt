package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.LastUsersQuery
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

    private val useCaseTest = DefaultGetAllUsersInteractor(getRepository)

    private val expectedUsers: List<User> = listOf(
        User(0L, "a", "a@a", Gender.MALE),
        User(1L, "b", "b@b", Gender.FEMALE)
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
    fun `Interactor will use LastUsersQuery`() = runBlockingTest {

        val result = useCaseTest(false)

        coVerify(exactly = 1) {
            getRepository.getAll(ofType(LastUsersQuery::class), any())
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