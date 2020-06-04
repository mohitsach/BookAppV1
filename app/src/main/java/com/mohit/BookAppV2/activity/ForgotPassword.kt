package com.mohit.BookAppV2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.util.ConnectionManager
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var etMobileForgot:EditText
    lateinit var etEmailFogot:EditText
    lateinit var btnForgot:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Forgot Password"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etEmailFogot=findViewById(R.id.etEmailForgot)
        etMobileForgot=findViewById(R.id.etMobileForgot)
        btnForgot=findViewById(R.id.btnForgot)

        btnForgot.setOnClickListener {

            val queue= Volley.newRequestQueue(this)
            val url="http://*********/v2/forgot_password/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number",etMobileForgot.text.toString())
            jsonParams.put("email",etEmailFogot.text.toString())
            if(ConnectionManager().checkconnectivity(this)) {
                val jsonobject=
                    object : JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {
                        try {
                            println("Response is $it")
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                val first=data.getBoolean("first_try")
                                if(first) {

                                    val intent = Intent(
                                        this@ForgotPassword,
                                        ResetPassword::class.java
                                    )

                                    intent.putExtra("Mobile Number", etMobileForgot.text.toString())
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(this@ForgotPassword,"${first.toString()}",Toast.LENGTH_LONG).show()
                                }
                            }else{

                                val errormessage=data.getString("errorMessage")
                                Toast.makeText(this@ForgotPassword,errormessage, Toast.LENGTH_LONG).show()
                            }

                        }catch (e: Error){
                            Toast.makeText(this@ForgotPassword,"Unexpected error", Toast.LENGTH_LONG).show()

                        }

                    },Response.ErrorListener {
                        Toast.makeText(this@ForgotPassword,"Volley Error", Toast.LENGTH_LONG).show()
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
                val dialog = AlertDialog.Builder(this@ForgotPassword)
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
                    ActivityCompat.finishAffinity(this@ForgotPassword)
                }
                dialog.create()
                dialog.show()

            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
