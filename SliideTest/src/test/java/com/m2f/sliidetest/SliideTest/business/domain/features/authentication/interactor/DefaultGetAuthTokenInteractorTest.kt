package com.m2f.sliidetest.SliideTest.business.domain.features.authentication.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.model.AuthToken
import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.query.AuthTokenQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.GetRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.CacheOperation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class DefaultGetAuthTokenInteractorTest {

    private val getAuthTokenRepository: GetRepository<AuthToken> = mockk()

    private val useCaseTest = DefaultGetAuthTokenInteractor(getAuthTokenRepository)

    private val expectedToken = AuthToken("something")

    @Before
    fun setUp() {
        coEvery { getAuthTokenRepository.get(any(), any()) } returns Observable.defer {
            Observable.just(expectedToken)
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use CacheOperation`() = runBlockingTest {

        val result = useCaseTest()

        coVerify(exactly = 1) {
            getAuthTokenRepository.get(any(), CacheOperation)
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor will use AuthTokenQuery`() =
        runBlockingTest {

            val result = useCaseTest()

            coVerify(exactly = 1) {
                getAuthTokenRepository.get(AuthTokenQuery, any())
            }
        }


    @Test
    @ExperimentalCoroutinesApi
    fun `Interactor returns an AuthToken`() = runBlockingTest {
        useCaseTest()
            .test()
            .assertValue(expectedToken)
    }

}