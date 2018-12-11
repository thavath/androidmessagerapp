package com.example.user.messagerapp
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
//import android.widget.Toast
import com.example.user.messagerapp.adapter.UserAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    private var userList: ArrayList<User>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
//        var recyclerView = findViewById<>(R.id.preview_new_message)
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        supportActionBar?.title = "Select User"
        progressBar.visibility = View.VISIBLE
        userList = ArrayList<User>()

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var user = it.getValue(User::class.java) as User
                    if (user != null){
                        userList!!.add(user)
                    }
                }
                layoutManager = LinearLayoutManager(this@NewMessageActivity)
                adapter = UserAdapter(userList!!, this@NewMessageActivity)
                preview_new_message.layoutManager = layoutManager
                preview_new_message.adapter = adapter
                progressBar.visibility = View.INVISIBLE
                adapter!!.notifyDataSetChanged()
            }
        })
    }
}
