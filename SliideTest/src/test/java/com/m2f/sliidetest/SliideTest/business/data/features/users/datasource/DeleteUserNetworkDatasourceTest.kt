package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.google.gson.Gson
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.DeleteUserResponse
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.DeleteUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.error.DataNotFoundException
import com.m2f.sliidetest.SliideTest.core_architecture.error.DeleteObjectFailException
import com.m2f.sliidetest.SliideTest.helpers.readContentFromFilePath
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import io.reactivex.Single
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

class DeleteUserNetworkDatasourceTest {
    private val mockWebServer: MockWebServer = MockWebServer()

    private lateinit var service: UserService

    private lateinit var dataSource: DeleteUserNetworkDatasource

    private val mockDeleteUserOk = UserEntity(
        id = 1653,
        name = "Tenali Ramakrishna",
        email = "tenali.ramakrishna@15ceeeeee.com",
        gender = "Male",
        status = "Active",
        created_at = "2021-02-13T03:34:12.608+05:30",
        updated_at = "2021-02-13T03:34:12.608+05:30"
    )

    private val mockDeleteUserKo = UserEntity(
        id = -1,
        name = "Tenali Ramakrishna",
        email = "tenali.ramakrishna@15ceeeeee.com",
        gender = "Male",
        status = "Active",
        created_at = "2021-02-13T03:34:12.608+05:30",
        updated_at = "2021-02-13T03:34:12.608+05:30"
    )


    private val mockOkResponse = DeleteUserResponse(code = 204)
    private val mockKoResponse = DeleteUserResponse(code = 404)

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

        dataSource = DeleteUserNetworkDatasource(service)

        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path?.contains("/users") == true && request.method == "DELETE" -> {
                        val queryParam = request.path?.split("/")?.last() ?: "-1"
                        val responseFile =
                            if (queryParam != "-1") "delete_user_ok_response.json" else "delete_user_ko_response.json"
                        MockResponse()
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                            .setBody(readContentFromFilePath(responseFile))
                    }
                    else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                }
            }
        }
        mockWebServer.dispatcher = dispatcher
    }


    @Test
    fun `DataSource will call service`() {

        service = mockk()
        dataSource = DeleteUserNetworkDatasource(service)
        every { service.deleteUser(any()) } returns Single.just(mockOkResponse)

        val ts = TestObserver.create<UserEntity>()
        dataSource.delete(DeleteUserQuery(mockDeleteUserOk.id))
            .subscribe(ts)

        verifySequence {
            service.deleteUser(mockDeleteUserOk.id)
        }

    }

    @Test
    fun `DataSource will map json correctly if delete success`() {

        val ts = TestObserver.create<UserEntity>()
        dataSource.delete(DeleteUserQuery(mockDeleteUserOk.id))
            .subscribe(ts)

        ts.assertNoErrors()
        ts.assertComplete()

    }

    @Test
    fun `DataSource will map json correctly if delete fails`() {

        val ts = TestObserver.create<UserEntity>()
        dataSource.delete(DeleteUserQuery(mockDeleteUserKo.id))
            .subscribe(ts)

        ts.assertError(DeleteObjectFailException::class.java)
        ts.assertNotComplete()

    }
}