import controllers.BookAPI
import models.Book
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val bookAPI = BookAPI()


fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a Book                |
         > |   2) List all Books            |
         > |   3) Update a Book             |
         > |   4) Delete a Book             |
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
    //logger.info { "addNote() function invoked" }
    val bookTitle = readNextLine("Enter a title for the book: ")
    val bookPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val bookISBN = readNextInt("Enter book ISBN ")
    val bookGenre = readNextLine("Enter a genre for the book: ")
    val isAdded = bookAPI.add(Book(bookTitle,bookISBN, bookPriority, bookGenre, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks(){
    //logger.info { "listBooks() function invoked" }
    println(bookAPI.listAllBooks())
}

fun updateBook(){
    logger.info { "updateBook() function invoked" }
}

fun deleteBook(){
    logger.info { "deleteBook() function invoked" }
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}

