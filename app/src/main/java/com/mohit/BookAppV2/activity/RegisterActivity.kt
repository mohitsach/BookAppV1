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
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {



    lateinit var etNameRegister:EditText
    lateinit var etEmailRegister:EditText
    lateinit var etMobileRegister:EditText
    lateinit var etPasswordRegister:EditText
    lateinit var etConfirmPasswordRegister:EditText
    lateinit var etAddressRegister:EditText
    lateinit var btnRegister:Button
    lateinit var toolbar: Toolbar
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = getSharedPreferences(getString(R.string.Preferences_file_name), Context.MODE_PRIVATE)
        etNameRegister = findViewById(R.id.etNameRegister)
        etAddressRegister = findViewById(R.id.etAddressRegister)
        etEmailRegister = findViewById(R.id.etEmailRegister)
        etMobileRegister = findViewById(R.id.etMobileRegister)
        etPasswordRegister = findViewById(R.id.etPasswordRegister)
        etConfirmPasswordRegister = findViewById(R.id.etConfirmPasswordRegister)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            if (etConfirmPasswordRegister.text.toString() == etPasswordRegister.text.toString()) {

                val queue = Volley.newRequestQueue(this)
                val url = "http://*******/v2/register/fetch_result/"
                val jsonParams = JSONObject()
                jsonParams.put("name", etNameRegister.text.toString())
                jsonParams.put("mobile_number", etMobileRegister.text.toString())
                jsonParams.put("password", etPasswordRegister.text.toString())
                jsonParams.put("address", etAddressRegister.text.toString())
                jsonParams.put("email", etEmailRegister.text.toString())
                if (ConnectionManager().checkconnectivity(this)) {
                    val jsonrequest =
                        object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                            try {
                                println("Respons is $it")
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {
                                    val userjsonobject = data.getJSONObject("data")
                                    val userid = userjsonobject.getString("user_id")
                                    val name = userjsonobject.getString("name")
                                    val email = userjsonobject.getString("email")
                                    val mobilenumber = userjsonobject.getString("mobile_number")
                                    val address = userjsonobject.getString("address")

                                    saveprefernces(
                                        etPasswordRegister.text.toString(),
                                        mobilenumber,
                                        userid,
                                        name,
                                        email,
                                        address
                                    )
                                    val intent=Intent(this@RegisterActivity,MenuActivity::class.java)
                                    startActivity(intent)
                                }else{
                                    val userjsonobject=it.getJSONObject("data")
                                    val errormessage=userjsonobject.getString("errorMessage")
                                    Toast.makeText(this@RegisterActivity,errormessage,Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Unexpected Error",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }, Response.ErrorListener {
                            Toast.makeText(this@RegisterActivity, "Volley Error", Toast.LENGTH_LONG)
                                .show()
                        }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = ""
                                return headers
                            }
                        }
                    queue.add(jsonrequest)

                } else {
                    val dialog = AlertDialog.Builder(this@RegisterActivity)
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
                        ActivityCompat.finishAffinity(this@RegisterActivity)
                    }
                    dialog.create()
                    dialog.show()

                }
            }
        }

    }
    override fun onPause() {
        super.onPause()
        finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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