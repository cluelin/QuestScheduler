package com.cluelin.app.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cluelin.app.MyAdapter
import com.cluelin.app.R
import com.cluelin.app.SharedViewModel
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QuestList : Fragment(), OnItemClickListener, OnItemLongClickListener {
    private lateinit var dataManager: DataManager
    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        dataManager = sharedViewModel.dataManager


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val questList = dataManager.getAllUnDoneQuests()

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

//        TODO
//        adpter 생성 위치를 activity 로 옮겨서 모든 프래그 먼트에서 사용할수잇게 만들고싶음.
//        아니면 하다못해 Add 프래그먼트에서 item추가 할수잇게끔 변경.
        val adapter = MyAdapter(questList as MutableList<Quest>, this)
        recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_quest_list, container, false)


        return view
    }


    override fun onItemClick(quest: Quest) {
        this.updateQuestProcessedTime(quest)
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
        val formattedDateTime: String = currentDateTime.format(formatter)

        dataManager.updateCompletedTime(quest.title, formattedDateTime)
    }

}


interface OnItemClickListener {
    fun onItemClick(quest: Quest)
}

interface OnItemLongClickListener {
    fun onItemLongClicked(quest: Quest)
}