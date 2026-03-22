package com.example.utconnect.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.utconnect.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LikesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)
        
        val bottomSheet = findViewById<androidx.cardview.widget.CardView>(R.id.bottom_sheet_likes)
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
