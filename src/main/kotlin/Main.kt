import controllers.BookAPI
import models.Book
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
//private val BookAPI = BookAPI(XMLSerializer(File("books.xml")))
private val BookAPI = BookAPI(JSONSerializer(File("books.json")))

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |       Book Depo    APP         |
         > ----------------------------------
         > | Book Depo MENU                 |
         > |   1) Add a book                |
         > |   2) List books                |
         > |   3) Update a book             |
         > |   4) Delete a book             |
         > |   5) Archive a book            |
         > ----------------------------------
         > |   20) Save Books               |
         > |   21) Load Books               |
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
            5 -> archiveBook()
            20  -> save()
            21  -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter a title for the nook: ")
    val bookPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val bookISBN = readNextInt("Enter a unique ISBN ")
    val bookGenre = readNextLine("Enter a genre for the book: ")
    val isAdded = BookAPI.add(Book(bookTitle, bookPriority,bookISBN, bookGenre, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks(){
    if (BookAPI.numberOfBooks() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL books          |
                  > |   2) View ACTIVE books       |
                  > |   3) View ARCHIVED books     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBooks();
            2 -> listActiveBooks();
            3 -> listArchivedBooks();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No books stored");
    }
}

fun listAllBooks() {
    println(BookAPI.listAllBooks())
}

fun listActiveBooks() {
    println(BookAPI.listActiveBooks())
}

fun listArchivedBooks() {
    println(BookAPI.listArchivedBooks())
}

fun updateBook() {
    listBooks()
    if (BookAPI.numberOfBooks() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the book to update: ")
        if (BookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a title for the book: ")
            val bookPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val bookISBN = readNextInt("Enter a unique ISBN ")
            val bookGenre = readNextLine("Enter a genre for the book: ")

            if (BookAPI.updateBook(indexToUpdate, Book(bookTitle, bookPriority, bookISBN, bookGenre))){
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
        //pass the index of the note to NoteAPI for deleting and check for success.
        val bookToDelete = BookAPI.deleteBook(indexToDelete)
        if (bookToDelete != null) {
            println("Delete Successful! Deleted book: ${bookToDelete.bookTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun archiveBook() {
    listActiveBooks()
    if (BookAPI.numberOfActiveBooks() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the Book to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
        if (BookAPI.archiveBook(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun save() {
    try {
        BookAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        BookAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exit(0)
}
