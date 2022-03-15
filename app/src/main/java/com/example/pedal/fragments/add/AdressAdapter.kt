package com.example.pedal.fragments.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pedal.R
import com.example.pedal.fragments.list.ListAdapter
import com.example.pedal.model.Adress
import com.example.pedal.viewmodel.UserViewModel

class AdressAdapter(val vm : UserViewModel): RecyclerView.Adapter<AdressAdapter.MyViewHolder>() {

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
            vm.adressSelected(currentItem.infoadress)
            vm.latlong(currentItem.lat,currentItem.long)
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