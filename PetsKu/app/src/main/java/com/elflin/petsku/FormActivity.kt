package com.elflin.petsku

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.elflin.petsku.databinding.ActivityFormBinding
import model.GlobalVar
import model.Hewan
import java.lang.NullPointerException
import java.lang.NumberFormatException

class FormActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityFormBinding
    private lateinit var imageArray: ByteArray
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        GetIntent()
        SetupListener()
    }

    private fun GetIntent(){
        if (intent.getIntExtra("status", 0) == GlobalVar.StatusAdd){
            viewBinding.FormTittleEdit.visibility = View.INVISIBLE
        }else if (intent.getIntExtra("status", 0) == GlobalVar.StatusEdit){
            position = intent.getIntExtra("position", -1)
            viewBinding.FormTittleTambah.visibility = View.INVISIBLE
            viewBinding.FormInputNama.editText?.setText(GlobalVar.ListDataHewan[position].NamaHewan)
            viewBinding.FormInputJenisHewan.editText?.setText(GlobalVar.ListDataHewan[position].JenisHewan)
            viewBinding.FormInputUmurHewan.editText?.setText(GlobalVar.ListDataHewan[position].umurHewan.toString())
            if(GlobalVar.ListDataHewan[position].FotoHewan != "null") {
                val bArray = GlobalVar.StringToByteArr(GlobalVar.ListDataHewan[position].FotoHewan)
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                options.inScaled = true
                val bitMap = BitmapFactory.decodeByteArray(
                    bArray,
                    0,
                    bArray.size,
                    options
                )
                viewBinding.FormPicture.setImageBitmap(bitMap)
            }
        }
    }

    private fun SetupListener(){
        viewBinding.FormPicture.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_PICK)
            myIntent.type = "image/*"
            GetResult.launch(myIntent)
        }

        viewBinding.FormBackButton.setOnClickListener {
            finish()
        }
        viewBinding.FormInputButton.setOnClickListener{
            try{
                val hewan = Hewan(
                    viewBinding.FormInputNama.editText?.text.toString().trim(),
                    viewBinding.FormInputJenisHewan.editText?.text.toString().trim(),
                    viewBinding.FormInputUmurHewan.editText?.text.toString().trim().toInt(),
                    GlobalVar.ByteArrToString(imageArray!!)
                )

                if (FormChecker(hewan)){
                    if (intent.getIntExtra("status", 0) == GlobalVar.StatusAdd){
                        GlobalVar.ListDataHewan.add(hewan)
                    } else if (intent.getIntExtra("status", 0) == GlobalVar.StatusEdit){
                        GlobalVar.ListDataHewan[position] = hewan
                    }
                    Toast.makeText(baseContext, "Data berhasil di simpan", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(baseContext, "Data gagal di simpan", Toast.LENGTH_LONG).show()
                }
            }catch (e: NumberFormatException){
                viewBinding.FormInputUmurHewan.error = "Umur hewan belum terisi"
            }catch (e: UninitializedPropertyAccessException){
                if (intent.getIntExtra("status", 0) == GlobalVar.StatusEdit){
                    val hewan = Hewan(
                        viewBinding.FormInputNama.editText?.text.toString().trim(),
                        viewBinding.FormInputJenisHewan.editText?.text.toString().trim(),
                        viewBinding.FormInputUmurHewan.editText?.text.toString().trim().toInt(),
                        GlobalVar.ListDataHewan[position].FotoHewan
                    )

                    if (FormChecker(hewan)){
                        GlobalVar.ListDataHewan[position] = hewan
                        Toast.makeText(baseContext, "Data berhasil di simpan", Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(baseContext, "Data gagal di simpan", Toast.LENGTH_LONG).show()
                    }
                }
                Toast.makeText(baseContext, "Foto Hewan belum di pilih", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun FormChecker(hewan:Hewan): Boolean {

        var isCompleted = true

        if(hewan.NamaHewan.isEmpty()){
            viewBinding.FormInputNama.error = "Nama hewan belum terisi"
            isCompleted = false
        }else{
            viewBinding.FormInputNama.error = ""
        }

        if(hewan.JenisHewan.isEmpty()){
            viewBinding.FormInputJenisHewan.error = "Jenis hewan belum terisi"
            isCompleted = false
        }else{
            viewBinding.FormInputJenisHewan.error = ""
        }

        if(hewan.umurHewan == 0){
            viewBinding.FormInputUmurHewan.error = "Umur hewan belum terisi"
            isCompleted = false
        }else{
            viewBinding.FormInputUmurHewan.error = ""
        }

        return isCompleted
    }

    private val GetResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){   // APLIKASI GALLERY SUKSES MENDAPATKAN IMAGE
            val uri = it.data?.data                 // GET PATH TO IMAGE FROM GALLEY
            viewBinding.FormPicture.setImageURI(uri)  // MENAMPILKAN DI IMAGE VIEW
            if (uri != null) {
                val inputStream = contentResolver.openInputStream(uri)
                inputStream?.buffered()?.use {
                    imageArray = it.readBytes()
                }
            }
        }
    }
}