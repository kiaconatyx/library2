package controllers

import models.Book

class BookAPI {
    private var book= = ArrayList<Book>()
    fun add(book: Book): Boolean {
        val books
        return books.add(book)
    }

    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                listOfBooks += "${i}: ${books[i]} \n"
            }
            listOfBooks
        }
    }
}