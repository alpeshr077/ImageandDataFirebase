package com.alpesh1.imageanddatafirebase

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alpesh1.imageanddatafirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    var sImage: String? = ""
    var nodeId = ""
    private lateinit var reference: DatabaseReference
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun insert_data(view: View) {

        val itemName = binding.edtItemName.text.toString()
        val itemRate = binding.edtItemRate.text.toString()
        val itemUnit = binding.edtItemUnit.text.toString()

        reference = FirebaseDatabase.getInstance().getReference("items")

        if (itemName.isEmpty()) {
            Toast.makeText(this, "insert name", Toast.LENGTH_SHORT).show()
        } else if (itemRate.isEmpty()) {
            Toast.makeText(this, "insert rate", Toast.LENGTH_SHORT).show()
        } else if (itemUnit.isEmpty()) {
            Toast.makeText(this, "insert unit", Toast.LENGTH_SHORT).show()
        } else {

            val item = ItemDS(itemName, itemRate, itemUnit, sImage)
            val databaseReference = FirebaseDatabase.getInstance().reference
            val id = databaseReference.push().key
            reference.child(id.toString()).setValue(item).addOnSuccessListener {

                binding.edtItemName.text.clear()
                binding.edtItemRate.text.clear()
                binding.edtItemUnit.text.clear()
                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun insert_Image(view: View) {

        var myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)

    }

    private val ActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == RESULT_OK) {
            val uri = result.data!!.data
            try {

                val inputStreem = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStreem)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
                binding.imgImage.setImageBitmap(myBitmap)
                inputStreem!!.close()
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()

            } catch (ex: Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private val itemResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == 2) {
           var intent = result.data
            if (intent != null){
                nodeId = intent.getStringExtra("item_id").toString()
            }
            reference = FirebaseDatabase.getInstance().getReference("items")
            reference.child(nodeId).get().addOnSuccessListener {

                if (it.exists()){

                    binding.edtItemName.setText(it.child("itemName").value.toString())
                    binding.edtItemRate.setText(it.child("itemRate").value.toString())
                    binding.edtItemUnit.setText(it.child("itemUnit").value.toString())
                    sImage = it.child("itemImage").value.toString()
                    val bytes = Base64.decode(sImage,Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                    binding.imgImage.setImageBitmap(bitmap)

                    binding.btnUpdateData.visibility = View.VISIBLE
                    binding.btnDeleteData.visibility = View.VISIBLE
                    binding.btnInsertData.visibility = View.INVISIBLE

                }else{
                    Toast.makeText(this, "item not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun Show_list(view: View) {

        var intent = Intent(this,Item_List::class.java)
        itemResultLauncher.launch(intent)

    }

    fun update_data(view: View) {

        val itemName = binding.edtItemName.text.toString()
        val itemRate = binding.edtItemRate.text.toString()
        val itemUnit = binding.edtItemUnit.text.toString()

        reference = FirebaseDatabase.getInstance().getReference("items")

        if (itemName.isEmpty()) {
            Toast.makeText(this, "insert name", Toast.LENGTH_SHORT).show()
        } else if (itemRate.isEmpty()) {
            Toast.makeText(this, "insert rate", Toast.LENGTH_SHORT).show()
        } else if (itemUnit.isEmpty()) {
            Toast.makeText(this, "insert unit", Toast.LENGTH_SHORT).show()
        } else {

            val item = ItemDS(itemName, itemRate, itemUnit, sImage)

            reference.child(nodeId).setValue(item).addOnSuccessListener {

                binding.edtItemName.text.clear()
                binding.edtItemRate.text.clear()
                binding.edtItemUnit.text.clear()
                sImage = ""

                binding.btnUpdateData.visibility = View.INVISIBLE
                binding.btnDeleteData.visibility = View.INVISIBLE
                binding.btnInsertData.visibility = View.VISIBLE

                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT).show()
            }

        }

    }
    fun delete_list(view: View) {

        reference = FirebaseDatabase.getInstance().getReference("items")
        reference.child(nodeId).removeValue()

        Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show()

    }

}