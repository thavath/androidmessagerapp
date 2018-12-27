package com.example.user.messagerapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.user.messagerapp.ChatLogActivity
import com.example.user.messagerapp.R
import com.example.user.messagerapp.model.Building
import com.squareup.picasso.Picasso

class BuildingAdapter (private val list: ArrayList<Building>,
                       private val context: Context) : RecyclerView.Adapter<BuildingAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_room_list_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindItem(list[position])
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(building: Building){

            var buildingName : TextView = itemView.findViewById(R.id.building_name)
            var imageBuilding : ImageView = itemView.findViewById(R.id.building_image)
            var buildingAddress : TextView = itemView.findViewById(R.id.building_address)

            buildingName.text = building.buildingName
            buildingAddress.text = building.address
            Picasso.get().load(building.buildingImage).into(imageBuilding)

            itemView.setOnClickListener {
                var intent = Intent(context, ChatLogActivity::class.java)
                intent.putExtra("building", building)
                context.startActivity(intent)
            }
        }
    }
}