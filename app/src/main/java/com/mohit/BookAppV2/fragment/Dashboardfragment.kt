package com.mohit.BookAppV2.fragment

import android.app.Activity
import android.app.AlertDialog
import com.android.volley.Response
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.adapter.RecyclerDashboardAdapter
import com.mohit.BookAppV2.model.Book
import com.mohit.BookAppV2.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class Dashboardfragment : Fragment() {
    lateinit var recyclerDashboard:RecyclerView
    lateinit var layoutmanager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:RecyclerDashboardAdapter
    lateinit var Progresslayout:RelativeLayout
    lateinit var Progressbar:ProgressBar
    val bookInfolist = arrayListOf<Book>()
    var bookcompare= Comparator<Book>{book1,book2 ->
        if (book1.bookrating.compareTo(book2.bookrating,true)==0){
            book1.bookname.compareTo(book2.bookname,true)
        }else{
            book1.bookrating.compareTo(book2.bookrating,true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true)

        val view =inflater.inflate(R.layout.fragment_dashboardfragment, container, false)
        recyclerDashboard=view.findViewById(R.id.recyclerDashboard)
        layoutmanager=LinearLayoutManager(activity)
        Progresslayout=view.findViewById(R.id.Progresslayout)
        Progressbar=view.findViewById(R.id.Progressbar)
        Progresslayout.visibility=View.VISIBLE


        val queue=Volley.newRequestQueue(activity as Context)
        val url ="http://********/v1/book/fetch_books"

        if(ConnectionManager().checkconnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    try{
                        Progresslayout.visibility=View.GONE
                    val success = it.getBoolean("success")
                    if (success) {
                        println("Response is $it")
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            println("Ids $i")
                            val bookjsonobject = data.getJSONObject(i)
                            val bookobject = Book(
                                bookjsonobject.getString("id"),
                                bookjsonobject.getString("book_id"),
                                bookjsonobject.getString("name"),
                                bookjsonobject.getString("author"),
                                bookjsonobject.getString("rating"),
                                bookjsonobject.getString("price"),
                                bookjsonobject.getString("image")
                            )

                            bookInfolist.add(bookobject)
                            recyclerAdapter = RecyclerDashboardAdapter(activity as Context, bookInfolist)
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutmanager

                        }
                    } else {
                        Toast.makeText(activity as Context, "Error", Toast.LENGTH_LONG)
                            .show()
                    }
                } catch(e: JSONException){
                    Toast.makeText(activity as Context,"Errror",Toast.LENGTH_LONG).show()

                }
                }, Response.ErrorListener {
                    if(activity!=null) {
                        Toast.makeText(activity as Context, "Error", Toast.LENGTH_LONG).show()
                    }
                    println("Error is $it")
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = ""
                        return headers
                    }
                }

            queue.add(jsonObjectRequest)
        }else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings"){text,listener ->
                val settingsintent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
                activity?.finish()
                //do nothing
            }
            dialog.setNegativeButton("Exit"){text,listener ->
                //do nothing
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }

         return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.sortmenu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if(id==R.id.sort){
            Collections.sort(bookInfolist,bookcompare)
            bookInfolist.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}
