package com.world4tech.godsdecision.model.repository

import androidx.lifecycle.LiveData
import com.world4tech.godsdecision.model.Decisions
import com.world4tech.godsdecision.model.room.DecisionsDao


class DecisionsRepository(private var decisionsDao: DecisionsDao) {
    val allDecisions:   LiveData<List<Decisions>> = decisionsDao.getAllDecisions()

    suspend fun insert(decisions: Decisions){
        decisionsDao.insert(decisions)
    }

    suspend fun delete(decisions: Decisions){
        decisionsDao.delete(decisions)
    }
}