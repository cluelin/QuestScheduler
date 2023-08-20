package com.cluelin.app.db

import android.content.ContentValues
import android.content.Context
import android.util.Log

class DataManager(private val context: Context) {

    private val databaseHelper = DatabaseHelper(context)

    fun insertQuest(quest: Quest) {
        databaseHelper.insertQuest(quest)
    }


    fun getQuestLogListByTitle(questTitle: String): List<QuestLog> {
        return databaseHelper.getQuestLogListByTitle(questTitle)
    }

    fun getQuestLogListByTitleAndDays(questTitle: String, days : Int): List<QuestLog> {
        return databaseHelper.getQuestLogListByTitleAndDays(questTitle, days)
    }

    fun getNoteById(id: Int): Quest? {
        return databaseHelper.getQuestById(id)
    }

    fun getAllQuests(): List<Quest> {
        return databaseHelper.getAllQuests()
    }

    fun getAllUnDoneQuests(): List<Quest> {
        Log.d("", "invoked getAllUnDoneQuests()")
        return databaseHelper.getAllUnDoneQuests()
    }


    fun update(quest: Quest) {
        databaseHelper.updateQuest(quest)
    }

    fun updateTerm(questTitle: String, term: Int){
        databaseHelper.updateTerm(questTitle, term)
    }


    fun increaseTerm(questTitle: String) {
        databaseHelper.updateTerm(questTitle, 1)
    }

    fun decreaseTerm(questTitle: String) {
        databaseHelper.updateTerm(questTitle, -1)
    }


    fun updateCompletedTime(questTitle: String, completedTime: String) {
        databaseHelper.updateCompletedTime(questTitle, completedTime)
    }

    fun removeAllRows() {
        databaseHelper.removeAllRows()
    }

}
