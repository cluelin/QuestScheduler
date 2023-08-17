package com.cluelin.app.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cluelin.app.R
import com.cluelin.app.db.DataManager
import com.cluelin.app.db.Quest


class QuestGenerator : Fragment() {
    val TAG = "injae.jang"

    lateinit var dataManager: DataManager
    lateinit var etQuestTitle: EditText
    lateinit var etQuestTerm: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var addFragment = inflater.inflate(R.layout.fragment_quest_add, container, false)
        dataManager = DataManager(requireContext())

        val btnAdd : Button = addFragment.findViewById(R.id.btn_add_quest)
        etQuestTitle = addFragment.findViewById(R.id.quest_name)
        etQuestTerm = addFragment.findViewById(R.id.quest_term)

        btnAdd.setOnClickListener {
            this.insertQuest()
            Toast.makeText(requireContext(), "New Quest Created", Toast.LENGTH_SHORT).show() // 토스트 말고 다른 방식으로 표현하는게 좋을듯.
        }

        return addFragment
    }


    fun insertQuest(){
        val questTitle = etQuestTitle.text.toString()
        val questTerm = Integer.parseInt(etQuestTerm.text.toString())

        val quest = Quest(questTitle, questTerm)
        dataManager.insertQuest(quest)

        etQuestTitle.setText("")
        etQuestTerm.setText("")
        Log.i(TAG, "Invoked insertQuest(), title ${questTitle}, term ${questTerm}")
    }


}