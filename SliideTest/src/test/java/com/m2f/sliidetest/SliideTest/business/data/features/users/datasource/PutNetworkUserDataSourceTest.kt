package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.google.gson.Gson
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.CreateUserResponse
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.CreateUserQuery
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

class PutNetworkUserDataSourceTest {
    private val mockWebServer: MockWebServer = MockWebServer()

    private lateinit var service: UserService

    private lateinit var dataSource: PutNetworkUserDataSource

    private val mockCreatedUser = UserEntity(
            id = 1653,
            name = "Tenali Ramakrishna",
            email = "tenali.ramakrishna@15ceeeeee.com",
            gender = "Male",
            status = "Active",
            created_at = "2021-02-13T03:34:12.608+05:30",
            updated_at = "2021-02-13T03:34:12.608+05:30"
    )

    private val mockResponse = CreateUserResponse(
            code = 201,
            data = mockCreatedUser)

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

        dataSource = PutNetworkUserDataSource(service)

        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path == "/users" && request.method == "POST" -> MockResponse()
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                            .setBody(readContentFromFilePath("create_user_response.json"))
                    else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                }
            }
        }
        mockWebServer.dispatcher = dispatcher
    }


    @Test
    fun `DataSource will call service`() {

        service = mockk()
        dataSource = PutNetworkUserDataSource(service)
        every { service.createUser(any()) } returns Observable.just(mockResponse)

        val ts = TestObserver.create<UserEntity>()
        dataSource.put(CreateUserQuery(mockCreatedUser.name, mockCreatedUser.email, Gender.MALE, "Active"), null)
                .subscribe(ts)

        verifySequence {
            service.createUser(any())
        }

    }

    @Test
    fun `DataSource will map json correctly`() {

        val ts = TestObserver.create<UserEntity>()
        dataSource.put(CreateUserQuery(mockCreatedUser.name, mockCreatedUser.email, Gender.MALE, "Active"), null)
                .subscribe(ts)

        ts.assertNoErrors()
        ts.assertComplete()
        ts.assertValue { it == mockCreatedUser }

    }
}