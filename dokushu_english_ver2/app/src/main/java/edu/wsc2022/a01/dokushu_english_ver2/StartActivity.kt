package edu.wsc2022.a01.dokushu_english_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.wsc2022.a01.dokushu_english_ver2.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var b: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityStartBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.q1Button.setOnClickListener {
            startActivity(Intent(this,QuizActivity::class.java).apply {
                putExtra("question",1)
            })
        }
        b.q2Button.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java).apply {
                putExtra("question",2)
            })
        }
    }
}