package com.github.gmarcell.todolist.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class DoesRepository(application: Application) {

    private var doesDao: DoesDao

    private var allDoes: LiveData<List<Does>>

    init {
        val database: DoesDatabase = DoesDatabase.getInstance(
            application.applicationContext
        )!!
        doesDao = database.doesDao()
        allDoes = doesDao.getAllDoes()
    }

    fun insert(does: Does) {
        val insertDoesAsyncTask = InsertDoesAsyncTask(doesDao).execute(does)
    }

    fun update(does: Does) {
        val updateDoesAsyncTask = UpdateDoesAsyncTask(doesDao).execute(does)
    }


    fun delete(does: Does) {
        val deleteDoesAsyncTask = DeleteDoesAsyncTask(doesDao).execute(does)
    }

    fun deleteAllDoes() {
        val deleteAllDoesAsyncTask = DeleteAllDoesAsyncTask(
            doesDao
        ).execute()
    }

    fun getAllDoes(): LiveData<List<Does>> {
        return allDoes
    }

    companion object {
        private class InsertDoesAsyncTask(doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {
            val doesDao = doesDao

            override fun doInBackground(vararg p0: Does?) {
                doesDao.insert(p0[0]!!)
            }
        }

        private class UpdateDoesAsyncTask(doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {
            val doesDao = doesDao

            override fun doInBackground(vararg p0: Does?) {
                doesDao.update(p0[0]!!)
            }
        }

        private class DeleteDoesAsyncTask(doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {
            val doesDao = doesDao

            override fun doInBackground(vararg p0: Does?) {
                doesDao.delete(p0[0]!!)
            }
        }

        private class DeleteAllDoesAsyncTask(doesDao: DoesDao) : AsyncTask<Unit, Unit, Unit>() {
            val doesDao = doesDao

            override fun doInBackground(vararg p0: Unit?) {
                doesDao.deleteAllDoes()
            }
        }
    }
}