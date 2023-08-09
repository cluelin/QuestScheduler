package com.cluelin.app.db

import android.content.ContentValues
import android.content.Context

class DataManager(private val context: Context) {

    private val databaseHelper = DatabaseHelper(context)

    fun insertQuest(quest: Quest) {
        databaseHelper.insertQuest(quest)
    }

    fun update(quest: Quest){
        databaseHelper.updateQuest(quest)
    }

    fun getQuestLogListByTitle(questTitle: String) : List<QuestLog>{
        return databaseHelper.getQuestLogListByTitle(questTitle)
    }

    fun updateCompletedTime(questTitle:String, completedTime: String){
        databaseHelper.updateCompletedTime(questTitle , completedTime)
    }

    fun getNoteById(id: Int): Quest? {
        return databaseHelper.getQuestById(id)
    }

    fun getAllQuests(): List<Quest> {
        return databaseHelper.getAllQuests()
    }

    fun getAllUnDoneQuests(): List<Quest> {
        return databaseHelper.getAllUnDoneQuests()
    }

    fun removeAllRows() {
        databaseHelper.removeAllRows()
    }

}
