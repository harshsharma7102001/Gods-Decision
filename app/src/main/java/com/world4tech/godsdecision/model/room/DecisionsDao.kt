package com.world4tech.godsdecision.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.world4tech.godsdecision.model.Decisions


@Dao
interface DecisionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(decision: Decisions)

    @Delete
    suspend fun delete(decision: Decisions)

    @Query("SELECT * FROM decisions_table ORDER BY id DESC")
    fun getAllDecisions(): LiveData<List<Decisions>>
}