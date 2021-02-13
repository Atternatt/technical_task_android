package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.DeleteUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.DeleteRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.MainOperation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class DefaultDeleteUserInteractorTest {

    private val repository: DeleteRepository = mockk()

    private val useCaseTest = DefaultDeleteUserInteractor(repository)

    private val usertoRemove: User = User(0L, "a", "a@a", Gender.MALE)

    @Before
    fun setUp() {
        coEvery { repository.delete(any(), any()) } returns Completable.complete()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use DeleteUserQuery`() = runBlockingTest {

        val result = useCaseTest(usertoRemove.id)

        coVerify(exactly = 1) {
            repository.delete(ofType(DeleteUserQuery::class), any())
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use MainOperation`() =
        runBlockingTest {

            val result = useCaseTest(usertoRemove.id)

            coVerify(exactly = 1) {
                repository.delete(any(), MainOperation)
            }
        }


    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor completes without errors`() = runBlockingTest {

        val ts = TestObserver.create<User>()
        useCaseTest(usertoRemove.id).subscribe(ts)
        ts.assertNoErrors()
        ts.assertComplete()
    }
}