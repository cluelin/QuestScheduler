package com.cluelin.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cluelin.app.Fragment.OnItemClickListener
import com.cluelin.app.db.Quest
import com.cluelin.app.utils.Common.getDaysFromToday

class QuestAdapter(
    var questList: List<Quest>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<QuestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val remainDays: TextView = itemView.findViewById(R.id.remainDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quest = questList[position]
        holder.textView.text = quest.title
        //TODO
        // remain days에 이미지나 색상강조 표시, 파이어!! 같은 느낌으로다가.
        holder.remainDays.text = (quest.term - getDaysFromToday(quest.lastCompletedTime)).toString()


        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(quest)
//            questList.removeAt(position)
            this.notifyDataSetChanged()
        }

        holder.itemView.setOnLongClickListener {
            showPopupWindow(holder.itemView, quest)
            true
        }
    }

    override fun getItemCount(): Int {
        return questList.size
    }


    private fun showPopupWindow(anchorView: View, quest: Quest) {
        val popupView = LayoutInflater.from(anchorView.context).inflate(R.layout.item_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set up dismiss listener to close the popup when clicked outside
        popupWindow.setOnDismissListener {
            // Perform any cleanup if needed
        }
        // Calculate the desired x and y coordinates for centering the popup
//        val xOffset = (anchorView.width - popupView.width) / 2
//        val yOffset = (anchorView.height - popupView.height) / 2
//
//        popupWindow.showAtLocation(anchorView, Gravity.CENTER, xOffset, yOffset)

        // Show the popup window below the anchor view
        popupWindow.showAsDropDown(anchorView)
    }
}

