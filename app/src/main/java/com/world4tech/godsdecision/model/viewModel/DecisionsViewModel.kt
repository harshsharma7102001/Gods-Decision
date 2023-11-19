package com.world4tech.godsdecision.model.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.world4tech.godsdecision.model.Decisions
import com.world4tech.godsdecision.model.repository.DecisionsRepository
import com.world4tech.godsdecision.model.room.DecisionsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DecisionsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: DecisionsRepository
    val allDecision: LiveData<List<Decisions>>
    init {
        val dao = DecisionsDatabase.getDatabase(application).getDecisionsDao()
        repository = DecisionsRepository(dao)
        allDecision = repository.allDecisions
    }
    fun delDecisions(decisions: Decisions) = viewModelScope.launch (Dispatchers.IO){
        repository.delete(decisions)
    }
    fun addDecisions(decisions: Decisions) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(decisions)
    }
}