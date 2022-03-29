package com.gledsonluan.pedale.fragments.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gledsonluan.pedale.R
import com.gledsonluan.pedale.model.Adress
import com.gledsonluan.pedale.viewmodel.UserViewModel

class AdressAdapter(val vm : UserViewModel, val end: Boolean): RecyclerView.Adapter<AdressAdapter.MyViewHolder>() {

    private var userList = emptyList<Adress>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val adress = itemView.findViewById<TextView>(R.id.name_adress)
        val info = itemView.findViewById<TextView>(R.id.info_adress)
        val rowlayout = itemView.findViewById<RelativeLayout>(R.id.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adress_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.adress.text = currentItem.nameadress
        holder.info.text = currentItem.infoadress

        holder.rowlayout.setOnClickListener {
            if(end){
                vm.endPointSelected(
                    currentItem.nameadress + " " + currentItem.infoadress,
                    currentItem.lat,
                    currentItem.long)
            } else {
                vm.startPointSelected(
                    currentItem.nameadress + " " + currentItem.infoadress,
                    currentItem.lat,
                    currentItem.long)
            }
        }
    }

    fun setData(user: List<Adress>){
        this.userList = user
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}