package ru.example.rickandmortyproject.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.example.rickandmortyproject.MainActivity
import ru.example.rickandmortyproject.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testNavigationBetweenTabs() {
        onView(withId(R.id.menu_item_characters))
            .perform(click())
        onView(withId(R.id.fragment_container))
            .check(matches(isDisplayed()))

        onView(withId(R.id.menu_item_episodes))
            .perform(click())
        onView(withId(R.id.fragment_container))
            .check(matches(isDisplayed()))

        onView(withId(R.id.menu_item_locations))
            .perform(click())
        onView(withId(R.id.fragment_container))
            .check(matches(isDisplayed()))
    }
}
