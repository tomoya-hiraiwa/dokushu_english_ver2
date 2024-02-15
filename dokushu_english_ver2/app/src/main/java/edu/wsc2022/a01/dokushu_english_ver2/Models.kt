package edu.wsc2022.a01.dokushu_english_ver2

data class GetToken(val username: String = "dokushu", val password: String = "d0kushuEN")
data class Token(val refresh: String, val access: String)
data class LoginUser(val user_id: Int,val name: String,val email: String,val created_datetime: String, val updated_datetime: String)
data class CreateUser(val name: String, val email: String, val password: String)
data class Question(val question_id: Int, val sentence: String,val text: String, val answer: String, val choice_type: String,val group_id: String, val choice_set:List<ChoiceSet>, val updated_datetime: String )
data class ChoiceSet(val value: String,val question: Int, val updated_datetime: String)
data class Point(val user_id: Int,val points: Int)