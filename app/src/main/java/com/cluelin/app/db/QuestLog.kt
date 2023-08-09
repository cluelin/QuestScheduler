package com.cluelin.app.db

data class QuestLog(val id: Int, val questID: Int, val completedTime: String) {
    constructor(questID: Int, completedTime: String) : this(-1, questID, completedTime)

}