package com.mohit.BookAppV2.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.mohit.BookAppV2.Database.BookDatabase
import com.mohit.BookAppV2.Database.BookEntity
import com.mohit.BookAppV2.R
import com.mohit.BookAppV2.adapter.FavouritesRecyclerAdapter


class FavouritesFragment : Fragment() {
    lateinit var recyclerFavourites:RecyclerView
    lateinit var Progresslayout:RelativeLayout
    lateinit var Progressbar:ProgressBar
    lateinit var layoutmanager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    var dbBookList= listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerFavourites=view.findViewById(R.id.recyclerFavourites)
        Progressbar=view.findViewById(R.id.Progressbar)
        Progresslayout=view.findViewById(R.id.Progresslayout)
        layoutmanager=GridLayoutManager(activity as Context,2)

        dbBookList=retrievefavourites(activity as Context).execute().get()
        if(activity!=null){
            Progresslayout.visibility=View.GONE
            recyclerAdapter=FavouritesRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourites.adapter=recyclerAdapter
            recyclerFavourites.layoutManager=layoutmanager
        }



        return view
    }


    class retrievefavourites(val context: Context):AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
            return db.bookdao().getAllBooks()


        }

    }
}
