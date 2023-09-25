package com.alpesh1.imageanddatafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Item_List : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemlist : ArrayList<ItemDS>
    private var nodeList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        recyclerView = findViewById(R.id.rcvList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        itemlist = arrayListOf<ItemDS>()

        getItemData()

    }

    private fun getItemData() {

        reference = FirebaseDatabase.getInstance().getReference("items")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (list in snapshot.children){
                        val data = list.getValue(ItemDS::class.java)
                        itemlist.add(data!!)
                        nodeList.add(list.key.toString())
                    }
                    val adapter = ItemAdapter(itemlist)
                    recyclerView.adapter = adapter

                    adapter.setOnClickListener(object :ItemAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val nodePath : String = nodeList[position]
                            val intent = Intent()
                            intent.putExtra("item_id",nodePath)
                            setResult(2,intent)
                            finish()

                        }

                        override fun onItemClick(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            TODO("Not yet implemented")
                        }

                    })
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}