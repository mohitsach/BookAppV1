package com.mohit.BookAppV2.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Books")
data class BookEntity (
    @PrimaryKey val book_id:Int,
    @ColumnInfo(name = "book_name") val bookname:String,
    @ColumnInfo(name = "book_author") val bookauthor:String,
    @ColumnInfo(name = "book_price") val bookprice:String,
    @ColumnInfo(name = "book_rating") val bookrating:String,
    @ColumnInfo(name = "book_desc") val bookdesc:String,
    @ColumnInfo(name = "book_image") val bookimage:String

)
