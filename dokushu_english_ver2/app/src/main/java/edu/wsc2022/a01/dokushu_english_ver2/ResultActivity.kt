package edu.wsc2022.a01.dokushu_english_ver2

import android.content.Context
import android.content.Intent
import android.icu.text.MessagePattern.ApostropheMode
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StatFs
import androidx.lifecycle.lifecycleScope
import edu.wsc2022.a01.dokushu_english_ver2.databinding.ActivityResultBinding
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var b: ActivityResultBinding
    private var imageList = listOf(R.drawable.p0,R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.pr5,R.drawable.pr6,R.drawable.pr7,R.drawable.pr8,R.drawable.pr9,R.drawable.pr10)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityResultBinding.inflate(layoutInflater)
        setContentView(b.root)
        val pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        val id = pref.getInt("userid",-1)
        val number = intent.getIntExtra("number",0)
        val pernum = intent.getIntExtra("pernum",0)
        val time = intent.getIntExtra("time",0)
        val correct = intent.getIntExtra("correct",0)
        val heart = intent.getIntExtra("heart",0)
        b.heartCount.text = heart.toString()
        b.progressText.text = "${pernum + 10}%"
        b.progressImage.setImageResource(imageList[number])
        val correctPer = (correct * 100)/10
        b.score.text = "${correctPer}%"
        b.endButton.setOnClickListener {
            startActivity(Intent(this,StartActivity::class.java))
        }
        val timeText  = if(heart == 0) "00:00" else getTime(time)
        b.totalTime.text = timeText
        lifecycleScope.launch {
        val res = API.get<List<Point>>("/api/users/points/$id", token)
            val totalPoint = res[0].points
            b.allPoint.text = totalPoint.toString()
        }
    }


    private fun getTime(time: Int): String{
        val min = time/60
        val sec = time % 60
        return String.format("%02d:%02d",min,sec)
    }
}