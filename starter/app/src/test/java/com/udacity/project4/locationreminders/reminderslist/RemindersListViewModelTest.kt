package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //: provide testing to the RemindersListViewModel and its live data objects

    private val reminder = ReminderDTO(
            "title",
            "description",
            "location",
            00.00,
            00.00
    )

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remindersListViewModel: RemindersListViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val fakeDataSource = FakeDataSource()
    @Before
    fun setupSaveReminderViewModel() {
        remindersListViewModel = RemindersListViewModel(
                ApplicationProvider.getApplicationContext(),
                fakeDataSource
        )
    }

    @Test
    fun notEmptyReturnSuccess() = mainCoroutineScopeRule.runBlockingTest {
        fakeDataSource.deleteAllReminders()
        fakeDataSource.saveReminder(reminder)
        remindersListViewModel.loadReminders()

        val remindersList = remindersListViewModel.remindersList.getOrAwaitValue()

        assertThat(remindersList.size, `is`(1))
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineScopeRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
        TestWatcher(),
        TestCoroutineScope by TestCoroutineScope(dispatcher) {
    override fun starting(description: org.junit.runner.Description?) {
        super.starting(description)
        // If your codebase allows the injection of other dispatchers like
        // Dispatchers.Default and Dispatchers.IO, consider injecting all of them here
        // and renaming this class to `CoroutineScopeRule`
        //
        // All injected dispatchers in a test should point to a single instance of
        // TestCoroutineDispatcher.
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: org.junit.runner.Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}