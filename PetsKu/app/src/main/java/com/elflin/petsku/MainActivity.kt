package com.elflin.petsku

import adapter.HewanRVAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.elflin.petsku.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import listener.CardAnimalListener
import model.GlobalVar

class MainActivity : AppCompatActivity(), CardAnimalListener {

    private lateinit var viewBind: ActivityMainBinding
    private val RVAdapter = HewanRVAdapter(GlobalVar.ListDataHewan, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBind.root)
        SetupRecyclerView()
        SetupListener()

        var soundBtn=findViewById(R.id.soundBtn) as ImageView
        soundBtn.setOnClickListener {
            Toast.makeText(this, "pock", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        if (GlobalVar.ListDataHewan.size > 0){
            viewBind.MainRecyclerView.visibility = View.VISIBLE
            viewBind.MainNoData.visibility = View.GONE
        }else{
            viewBind.MainRecyclerView.visibility = View.GONE
            viewBind.MainNoData.visibility = View.VISIBLE
        }
        RVAdapter.notifyDataSetChanged()
    }

    private fun SetupRecyclerView(){
        val layoutManager = LinearLayoutManager(baseContext)
        viewBind.MainRecyclerView.layoutManager = layoutManager
        viewBind.MainRecyclerView.adapter = RVAdapter
    }

    private fun SetupListener(){
        viewBind.MainFAB.setOnClickListener{
            val intent = Intent(baseContext, FormActivity::class.java).apply {
                putExtra("status", GlobalVar.StatusAdd)
            }
            startActivity(intent)
        }
    }



    override fun OnEditClicked(position: Int) {
        val intent = Intent(baseContext, FormActivity::class.java).apply {
            putExtra("status", GlobalVar.StatusEdit)
            putExtra("position", position)
        }
        startActivity(intent)
    }

    override fun OnDeleteClicked(position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Hapus Hewan")
            .setMessage("Apakah anda ingin menghapus hewan ini?")
            .setNegativeButton("Batal") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Setuju") { dialog, which ->
                GlobalVar.ListDataHewan.removeAt(position)
                Toast.makeText(baseContext, "Data berhasil di hapus", Toast.LENGTH_LONG).show()
                onResume()
            }
            .show()
    }


}