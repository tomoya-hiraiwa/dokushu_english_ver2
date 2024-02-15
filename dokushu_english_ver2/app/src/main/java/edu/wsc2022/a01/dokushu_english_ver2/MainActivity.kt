package edu.wsc2022.a01.dokushu_english_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.wsc2022.a01.dokushu_english_ver2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.startButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}