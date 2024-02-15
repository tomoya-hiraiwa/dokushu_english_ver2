package edu.wsc2022.a01.dokushu_english_ver2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import edu.wsc2022.a01.dokushu_english_ver2.databinding.ActivityQuizBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin


class QuizActivity : AppCompatActivity() {
    private lateinit var b: ActivityQuizBinding
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private lateinit var pref: SharedPreferences
    private lateinit var singleList: RecyclerView
    private lateinit var answerList: RecyclerView
    private lateinit var choiceList: RecyclerView
    private lateinit var singleListAdapter: SingleListAdapter
    private lateinit var choiceListAdapter: ChoiceListAdapter
    private lateinit var answerListAdapter: AnswerListAdapter
    private var quizList = listOf<Question>()
    private var number = 0
    private var id = -1
    private var multiChoiceList = mutableListOf<String>()
    private var heart = 5
    private var totalCount = 0
    private var timeCount = 0
    private var perNum = 0
    private var ansText = ""
    private var correctCount = 0
    private val imageList = listOf(R.drawable.p0,R.drawable.p1,R.drawable.p2,R.drawable.p3,
                            R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(b.root)
        pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        singleList = b.singleList
        answerList = b.ansList
        choiceList = b.choiceList
        getTotalCount()
        singleList.layoutManager = GridLayoutManager(this,2)
        answerList.layoutManager = GridLayoutManager(this,3)
        choiceList.layoutManager = GridLayoutManager(this,3)
        val baseNumber = intent.getIntExtra("question",1)
        countTime()
        behavior = BottomSheetBehavior.from(b.sheet)
        behavior.apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        lifecycleScope.launch {
            val resList = API.get<List<Question>>("/api/questions/1", token)
            quizList = resList.filter { it.group_id.toInt() == baseNumber }
            setQuestion()
        }
        b.sheetNextButton.setOnClickListener {

            number += 1
            if (heart == 0){
                if (totalCount >= 10){
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    b.contContainer.visibility = View.VISIBLE
                    b.contText.text = "HPがゼロになりました!\n累計ポイント10を使用して\nHPを1つ回復できます"
                    b.contContButton.setOnClickListener {
                        addHeart()
                        perNum += 10
                        b.contContainer.visibility = View.GONE
                        b.cover.visibility = View.GONE
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                        setQuestion()
                    }
                    b.contCancelButton.setOnClickListener {
                        sendResult()
                    }
                }
                else sendResult()
            }
            else if (number == 10){
                sendResult()
            }
            else {
                perNum += 10
                b.cover.visibility = View.GONE
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                setQuestion()
            }
        }
        b.nextButton.setOnClickListener {
            val resText =  buildString {
                multiChoiceList.forEach {
                    append(it)
                }
            }
            ansText = resText
            checkAnswer()
            multiChoiceList.clear()
        }
    }
    private fun setQuestion(){
        val question = quizList[number]
        b.sentenceText.text = question.sentence
        b.heartCount.text = heart.toString()
        b.progressImage.setImageResource(imageList[number])
        b.progressText.text = "${perNum}%"
        if (question.choice_type == "single choice"){
            b.singleContainer.visibility = View.VISIBLE
            b.multiFrame.visibility = View.GONE
            b.singleQText.text =  question.text
            singleListAdapter = SingleListAdapter(question.choice_set,this)
            singleList.adapter = singleListAdapter
            singleListAdapter.setOnSingleClicker(object: SingleListAdapter.SingleClicker{
                override fun onClick(data: String) {
                    ansText = data
                    checkAnswer()
                }
            })
        }
        else{
            b.singleContainer.visibility = View.GONE
            b.multiFrame.visibility = View.VISIBLE
            b.multiQText.text = question.text
            b.nextButton.apply {
                isEnabled = false
                setBackgroundColor(resources.getColor(R.color.notEnable,theme))
                setTextColor(resources.getColor(R.color.black,theme))
            }
            choiceListAdapter = ChoiceListAdapter(question.choice_set)
            choiceList.adapter = choiceListAdapter
            answerListAdapter = AnswerListAdapter(multiChoiceList)
            answerList.adapter = answerListAdapter
            choiceListAdapter.setOnChoiceListClicker(object: ChoiceListAdapter.ChoiceListClicker{
                override fun onClick(data: String) {
                    multiChoiceList.add(data)
                    answerListAdapter.notifyDataSetChanged()
                    b.nextButton.apply {
                        isEnabled = true
                        setBackgroundColor(resources.getColor(R.color.button,theme))
                        setTextColor(resources.getColor(R.color.white,theme))
                    }
                    }
            })
            answerListAdapter.setOnAnswerListClicker(object: AnswerListAdapter.AnswerListClicker{
                override fun onClick(data: String) {
                    multiChoiceList.remove(data)
                    choiceListAdapter.selectedList.remove(data)
                    if (multiChoiceList.isEmpty()){
                        b.nextButton.apply {
                            isEnabled = true
                            setBackgroundColor(resources.getColor(R.color.notEnable,theme))
                            setTextColor(resources.getColor(R.color.black,theme))
                        }
                    }
                    choiceListAdapter.notifyDataSetChanged()
                    answerListAdapter.notifyDataSetChanged()
                }
            })
        }
    }
    private fun checkAnswer(){
        b.cover.visibility = View.VISIBLE
        b.cover.setOnClickListener {}
        if (ansText.replace(" ","") == quizList[number].answer){
            correctCount += 1
            b.sheetTrueAnswerText.visibility = View.GONE
            b.sheetImage.setImageResource(R.drawable.check)
            b.sheetCheckText.text = "いい感じ!"
            b.sheetNextButton.text = "次へ"
            addTotalCount()
        }
        else{
            b.sheetImage.setImageResource(R.drawable.wrong)
            b.sheetCheckText.text = "不正解!!"
            b.sheetNextButton.text = "OK"
            b.sheetTrueAnswerText.text = "正解:${quizList[number].answer}"
            b.sheetTrueAnswerText.visibility = View.VISIBLE
            heart = heart -1
        }
        if (number == 9){
            b.sheetNextButton.text = "送信"
        }
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

        private fun countTime(){
            lifecycleScope.launch {
                while (true){
                    timeCount += 1
                    delay(1000)
                }
            }
        }
    private fun sendResult(){
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("number",number)
            putExtra("pernum",perNum)
            putExtra("time",timeCount)
            putExtra("correct",correctCount)
            putExtra("heart",heart)
        }
        startActivity(intent)
    }
    private fun getTotalCount(){
        lifecycleScope.launch {
            id = pref.getInt("userid",-1)
            val res = API.get<List<Point>>("/api/users/points/${id}", token)
            totalCount = res[0].points
        }
    }
    private fun addTotalCount(){
        val data = Point(id, 1)
        lifecycleScope.launch {
            val res = API.post<List<Point>>("/api/users/points/add/", token,data)
            totalCount = res[0].points
        }
    }
    private fun addHeart(){
        lifecycleScope.launch {
            val data = Point(id, 10)
            val res = API.post<List<Point>>("/api/users/points/subtract/", token, data)
            totalCount = res[0].points
            heart += 1
            b.heartCount.text = heart.toString()
        }
    }
}