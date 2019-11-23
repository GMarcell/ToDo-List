package com.github.gmarcell.todolist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "does_table")
data class Does(

    @ColumnInfo(name = "Title")
    var title: String,

    @ColumnInfo(name = "Description")
    var description: String,

    @ColumnInfo(name = "Time")
    var duetime: String,

    @ColumnInfo(name = "Date")
    var duedate: String
) {
    //does it matter if these are private or not?
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}