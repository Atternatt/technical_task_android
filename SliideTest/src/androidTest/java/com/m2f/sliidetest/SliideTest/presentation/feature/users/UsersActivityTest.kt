package com.m2f.sliidetest.SliideTest.presentation.feature.users


import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.m2f.sliidetest.SliideTest.di.RxModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@LargeTest
@HiltAndroidTest
@UninstallModules(RxModule::class)
class UsersActivityTest {

    @get:Rule
    var rule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(ActivityTestRule(UsersActivity::class.java))

    @Test
    fun usersActivityTest2() {
    } /*{
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

        val textView = onView(
            allOf(
                withId(R.id.headerTitle), withText("Users"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Users")))
    }*/
}
