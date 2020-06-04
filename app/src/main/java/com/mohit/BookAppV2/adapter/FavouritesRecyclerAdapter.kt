package com.mohit.BookAppV2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mohit.BookAppV2.Database.BookEntity
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.activity.DescriptionActivity
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context:Context,val booklist:List<BookEntity>):
RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouriteViewHolder>() {


    class FavouriteViewHolder(view:View):RecyclerView.ViewHolder(view){
        val imgBook: ImageView =view.findViewById(R.id.imgFavBookImage)
        val txtBookName: TextView =view.findViewById(R.id.txtFavBookTitle)
        val txtAuthorName: TextView =view.findViewById(R.id.txtFavBookAuthor)
        val txtPrice: TextView =view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating: TextView =view.findViewById(R.id.txtFavBookRating)
        val llFavcontent: LinearLayout =view.findViewById(R.id.llFavContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return booklist.size

    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
    val book=booklist[position]
        holder.txtBookName.text=book.bookname
        holder.txtAuthorName.text=book.bookauthor
        holder.txtBookRating.text=book.bookrating
        holder.txtPrice.text=book.bookprice
        Picasso.get().load(book.bookimage).error(R.drawable.default_book_cover).into(holder.imgBook)
        holder.llFavcontent.setOnClickListener {
            try {

                val intent = Intent(context, DescriptionActivity::class.java)
                intent.putExtra("book_id", book.book_id.toString())
                context.startActivity(intent)
            }catch (e:Error){
                Toast.makeText(context,"error",Toast.LENGTH_LONG).show()
            }
        }
    }
}