package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    : Add testing implementation to the RemindersLocalRepository.kt
    private lateinit var localDataSource: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource =
            RemindersLocalRepository(
                    database.reminderDao(),
                    Dispatchers.Main
            )
    }

    @Test
    fun saveAndRetrieveAReminder() = runBlocking {
        val reminder = ReminderDTO(
                "title",
                "description",
                "location",
                0.0,
                0.0)

        localDataSource.saveReminder(reminder)

        val reminderLocalDTO = localDataSource.getReminder(reminder.id)

        assertThat(reminderLocalDTO, not(nullValue()))
        reminderLocalDTO as Result.Success
        assertThat(reminderLocalDTO.data.title, `is`(reminder.title))
        assertThat(reminderLocalDTO.data.description, `is`(reminder.description))
        assertThat(reminderLocalDTO.data.location, `is`(reminder.location))
        assertThat(reminderLocalDTO.data.latitude, `is`(reminder.latitude))
        assertThat(reminderLocalDTO.data.longitude, `is`(reminder.longitude))
    }

    @After
    fun cleanUp() {
        database.close()
    }
}