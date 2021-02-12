package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.google.gson.Gson
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.Meta
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.Pagination
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UsersResponse
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.LastUsersQuery
import com.m2f.sliidetest.SliideTest.helpers.readContentFromFilePath
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


class GetUsersNetworkDatasourceTest {

    private val mockWebServer: MockWebServer = MockWebServer()

    private lateinit var service: UserService

    private lateinit var dataSource: GetUsersNetworkDatasource

    private val mockResponse = UsersResponse(
            code = 200,
            meta = Meta(pagination = Pagination(0, 0, 0, 0)),
            data = emptyList()
    )

    @Before
    fun setUp() {

        val httpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .hostnameVerifier { _, _ -> true }
                .build()

        service = Retrofit.Builder()
                .client(httpClient)
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UserService::class.java)

        dataSource = GetUsersNetworkDatasource(service)

        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path?.contains("/users") == true && request.method == "GET" -> MockResponse()
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                            .setBody(readContentFromFilePath("users_response_ok.json"))
                    else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                }
            }
        }
        mockWebServer.dispatcher = dispatcher
    }


    @Test
    fun `DataSource will call service`() {

        service = mockk()
        dataSource = GetUsersNetworkDatasource(service)
        every { service.getUsers(any()) } returns Observable.just(mockResponse)

        val ts = TestObserver.create<List<UserEntity>>()
        dataSource.getAll(LastUsersQuery).subscribe(ts)

        verifySequence {
            service.getUsers(any())
            service.getUsers(any())
        }

    }

    @Test
    fun `DataSource will map json correctly`() {

        val ts = TestObserver.create<List<UserEntity>>()
        dataSource.getAll(LastUsersQuery).subscribe(ts)

        ts.assertNoErrors()
        ts.assertComplete()
        ts.assertValue { it.count() == 20 }

    }
}