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
        InsertDoesAsyncTask(doesDao).execute(does)
    }

    fun update(does: Does) {
        UpdateDoesAsyncTask(doesDao).execute(does)
    }


    fun delete(does: Does) {
        DeleteDoesAsyncTask(doesDao).execute(does)
    }

    fun getAllDoes(): LiveData<List<Does>> {
        return allDoes
    }

    companion object {
        private class InsertDoesAsyncTask(val doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {

            override fun doInBackground(vararg p0: Does?) {
                doesDao.insert(p0[0]!!)
            }
        }

        private class UpdateDoesAsyncTask(val doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {

            override fun doInBackground(vararg p0: Does?) {
                doesDao.update(p0[0]!!)
            }
        }

        private class DeleteDoesAsyncTask(val doesDao: DoesDao) : AsyncTask<Does, Unit, Unit>() {

            override fun doInBackground(vararg p0: Does?) {
                doesDao.delete(p0[0]!!)
            }
        }
    }
}