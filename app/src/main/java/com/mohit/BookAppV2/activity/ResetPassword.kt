package com.mohit.BookAppV2.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.util.ConnectionManager
import org.json.JSONObject

class ResetPassword : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var etOTP: EditText
    lateinit var etnewpassword: EditText
    lateinit var etconfirmnewpassword: EditText
    lateinit var btnSubmit: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Reset Password"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences=getSharedPreferences(getString(R.string.Preferences_file_name), Context.MODE_PRIVATE)
        val phonenumber = intent.getStringExtra("Mobile Number")

        etOTP = findViewById(R.id.etOTP)
        etnewpassword = findViewById(R.id.etnewpassword)
        etconfirmnewpassword = findViewById(R.id.etconfirmnewpassword)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            if (etconfirmnewpassword.text.toString() == etnewpassword.text.toString()) {
                val queue = Volley.newRequestQueue(this)
                val url = "http://********/v2/reset_password/fetch_result/"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", phonenumber)
                jsonParams.put("password", etnewpassword.text.toString())
                jsonParams.put("otp",etOTP.text.toString())
                if (ConnectionManager().checkconnectivity(this)) {
                    val jsonobject =
                        object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                            try {
                                println("Response is $it")
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {
                                    val userjsonobject = data.getJSONObject("data")
                                     Toast.makeText(this@ResetPassword,"${userjsonobject.getJSONObject("successMessage")}",Toast.LENGTH_LONG).show()
                                        val intent = Intent(
                                            this@ResetPassword,
                                            LoginActivity::class.java
                                        )
                                        startActivity(intent)
                                    savedpreferences()


                                } else {
                                    val userjsonobject = it.getJSONObject("data")
                                    val errormessage = userjsonobject.getString("errorMessage")
                                    Toast.makeText(
                                        this@ResetPassword,
                                        errormessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            } catch (e: Error) {
                                Toast.makeText(
                                    this@ResetPassword,
                                    "Unexpected error",
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        }, Response.ErrorListener {
                            Toast.makeText(this@ResetPassword, "Volley Error", Toast.LENGTH_LONG)
                                .show()
                        }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = ""
                                return headers
                            }
                        }
                    queue.add(jsonobject)
                } else {
                    val dialog = AlertDialog.Builder(this@ResetPassword)
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
                        ActivityCompat.finishAffinity(this@ResetPassword)
                    }
                    dialog.create()
                    dialog.show()

                }
            }else{
                Toast.makeText(this@ResetPassword,"Password not matching",Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun savedpreferences(){
        sharedPreferences.edit().putBoolean("LoggedIn",false).apply()
    }
}

