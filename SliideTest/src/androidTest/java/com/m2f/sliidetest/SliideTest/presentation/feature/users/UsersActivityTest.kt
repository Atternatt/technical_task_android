package com.m2f.sliidetest.SliideTest.presentation.feature.users


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.m2f.sliidetest.SliideTest.R
import com.m2f.sliidetest.SliideTest.TestNetworkDispatcher
import com.m2f.sliidetest.SliideTest.business.data.EndpointModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@LargeTest
@HiltAndroidTest
@UninstallModules(EndpointModule::class)
class UsersActivityTest {

    private val activityRule = ActivityTestRule(UsersActivity::class.java, true, false)

    @get:Rule
    var rule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(activityRule)


    @Before
    fun setUpWebServer() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testOnAppStart() {
        activityRule.launchActivity(null)
        val recyclerView = onView(
            allOf(
                withId(R.id.usersList),
                withParent(
                    allOf(
                        withId(R.id.content),
                        withParent(withId(android.R.id.content))
                    )
                ),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        recyclerView.check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            assertThat((view as? RecyclerView)?.adapter?.itemCount, `is`(20))
        }
    }
}
