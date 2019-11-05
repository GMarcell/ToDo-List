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

    @Query("DELETE FROM does_table")
    fun deleteAllDoes()

    @Query("SELECT * FROM does_table ORDER BY priority DESC")
    fun getAllDoes(): LiveData<List<Does>>

}