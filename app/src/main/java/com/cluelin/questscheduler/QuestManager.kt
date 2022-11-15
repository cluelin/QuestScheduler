package com.cluelin.questscheduler

class QuestManager {

    private val dailyQuestList: MutableList<DailyQuest> = mutableListOf<DailyQuest>()
    private val weeklyQuestList: MutableList<WeeklyQuest> = ArrayList<WeeklyQuest>()
    private val monthlyQuestList: MutableList<MonthlyQuest> = ArrayList<MonthlyQuest>()

    fun addDailyQuest(name : String){
        dailyQuestList.add(DailyQuest(name))
    }

    fun addWeeklyQuest(name: String){
        weeklyQuestList.add(WeeklyQuest(name))
    }

    fun addMonthlyQuest(name: String){
        monthlyQuestList.add(MonthlyQuest(name))
    }

    fun getDailyQuestList() : List<DailyQuest>{
        return dailyQuestList
    }

}