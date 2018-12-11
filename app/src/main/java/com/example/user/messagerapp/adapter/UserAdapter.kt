package com.example.user.messagerapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.user.messagerapp.ChatLogActivity
import com.example.user.messagerapp.R
import com.example.user.messagerapp.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter (private val list: ArrayList<User>,
                   private val context: Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_users, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindItem(list[position])
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(user: User){

            var username : TextView = itemView.findViewById(R.id.user_name_view)
            var imageProfile : ImageView = itemView.findViewById(R.id.user_image_view)
            var emailAddress : TextView = itemView.findViewById(R.id.user_email_view)
            var circleImage = itemView.findViewById<CircleImageView>(R.id.circle_image_view)

            username.text = user.username
            emailAddress.text = user.email
            Picasso.get().load(user.imageUri).into(imageProfile)
            imageProfile.alpha = 0f
            Picasso.get().load(user.imageUri).into(circleImage)

            itemView.setOnClickListener {
                var intent = Intent(context, ChatLogActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}