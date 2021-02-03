package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    : Add testing implementation to the RemindersDao.kt

    private lateinit var database: RemindersDatabase
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
                getApplicationContext(),
                RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertReminderAndGetById() = runBlockingTest {
        val reminder = ReminderDTO(
                "title",
                "description",
                "location",
                0.0,
                0.0)

        database.reminderDao().saveReminder(reminder)

        val reminderFromDb = database.reminderDao().getReminderById(reminder.id)
        assertThat(reminderFromDb, not(nullValue()))
        assertThat(reminderFromDb?.title, `is`(reminder.title))
        assertThat(reminderFromDb?.description, `is`(reminder.description))
        assertThat(reminderFromDb?.location, `is`(reminder.location))
        assertThat(reminderFromDb?.latitude, `is`(reminder.latitude))
        assertThat(reminderFromDb?.longitude, `is`(reminder.longitude))
    }

    @Test
    fun insertReminderAndGetIt() = runBlockingTest {
        val reminder = ReminderDTO(
                "title",
                "description",
                "location",
                0.0,
                0.0)

        database.reminderDao().saveReminder(reminder)

        val reminders = database.reminderDao().getReminders()

        assertThat(reminders, not(nullValue()))
        assertThat(reminders.size, `is`(1))
        assertThat(reminders[0], `is`(reminder))
    }
}