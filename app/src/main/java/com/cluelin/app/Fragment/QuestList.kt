package com.cluelin.app.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cluelin.app.QuestAdapter
import com.cluelin.app.R
import com.cluelin.app.SharedViewModel
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QuestList : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var dataManager: DataManager
    private lateinit var questAdapter: QuestAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        dataManager = sharedViewModel.dataManager
        questAdapter = sharedViewModel.questAdapter
//        insertTestItems()
//        insertQuestLog()

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = questAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_quest_list, container, false)


        return view
    }

    fun insertTestItems() {
        dataManager.removeAllRows()
        dataManager.insertQuest(Quest("Doing Something Great", 1))
        dataManager.insertQuest(Quest("보라카이", 3))
        dataManager.insertQuest(Quest("영양제 먹기", 1))
        dataManager.insertQuest(Quest("청소기,건조기 먼지 정리", 14))
        dataManager.insertQuest(Quest("PCSE", 1))
        dataManager.insertQuest(Quest("식기세척기 쓰레기 버리기", 30))
    }


    fun insertQuestLog(){
        dataManager.getQuestByTitle("PCSE")?.let{
            val questId = it.id
            dataManager.insertQuestLog(questId, "2023-08-18 09:49:00")
            dataManager.insertQuestLog(questId, "2023-08-19 09:49:00")
            dataManager.insertQuestLog(questId, "2023-08-20 09:49:00")
        }
    }


    override fun onItemClick(quest: Quest) {
        this.updateQuestProcessedTime(quest)
        questAdapter.questList = dataManager.getAllUnDoneQuests()
    }

    override fun onItemLongClicked(quest: Quest) {
//        UI popup 창생성
//        로직 term 조절 기능 구현.
//        dataManager.updateTerm(quest.title, )
    }


    fun handleQuestTerm(questTitle: String, term: Int) {
        dataManager.increaseTerm(questTitle)
    }

    private fun updateQuestProcessedTime(quest: Quest) {
        val currentDateTime: LocalDateTime = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val completedDateTime: String = currentDateTime.format(formatter)

        dataManager.updateCompletedTime(quest.title, completedDateTime)
    }

}


interface OnItemClickListener {
    fun onItemClick(quest: Quest)
}

interface OnItemLongClickListener {
    fun onItemLongClicked(quest: Quest)
}