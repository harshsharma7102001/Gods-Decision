package com.world4tech.godsdecision.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.world4tech.godsdecision.model.Decisions

@Database(entities = arrayOf(Decisions::class), version = 1, exportSchema = false)
abstract class DecisionsDatabase :RoomDatabase(){
    abstract fun getDecisionsDao():DecisionsDao
    //singleton object to accessing the instance so that it could be accessed only ones
    companion object{
        @Volatile //so that all the operations could be accessed by all the threads
        private var INSTANCE:DecisionsDatabase?=null
        fun getDatabase(context:Context):DecisionsDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DecisionsDatabase::class.java,
                    "decision_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}