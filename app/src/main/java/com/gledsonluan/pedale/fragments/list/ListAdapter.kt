package com.gledsonluan.pedale.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gledsonluan.pedale.R
import com.gledsonluan.pedale.model.Tour

class ListAdapter(val findNavController: NavController):RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<Tour>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val firstName_txt = itemView.findViewById<TextView>(R.id.title)
        val lastName_txt = itemView.findViewById<TextView>(R.id.start_point)
        val age_txt = itemView.findViewById<TextView>(R.id.date)
        val distance = itemView.findViewById<TextView>(R.id.distance)
        val rowlayout = itemView.findViewById<CardView>(R.id.rowLayout)
        val img = itemView.findViewById<ImageView>(R.id.background_item)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.distance.text = currentItem.distance
        holder.firstName_txt.text = currentItem.name
        holder.lastName_txt.text = currentItem.description
        holder.age_txt.text = currentItem.date

        if (currentItem.type == "Urbano"){
            holder.img.load(R.drawable.urbano){
                crossfade(true)
                crossfade(400)
            }
        }else{
            holder.img.load(R.drawable.trilha){
                crossfade(true)
                crossfade(400)
            }
        }

        holder.rowlayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToReadFragment(
                currentItem
            )
            findNavController.navigate(action)
        }

    }

    fun setData(Tour: List<Tour>){
        this.userList = Tour
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}