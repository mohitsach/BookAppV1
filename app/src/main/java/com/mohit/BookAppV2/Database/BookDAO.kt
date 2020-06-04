package com.mohit.BookAppV2.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDAO {
    @Insert
    fun insertBook(bookEntity:BookEntity)
    @Delete
    fun deleteBook(bookEntity: BookEntity)
    @Query("SELECT * FROM books WHERE book_id=:bookid")
    fun getBookbyID(bookid:String):BookEntity
    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity>

}