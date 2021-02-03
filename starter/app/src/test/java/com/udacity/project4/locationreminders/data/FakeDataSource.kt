package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource

// Pin -> Get this from Android-testing app course implementation
class FakeDataSource(var reminders: MutableList<ReminderDTO> = mutableListOf()) : ReminderDataSource {

//    : Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        //("Return the reminders")
       return Result.Success(reminders.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        // ("save the reminder")
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        //("return the reminder with the id")
        reminders.forEach {
            if(id == it.id) {
                return Result.Success(it)
            }
        }
        return Result.Error("Error: reminder not found from getReminder (FakeDataSource).")
    }

    override suspend fun deleteAllReminders() {
        //("delete all the reminders")
        reminders.clear()
    }


}