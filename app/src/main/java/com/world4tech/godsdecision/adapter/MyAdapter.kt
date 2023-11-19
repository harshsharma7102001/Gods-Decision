package com.world4tech.godsdecision.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.world4tech.godsdecision.R
import com.world4tech.godsdecision.model.Decisions

class MyAdapter: RecyclerView.Adapter<MyAdapter.myViewHolder>() {
    private var decisionsList = ArrayList<Decisions>()
    class myViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val date: TextView = itemView.findViewById(R.id.date)
        val decision:TextView = itemView.findViewById(R.id.decision)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.decision_blueprint,parent,false)
        return myViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentItem = decisionsList[position]
        holder.date.text = currentItem.date
        holder.decision.text = currentItem.text
    }

    override fun getItemCount(): Int {
        return decisionsList.size
    }

    fun getTaskAt(position: Int): Decisions {
        return decisionsList.get(position)
    }

    fun setData(decisions: List<Decisions>){
        this.decisionsList.addAll(decisions)
        notifyDataSetChanged()
    }
}