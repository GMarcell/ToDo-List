package com.github.gmarcell.todolist.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.gmarcell.todolist.data.DoesRepository
import com.github.gmarcell.todolist.data.Does

class DoesViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DoesRepository =
        DoesRepository(application)
    private var allNotes: LiveData<List<Does>> = repository.getAllNotes()

    fun insert(does: Does) {
        repository.insert(does)
    }

    fun update(does: Does) {
        repository.update(does)
    }

    fun delete(does: Does) {
        repository.delete(does)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Does>> {
        return allNotes
    }
}