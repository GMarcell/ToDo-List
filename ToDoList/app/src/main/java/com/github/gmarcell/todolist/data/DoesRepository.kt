package com.github.gmarcell.todolist.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class DoesRepository(application: Application) {

    private var doesDao: DoesDao

    private var allNotes: LiveData<List<Does>>

    init {
        val database: DoesDatabase = DoesDatabase.getInstance(
            application.applicationContext
        )!!
        doesDao = database.noteDao()
        allNotes = doesDao.getAllNotes()
    }

    fun insert(does: Does) {
        val insertNoteAsyncTask = InsertNoteAsyncTask(doesDao).execute(does)
    }

    fun update(does: Does) {
        val updateNoteAsyncTask = UpdateNoteAsyncTask(doesDao).execute(does)
    }


    fun delete(does: Does) {
        val deleteNoteAsyncTask = DeleteNoteAsyncTask(doesDao).execute(does)
    }

    fun deleteAllNotes() {
        val deleteAllNotesAsyncTask = DeleteAllNotesAsyncTask(
            doesDao
        ).execute()
    }

    fun getAllNotes(): LiveData<List<Does>> {
        return allNotes
    }

    companion object {
        private class InsertNoteAsyncTask(doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {
            val noteDao = doesDao

            override fun doInBackground(vararg p0: Does?) {
                noteDao.insert(p0[0]!!)
            }
        }

        private class UpdateNoteAsyncTask(doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {
            val noteDao = doesDao

            override fun doInBackground(vararg p0: Does?) {
                noteDao.update(p0[0]!!)
            }
        }

        private class DeleteNoteAsyncTask(doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {
            val noteDao = doesDao

            override fun doInBackground(vararg p0: Does?) {
                noteDao.delete(p0[0]!!)
            }
        }

        private class DeleteAllNotesAsyncTask(doesDao: DoesDao) : AsyncTask<Unit, Unit, Unit>() {
            val noteDao = doesDao

            override fun doInBackground(vararg p0: Unit?) {
                noteDao.deleteAllNotes()
            }
        }
    }
}