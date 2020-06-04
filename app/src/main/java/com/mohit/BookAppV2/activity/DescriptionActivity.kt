package com.mohit.BookAppV2.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohit.BookAppV2.Database.BookDatabase
import com.mohit.BookAppV2.Database.BookEntity
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    lateinit var llcontent:LinearLayout
    lateinit var imgBook:ImageView
    lateinit var txtBookName:TextView
    lateinit var txtAuthorName:TextView
    lateinit var txtPrice:TextView
    lateinit var txtBookRating:TextView
    lateinit var txtabout:TextView
    lateinit var txtdescription:TextView
    lateinit var toolbar: Toolbar
    lateinit var Progresslayout:RelativeLayout
    lateinit var Progressbar:ProgressBar
    lateinit var btnFavourites:Button
    var bookid:String?="100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        llcontent = findViewById(R.id.llconent)
        imgBook = findViewById(R.id.imgBook)
        txtBookName = findViewById(R.id.txtBookName)
        txtAuthorName = findViewById(R.id.txtAuthorName)
        txtPrice = findViewById(R.id.txtPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        txtabout = findViewById(R.id.txtabout)
        txtdescription = findViewById(R.id.txtdescription)
        toolbar = findViewById(R.id.toolbar)
        Progresslayout = findViewById(R.id.Progresslayout)
        Progresslayout.visibility = View.VISIBLE
        Progressbar = findViewById(R.id.Progressbar)
        Progressbar.visibility = View.VISIBLE
        btnFavourites = findViewById(R.id.btnFavourites)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
            bookid = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Error", Toast.LENGTH_LONG).show()
        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://*********/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookid)
        if (ConnectionManager().checkconnectivity(this@DescriptionActivity)) {
            val jsonrequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookjsonobject = it.getJSONObject("book_data")
                            Progresslayout.visibility = View.GONE
                            val bookimage = bookjsonobject.getString("image")
                            Picasso.get().load(bookjsonobject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBook)
                            txtBookName.text = bookjsonobject.getString("name")
                            txtAuthorName.text = bookjsonobject.getString("author")
                            txtPrice.text = bookjsonobject.getString("price")
                            txtBookRating.text = bookjsonobject.getString("rating")
                            txtdescription.text = bookjsonobject.getString("description")

                            val bookEntity = BookEntity(
                                bookid?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtAuthorName.text.toString(),
                                txtPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtdescription.text.toString(),
                                bookimage
                            )
                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                             val isfav = checkFav.get()
                            if(isfav){
                                btnFavourites.text="Remove from Favourites"
                            }else{
                                btnFavourites.text="Add to Favourites"
                            }
                            btnFavourites.setOnClickListener {
                                if(! DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                    val async=DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result=async.get()
                                    if(result){
                                        Toast.makeText(this@DescriptionActivity,"Book Added",Toast.LENGTH_LONG).show()
                                        btnFavourites.text="Remove From Favourites"

                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Error",Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result=async.get()
                                    if(result){
                                        Toast.makeText(this@DescriptionActivity,"Book Removed",Toast.LENGTH_LONG).show()
                                        btnFavourites.text="Add to Favourites"

                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Error",Toast.LENGTH_LONG).show()
                                    }

                                }
                            }

                        } else {

                            Progresslayout.visibility = View.GONE
                            Toast.makeText(this@DescriptionActivity, "Error", Toast.LENGTH_LONG)
                                .show()
                        }
                    } catch (e: Exception) {
                        Progresslayout.visibility = View.GONE
                        Toast.makeText(this@DescriptionActivity, "Error", Toast.LENGTH_LONG).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivity, "Volley Error", Toast.LENGTH_LONG)
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
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
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
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()

        }
    }

    class DBAsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int): AsyncTask<Void,Void,Boolean>(){

        val db =Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode){
                1 -> {
                val book:BookEntity?=db.bookdao().getBookbyID(bookEntity.book_id.toString())
                    db.close()
                    return book !=null
                }
                2 -> {
                db.bookdao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {

                    db.bookdao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }





            return false

        }

    }




    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


