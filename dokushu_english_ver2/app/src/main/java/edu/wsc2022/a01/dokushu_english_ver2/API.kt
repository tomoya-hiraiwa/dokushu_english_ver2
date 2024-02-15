package edu.wsc2022.a01.dokushu_english_ver2

import androidx.core.graphics.createBitmap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

const val BASE_URL = "http://172.16.234.156:8000"
var token = ""
object API {
    val gson = Gson()
     suspend fun request(arg: String,token: String, body: String = ""):String{
        return withContext(Dispatchers.IO){
            val url = URL("${BASE_URL}$arg")
            val con = url.openConnection() as HttpURLConnection
            con.apply {
                requestMethod = if (body.isNotEmpty()) "POST" else "GET"
                if (token.isNotEmpty()) setRequestProperty("Authorization","Bearer $token")
                if (body.isNotEmpty()) {
                    doOutput = true
                    setRequestProperty("Content-Type","application/json")
                    outputStream.use {
                        it.write(body.toByteArray())
                        it.flush()
                    }
                }
            }
            con.inputStream.bufferedReader().use { it.readText() }
        }
    }

    suspend fun login(body: String):List<LoginUser>{
        val url = URL("${BASE_URL}/api/users/")
        return withContext(Dispatchers.IO){
            val con = url.openConnection() as HttpURLConnection
            con.apply {
                requestMethod = "POST"
                setRequestProperty("Authorization","Bearer $token")
                setRequestProperty("Content-Type","application/x-www-form-urlencoded")
                doOutput = true
                outputStream.use {
                    it.write(body.toByteArray())
                    it.flush()
                }
            }
            gson.fromJson(con.inputStream.bufferedReader().use { it.readText() },object: TypeToken<List<LoginUser>>(){}.type)
        }
    }
    inline suspend fun <reified T> get(arg: String, token: String = ""): T{
        return gson.fromJson(request(arg,token),object : TypeToken<T>(){}.type)
    }

    inline suspend fun <reified T> post(arg: String, token: String = "",data: Any): T{
        val body = gson.toJson(data)
        return gson.fromJson(request(arg,token,body),object : TypeToken<T>(){}.type)
    }
}