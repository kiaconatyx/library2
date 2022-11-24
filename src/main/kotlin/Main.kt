import controllers.BookAPI
import models.Book
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val BookAPI = BookAPI()

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        Book Depositry APP      |
         > ----------------------------------
         > | Book Menu                      |
         > |   1) Add a book                |
         > |   2) List all books            |
         > |   3) Update a book             |
         > |   4) Delete a book             |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> deleteBook()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter a title for the book: ")
    val bookPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val bookISBN = readNextInt("Enter a value ")
    val bookGenre = readNextLine("Enter a category for the note: ")
    val isAdded = BookAPI.add(Book(bookTitle, bookPriority, bookISBN, bookGenre, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks(){
    println(BookAPI.listAllBooks())
}

fun updateBook() {
    listBooks()
    if (BookAPI.numberOfBooks() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the book to update: ")
        if (BookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a title for the book: ")
            val bookPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val bookISBN = readNextInt("Enter an ISBN")
            val bookGenre = readNextLine("Enter a genre for the book: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (BookAPI.updateBook(indexToUpdate, Book( bookTitle , bookPriority, bookISBN, bookGenre))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun deleteBook(){
    listBooks()
    if (BookAPI.numberOfBooks() > 0) {
        val indexToDelete = readNextInt("Enter the index of the book to delete: ")
        val bookToDelete = BookAPI.deleteBook(indexToDelete)
        if (bookToDelete != null) {
            println("Delete Successful! Deleted book: ${bookToDelete.bookTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exit(0)
}
