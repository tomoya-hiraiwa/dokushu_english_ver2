package edu.wsc2022.a01.dokushu_english_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import edu.wsc2022.a01.dokushu_english_ver2.databinding.ActivitySiginUpBinding
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var b: ActivitySiginUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySiginUpBinding.inflate(layoutInflater)
        setContentView(b.root)
        val nameEdit = b.nameEdit
        val mailEdit = b.mailEdit
        val passEdit = b.passEdit
        val nameInput = b.nameInput
        val mailInput = b.mailInput
        val passInput = b.passInput
        b.backArrow.setOnClickListener {
            finish()
        }
        b.compButton.setOnClickListener {
            nameInput.error = null
            mailInput.error = null
            passInput.error = null
            if (nameEdit.text.isNullOrEmpty()) {
                nameInput.error = "Nameは必須です"
            }
            if (!mailEdit.text!!.contains("@") || mailEdit.text!!.length < 3) {
                mailInput.error = "Emailは@が含まれている3文字以上の文字列でなければなりません"
            }
            if (passEdit.text!!.length < 8 || passEdit.text!!.length > 32) {
                passInput.error = "Passwordが8文字以上、32文字以下の文字列でなければなりません。"
            }

            if (nameInput.error == null && mailInput.error == null && passInput.error == null) {
                val data = CreateUser(
                    nameEdit.text.toString(),
                    mailEdit.text.toString(),
                    passEdit.text.toString()
                )
                lifecycleScope.launch {
                    val res = API.post<List<LoginUser>>("/api/users/create/", token, data)
                    if (res.isNotEmpty()) {
                        startActivity(Intent(this@SignUpActivity, StartActivity::class.java))
                    }
                }
            }
        }
    }
}