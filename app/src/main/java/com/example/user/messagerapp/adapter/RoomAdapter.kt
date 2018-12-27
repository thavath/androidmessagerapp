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
import com.example.user.messagerapp.NewMessageActivity
import com.example.user.messagerapp.R
import com.example.user.messagerapp.User
import com.example.user.messagerapp.model.Room
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class RoomAdapter (private val list: ArrayList<Room>,
                   private val context: Context) : RecyclerView.Adapter<RoomAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_room_list_roomview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindItem(list[position])
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(room: Room){

            var priceSize : TextView = itemView.findViewById(R.id.room_price_size_view)
            var imageProfile : ImageView = itemView.findViewById(R.id.room_image_view)
            var roomDescriptionView : TextView = itemView.findViewById(R.id.room_description_view)

            priceSize.text = "Price : " + room.price.toString() + "$, Size : " + room.roomSize
            roomDescriptionView.text = room.roomDescription
            Picasso.get().load(room.roomImageUri).into(imageProfile)

            itemView.setOnClickListener {
                var intent = Intent(context, ChatLogActivity::class.java)
                intent.putExtra("room", room)
                context.startActivity(intent)
            }
        }
    }
}