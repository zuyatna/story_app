package com.zuyatna.storyapp.ui.view.fragment

import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.di.ApiModule
import com.zuyatna.storyapp.utils.EspressoIdlingResource
import com.zuyatna.storyapp.utils.JsonConverter
import com.zuyatna.storyapp.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@ExperimentalPagingApi
@HiltAndroidTest
class StoryFragmentTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        ApiModule.apiConfig = "http://127.0.0.1:8080/"

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun launchHomeFragmentSuccess() {
        launchFragmentInHiltContainer<StoryFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        Espresso.onView(withId(R.id.toolbar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.rv_story)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("Dimas")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun launchHomeFragmentEmptyOrError() {
        launchFragmentInHiltContainer<StoryFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response_empty.json"))

        mockWebServer.enqueue(mockResponse)

        Espresso.onView(withId(R.id.tv_not_found)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.tv_error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}