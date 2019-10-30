package com.github.gmarcell.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Does(

    var title: String,

    var description: String,

    var priority: Int,

    var duedate: String
) {
    //does it matter if these are private or not?
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}