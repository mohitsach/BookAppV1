package com.mohit.BookAppV2.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.util.ConnectionManager
import org.json.JSONObject
import java.lang.Error

class LoginActivity : AppCompatActivity() {

    lateinit var etMobileLogin: EditText
    lateinit var etPassLogin: EditText
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView
    lateinit var btnLogin: Button
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title="Log In"

        etMobileLogin = findViewById(R.id.etMobileLogin)
        etPassLogin = findViewById(R.id.etPassLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)
        btnLogin = findViewById(R.id.btnLogin)
        sharedPreferences=getSharedPreferences(getString(R.string.Preferences_file_name), Context.MODE_PRIVATE)
        val loggedin=sharedPreferences.getBoolean("LoggedIn",false)
        if(loggedin){
            val intent2 = Intent(this@LoginActivity, MenuActivity::class.java)
            startActivity(intent2)
            finish()
        }
        btnLogin.setOnClickListener {
            Toast.makeText(
                this@LoginActivity,
                "Logged In",
                Toast.LENGTH_LONG
            ).show()

            val MobileNumber = etMobileLogin.text.toString()
            val Password = etPassLogin.text.toString()
            val queue=Volley.newRequestQueue(this)
            val url="http://*********/v2/login/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number",MobileNumber)
            jsonParams.put("password",Password)
            if(ConnectionManager().checkconnectivity(this)) {
                val jsonobject=
                    object : JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                        try {
                            println("Response is $it")
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                val userjsonobject=data.getJSONObject("data")
                                val userid=userjsonobject.getString("user_id")
                                val name=userjsonobject.getString("name")
                                val email=userjsonobject.getString("email")
                                val mobilenumber=userjsonobject.getString("mobile_number")
                                val address=userjsonobject.getString("address")

                                saveprefernces(Password,MobileNumber,userid,name,email,address)
                                val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                                startActivity(intent)
                            }else{
                                val userjsonobject=it.getJSONObject("data")
                                val errormessage=userjsonobject.getString("errorMessage")
                                Toast.makeText(this@LoginActivity,errormessage,Toast.LENGTH_LONG).show()
                            }

                        }catch (e: Error){
                            Toast.makeText(this@LoginActivity,"Unexpected error",Toast.LENGTH_LONG).show()

                        }

                    },Response.ErrorListener {
                        Toast.makeText(this@LoginActivity,"Volley Error",Toast.LENGTH_LONG).show()
                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = ""
                            return headers
                        }
                    }
                        queue.add(jsonobject)
            }else{
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsintent)
                    finish()
                    //do nothing
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    //do nothing
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialog.create()
                dialog.show()

            }
        }
        txtRegister.setOnClickListener {
            Toast.makeText(
                this@LoginActivity,
                "Register Yourself",
                Toast.LENGTH_LONG
            ).show()
        val intent=Intent(this@LoginActivity,
            RegisterActivity::class.java)
            startActivity(intent)

        }
        txtForgotPassword.setOnClickListener {
            Toast.makeText(
                this@LoginActivity,
                "Forgot Password",
                Toast.LENGTH_LONG
            ).show()
            val intent=Intent(this@LoginActivity,
                ForgotPassword::class.java)
            startActivity(intent)

        }


    }
    override fun onPause() {
        super.onPause()
        finish()
    }

    fun saveprefernces(Password:String,MobileNumber:String,userid:String,name:String,email:String,address:String){
        sharedPreferences.edit().putBoolean("LoggedIn",true).apply()
        sharedPreferences.edit().putString("Password",Password).apply()
        sharedPreferences.edit().putString("MobileNumber",MobileNumber).apply()
        sharedPreferences.edit().putString("userid",userid).apply()
        sharedPreferences.edit().putString("email",email).apply()
        sharedPreferences.edit().putString("address",address).apply()
        sharedPreferences.edit().putString("name",name).apply()
    }




}
