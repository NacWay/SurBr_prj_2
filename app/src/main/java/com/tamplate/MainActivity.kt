package com.tamplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn : ImageButton = findViewById(R.id.startBtn)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        startBtn.setOnClickListener {
            startBtn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed(        //немного замедляем поток чтобы показать  анимацию
                {
                    startActivity(Intent(applicationContext, GameActivity::class.java))
                    overridePendingTransition(
                        R.anim.change_acrivity2,
                        R.anim.change_acrivity1)
                        finish()
                },
                800 // value in milliseconds
            )

        }

    }
}