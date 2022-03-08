package com.example.pedal.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pedal.R
import com.example.pedal.model.User

class ListAdapter(val findNavController: NavController):RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val id_txt = itemView.findViewById<TextView>(R.id.id_txt)
        val firstName_txt = itemView.findViewById<TextView>(R.id.firstName_txt)
        val lastName_txt = itemView.findViewById<TextView>(R.id.lastName_txt)
        val age_txt = itemView.findViewById<TextView>(R.id.age_txt)
        val rowlayout = itemView.findViewById<ConstraintLayout>(R.id.rowLayout)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.id_txt.text = currentItem.id.toString()
        holder.firstName_txt.text = currentItem.firstName
        holder.lastName_txt.text = currentItem.lastName
        holder.age_txt.text = currentItem.age.toString()

        holder.rowlayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            findNavController.navigate(action)
        }

    }

    fun setData(user: List<User>){
        this.userList = user
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}