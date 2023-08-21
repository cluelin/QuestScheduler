package com.cluelin.app

import androidx.lifecycle.ViewModel
import com.cluelin.app.db.DataManager

class SharedViewModel : ViewModel() {
    lateinit var dataManager: DataManager
    lateinit var questAdapter: QuestAdapter
}