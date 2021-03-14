package com.example.movieapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.movieapi.mvvm.MvvmActivity
import com.example.movieapi.mvvm.single_movie_details.SingleMovie

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMVVM = findViewById<Button>(R.id.btnMVVM)
        btnMVVM.setOnClickListener{
            val intent = Intent(this,MvvmActivity::class.java)
//            intent.putExtra("id",587807)
            startActivity(intent)
        }
    }
}