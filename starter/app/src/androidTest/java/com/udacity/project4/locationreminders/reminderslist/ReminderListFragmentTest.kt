package com.udacity.project4.locationreminders.reminderslist

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

//    : test the navigation of the fragments.
//    : test the displayed data on the UI.
//    : add testing for the error messages.
    @Test
    fun saveReminderTest() {
        val navController = mock(NavController::class.java)
        val container = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        container.onFragment {
            it.view?.let { it1 -> Navigation.setViewNavController(it1,navController) }
        }
        onView(withId(R.id.addReminderFAB)).perform(click())
        Mockito.verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }

}