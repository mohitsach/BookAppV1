package com.mohit.BookAppV2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.activity.DescriptionActivity
import com.mohit.BookAppV2.model.Book
import com.squareup.picasso.Picasso

class RecyclerDashboardAdapter(val context:Context,val booklist:ArrayList<Book>):RecyclerView.Adapter<RecyclerDashboardAdapter.DashboardViewHolder>() {


    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imgBook:ImageView=view.findViewById(R.id.imgBook)
        val txtBookName:TextView=view.findViewById(R.id.txtBookName)
        val txtAuthorName:TextView=view.findViewById(R.id.txtAuthorName)
        val txtPrice:TextView=view.findViewById(R.id.txtPrice)
        val txtBookRating:TextView=view.findViewById(R.id.txtBookRating)
        val llcontent:LinearLayout=view.findViewById(R.id.llconent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recyler_dashboard_singleview_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return booklist.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
       val book=booklist[position]
        holder.txtBookName.text=book.bookname
        holder.txtAuthorName.text=book.bookauthor
        holder.txtPrice.text=book.bookprice
        holder.txtBookRating.text=book.bookrating

        Picasso.get().load(book.bookimg).error(R.drawable.default_book_cover).into(holder.imgBook);

        holder.llcontent.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.book_id)
            context.startActivity(intent)
        }
    }
}