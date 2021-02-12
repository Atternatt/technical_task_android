package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.CreateUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.PutRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.MainOperation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class DefaultAddUserInteractorTest {

    private val putRepository: PutRepository<User> = mockk()

    private val useCaseTest = DefaultAddUserInteractor(putRepository)

    private val expectedUser: User = User(0L, "a", "a@a", Gender.MALE)

    @Before
    fun setUp() {
        coEvery { putRepository.put(any(), any(), any()) } returns Observable.defer {
            Observable.just(expectedUser)
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use CreateUserQuery`() = runBlockingTest {

        val result = useCaseTest(expectedUser.name, expectedUser.email, expectedUser.gender)

        coVerify(exactly = 1) {
            putRepository.put(ofType(CreateUserQuery::class), any(), any())
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use MainOperation`() =
            runBlockingTest {

                val result = useCaseTest(expectedUser.name, expectedUser.email, expectedUser.gender)

                coVerify(exactly = 1) {
                    putRepository.put(any(), any(), MainOperation)
                }
            }


    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor returns a created User`() = runBlockingTest {

        val ts = TestObserver.create<User>()
        useCaseTest(expectedUser.name, expectedUser.email, expectedUser.gender).subscribe(ts)
        ts.assertValues(expectedUser)
    }
}