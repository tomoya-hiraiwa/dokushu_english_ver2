package edu.wsc2022.a01.dokushu_english_ver2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import edu.wsc2022.a01.dokushu_english_ver2.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        val pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        val mailEdit = b.mailEdit
        val passEdit = b.passEdit
        val errorView = b.errorText

        b.siginButton.setOnClickListener {
            val errorText = buildString {
                if (mailEdit.text.isNullOrEmpty()) append("Emailは必須です\n")
                else if (!mailEdit.text!!.contains("@") || mailEdit.text!!.length < 3) append("Emailの形式が正しくありません\n")
                if (passEdit.text.isNullOrEmpty()) append("Passwordは必須です")
            }
            if (errorText.isNotEmpty()){
                Log.d("loginError", "$errorText")
                errorView.text = errorText
                errorView.visibility = View.VISIBLE
            }
            else{
                lifecycleScope.launch {
                    val res = API.login("email=${mailEdit.text.toString()}&password=${passEdit.text.toString()}")
                    println("res = $res")
                    if(res.isNotEmpty()){
                        pref.edit().apply {
                            putInt("userid",res[0].user_id)
                                .commit()
                        }
                        startActivity(Intent(this@LoginActivity, StartActivity::class.java))
                    }
                }
            }
        }
        lifecycleScope.launch {
            val result = API.post<Token>("/api/token/", data = GetToken())
            token = result.access
        }
        b.siginupButton.setOnClickListener {
        startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}