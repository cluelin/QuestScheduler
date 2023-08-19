package com.cluelin.app.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cluelin.app.MyAdapter
import com.cluelin.app.R
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuestList.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestList : Fragment(), OnItemClickListener, OnItemLongClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_quest_list, container, false)

        dataManager = DataManager(requireContext())
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val questList = dataManager.getAllUnDoneQuests()

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

//        TODO
//        adpter 생성 위치를 activity 로 옮겨서 모든 프래그 먼트에서 사용할수잇게 만들고싶음.
//        아니면 하다못해 Add 프래그먼트에서 item추가 할수잇게끔 변경.
        val adapter = MyAdapter(questList as MutableList<Quest>, this)
        recyclerView.adapter = adapter

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



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuestList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


interface OnItemClickListener {
    fun onItemClick(quest: Quest)
}

interface OnItemLongClickListener {
    fun onItemLongClicked(quest: Quest)
}