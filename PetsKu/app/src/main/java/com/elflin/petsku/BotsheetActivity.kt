package com.elflin.petsku

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class BotsheetActivity : AppCompatActivity() {

    var sheetBehavior: BottomSheetBehavior<*>? = null
    var sheetDialog: BottomSheetDialog? = null
    var bottom_sheet: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_botsheet)
        bottom_sheet = findViewById<CardView>(R.id.standard_bottom_sheet)

        val button: Button = findViewById(R.id.OpenButton)
        button.setOnClickListener(View.OnClickListener { showBottomSheetDialog() })
    }

    private fun showBottomSheetDialog() {
        val view: View = layoutInflater.inflate(R.layout.sheet_content, null)
        if (sheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        view.findViewById<View>(R.id.close_button).setOnClickListener { sheetDialog!!.dismiss() }

        sheetDialog = BottomSheetDialog(this)
        sheetDialog!!.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        sheetDialog!!.show()
        sheetDialog!!.setOnDismissListener { sheetDialog = null }
    }

}
