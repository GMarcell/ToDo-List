package com.github.gmarcell.todolist.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Does::class], version = 1)
abstract class DoesDatabase : RoomDatabase() {

    abstract fun doesDao(): DoesDao


    companion object {
        private var instance: DoesDatabase? = null

        fun getInstance(context: Context): DoesDatabase? {
            if (instance == null) {
                synchronized(DoesDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DoesDatabase::class.java, "does_database"
                    )
                        .fallbackToDestructiveMigration() // when version increments, it migrates (deletes db and creates new) - else it crashes
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }
    }

    class PopulateDbAsyncTask(db: DoesDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val doesDao = db?.doesDao()

        override fun doInBackground(vararg p0: Unit?) {
            doesDao?.insert(Does("title 1", "description 1", "13.00", "10 Oct 2019"))
            doesDao?.insert(Does("title 2", "description 2", "17.00", "13 Oct 2019"))
            doesDao?.insert(Does("title 3", "description 3", "18.00", "21 Oct 2019"))
        }
    }

}