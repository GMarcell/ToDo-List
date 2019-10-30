package com.github.gmarcell.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DoesDao {

    @Insert
    fun insert(does: Does)

    @Update
    fun update(does: Does)

    @Delete
    fun delete(does: Does)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotes(): LiveData<List<Does>>

}