package controllers

import models.Book
import persistence.Serializer

class BookAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType

    private var books = ArrayList<Book>()

    fun add(book: Book): Boolean {
        return books.add(book)
    }

    fun deleteBook(indexToDelete: Int): Book? {
        return if (isValidListIndex(indexToDelete, books)) {
            books.removeAt(indexToDelete)
        } else null
    }

    fun updateBook(indexToUpdate: Int, book: Book?): Boolean {
        //find the note object by the index number
        val foundBook = findBook(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundBook != null) && (book != null)) {
            foundBook.bookTitle = book.bookTitle
            foundBook.bookPriority = book.bookPriority
            foundBook.bookISBN = book.bookISBN
            foundBook.bookGenre = book.bookGenre
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun archiveBook(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val bookToArchive = books[indexToArchive]
            if (!bookToArchive.isBookArchived) {
                bookToArchive.isBookArchived = true
                return true
            }
        }
        return false
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

    fun listActiveBooks(): String {
        return if (numberOfActiveBooks() == 0) {
            "No active books stored"
        } else {
            var listOfActiveBooks = ""
            for (book in books) {
                if (!book.isBookArchived) {
                    listOfActiveBooks += "${books.indexOf(book)}: $book \n"
                }
            }
            listOfActiveBooks
        }
    }

    fun listArchivedBooks(): String {
        return if (numberOfArchivedBooks() == 0) {
            "No archived books stored"
        } else {
            var listOfArchivedBooks = ""
            for (book in books) {
                if (book.isBookArchived) {
                    listOfArchivedBooks += "${books.indexOf(book)}: $book \n"
                }
            }
            listOfArchivedBooks
        }
    }

    fun listBooksBySelectedPriority(priority: Int): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                if (books[i].bookPriority == priority) {
                    listOfBooks +=
                        """$i: ${books[i]}
                        """.trimIndent()
                }
            }
            if (listOfBooks.equals("")) {
                "No books with priority: $priority"
            } else {
                "${numberOfBooksByPriority(priority)} books with priority $priority: $listOfBooks"
            }
        }
    }

    fun listBooksBySelectedISBN(ISBN: Int): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                if (books[i].bookISBN == ISBN) {
                    listOfBooks +=
                        """$i: ${books[i]}
                        """.trimIndent()
                }
            }
            if (listOfBooks.equals("")) {
                "No books with ISBN: $ISBN"
            } else {
                "${numberOfBooksByISBN(ISBN)} books with ISBN $ISBN: $listOfBooks"
            }
        }
    }

    fun numberOfBooks(): Int {
        return books.size
    }

    fun numberOfArchivedBooks(): Int {
        var counter = 0
        for (book in books) {
            if (book.isBookArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfActiveBooks(): Int {
        var counter = 0
        for (book in books) {
            if (!book.isBookArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfBooksByPriority(priority: Int): Int {
        //return books.stream().filter { p: Note -> p.notePriority == priority }.count().toInt()
        var counter = 0
        for (book in books) {
            if (book.bookPriority == priority) {
                counter++
            }
        }
        return counter
    }

    fun numberOfBooksByISBN(ISBN: Int): Int {
        //return books.stream().filter { p: Note -> p.notePriority == priority }.count().toInt()
        var counter = 0
        for (book in books) {
            if (book.bookISBN == ISBN) {
                counter++
            }
        }
        return counter
    }

    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, books);
    }

    @Throws(Exception::class)
    fun load() {
        books = serializer.read() as ArrayList<Book>
    }


    @Throws(Exception::class)
    fun store() {
        serializer.write(books)
    }

    fun updateBook(indexToUpdate: Int, book: Book?): Boolean {
        val foundBook = findBook(indexToUpdate)


        if ((foundBook != null) && (book != null)) {
            foundBook.bookTitle = book.bookTitle
            foundBook.bookPriority = book.bookPriority
            foundBook.bookISBN = book.bookISBN
            foundBook.bookGenre = book.bookGenre
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, books);
    }
}