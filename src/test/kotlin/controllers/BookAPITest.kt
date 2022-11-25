package controllers



import models.Book
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals

class BookAPITest {

    private var learnKotlin: Book? = null
    private var music: Book? = null
    private var codeApp: Book? = null
    private var bio: Book? = null
    private var fantasy: Book? = null
    private var populatedBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))
    private var emptyBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))

    @BeforeEach
    fun setup(){
        learnKotlin = Book("Learning Kotlin", 5, 2333, "howto", false)
        music = Book("How To Play Guitar", 1, 22, "music", true)
        codeApp = Book("Coding 101", 4, 222, "tech", false)
        bio = Book("The Life Of Elvis Presley", 4, 1935, "biopic", false)
        fantasy = Book("Land Of Zelda", 3, 21111, "fiction", true)


        populatedBooks!!.add(learnKotlin!!)
        populatedBooks!!.add(music!!)
        populatedBooks!!.add(codeApp!!)
        populatedBooks!!.add(bio!!)
        populatedBooks!!.add(fantasy!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        music = null
        codeApp = null
        bio = null
        fantasy = null
        populatedBooks = null
        emptyBooks = null
    }

    @Nested

    inner class UpdateNotes {
        @Test
        fun `updating a book that does not exist returns false`(){
            assertFalse(populatedBooks!!.updateBook(6, Book("Updating Book", 2, 999, "howto", false)))
            assertFalse(populatedBooks!!.updateBook(-1, Book("Updating Book", 2, 999, "howto", false)))
            assertFalse(emptyBooks!!.updateBook(0, Book("Updating Book", 2, 999, "howto", false)))
        }

        @Test
        fun `updating a book that exists returns true and updates`() {
            //check note 5 exists and check the contents
            assertEquals(music, populatedBooks!!.findBook(4))
            assertEquals("music", populatedBooks!!.findBook(4)!!.bookTitle)
            assertEquals(3, populatedBooks!!.findBook(4)!!.bookPriority)
            assertEquals("fiction", populatedBooks!!.findBook(4)!!.bookGenre)

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedBooks!!.updateBook(4, Book("Updating Book", 2, 2111, "fiction", false)))
            assertEquals("Updating Book", populatedBooks!!.findBook(4)!!.bookTitle)
            assertEquals(2, populatedBooks!!.findBook(4)!!.bookPriority)
            assertEquals("bio", populatedBooks!!.findBook(4)!!.bookGenre)
        }
    }
    @Nested
    inner class DeleteBooks {

        @Test
        fun `deleting a Book that does not exist, returns null`() {
            assertNull(emptyBooks!!.deleteBook(0))
            assertNull(populatedBooks!!.deleteBook(-1))
            assertNull(populatedBooks!!.deleteBook(5))
        }

        @Test
        fun `deleting a book that exists delete and returns deleted object`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertEquals(music, populatedBooks!!.deleteBook(4))
            assertEquals(4, populatedBooks!!.numberOfBooks())
            assertEquals(learnKotlin, populatedBooks!!.deleteBook(0))
            assertEquals(3, populatedBooks!!.numberOfBooks())
        }
    }
    @Nested
    inner class AddBooks {
        @Test
        fun `adding a Book to a populated list adds to ArrayList`() {
            val newBook = Book("Jefree Dahmer", 1, 1977, "True Crime", false)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertTrue(populatedBooks!!.add(newBook))
            assertEquals(6, populatedBooks!!.numberOfBooks())
            assertEquals(newBook, populatedBooks!!.findBook(populatedBooks!!.numberOfBooks() - 1))
        }

        @Test
        fun `adding a Book to an empty list adds to ArrayList`() {
            val newBook = Book("John Lydon - Anarchy in the UK", 1, 870, "bio", true)
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.add(newBook))
            assertEquals(1, emptyBooks!!.numberOfBooks())
            assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
        }
    }

    @Nested
    inner class ListBooks {

        @Test
        fun `listAllBooks returns No Books Stored message when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books"))
        }

        @Test
        fun `listAllBooks returns books when ArrayList has books stored`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val booksString = populatedBooks!!.listAllBooks().lowercase()
            assertTrue(booksString.contains("learning kotlin"))
            assertTrue(booksString.contains("code app"))
            assertTrue(booksString.contains("music"))
            assertTrue(booksString.contains("fantasy"))
            assertTrue(booksString.contains("bio"))
        }

        @Test
        fun `listActiveBooks returns no active books stored when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfActiveBooks())
            assertTrue(
                emptyBooks!!.listActiveBooks().lowercase().contains("no active books")
            )
        }

        @Test
        fun `listActiveBooks returns active books when ArrayList has active books stored`() {
            assertEquals(3, populatedBooks!!.numberOfActiveBooks())
            val activeBooksString = populatedBooks!!.listActiveBooks().lowercase()
            assertTrue(activeBooksString.contains("learning kotlin"))
            assertFalse(activeBooksString.contains("code app"))
            assertTrue(activeBooksString.contains("music"))
            assertTrue(activeBooksString.contains("fantasy"))
            assertFalse(activeBooksString.contains("bio"))
        }

        @Test
        fun `listArchivedBooks returns no archived books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfArchivedBooks())
            assertTrue(
                emptyBooks!!.listArchivedBooks().lowercase().contains("no archived books")
            )
        }

        @Test
        fun `listArchivedBooks returns archived books when ArrayList has archived books stored`() {
            assertEquals(2, populatedBooks!!.numberOfArchivedBooks())
            val archivedBooksString = populatedBooks!!.listArchivedBooks().lowercase()
            assertFalse(archivedBooksString.contains("learning kotlin"))
            assertTrue(archivedBooksString.contains("code app"))
            assertFalse(archivedBooksString.contains("music"))
            assertFalse(archivedBooksString.contains("bio"))
            assertTrue(archivedBooksString.contains("fantasy"))
        }

        @Test
        fun `listBooksBySelectedPriority returns No Books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listBooksBySelectedPriority(1).lowercase().contains("no books")
            )
        }

        @Test
        fun `listBooksBySelectedPriority returns no books when no books of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 books), 5 (1 note)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val priority2String = populatedBooks!!.listBooksBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no books"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listBooksBySelectedPriority returns all books that match that priority when books of that priority exist`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val priority1String = populatedBooks!!.listBooksBySelectedPriority(1).lowercase()
            assertTrue(priority1String.contains("1 book"))
            assertTrue(priority1String.contains("priority 1"))
            assertTrue(priority1String.contains("fantasy"))
            assertFalse(priority1String.contains("bio"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("music"))


            val priority4String = populatedBooks!!.listBooksBySelectedPriority(4).lowercase()
            assertTrue(priority4String.contains("2 note"))
            assertTrue(priority4String.contains("priority 4"))
            assertFalse(priority4String.contains("music"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("bio"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("fantasy"))
        }


        @Test
        fun `listBooksBySelectedISBN returns No Books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listBooksBySelectedISBN(1977).lowercase().contains("no books")
            )
        }

        @Test
        fun `listBooksBySelectedISBN returns no books when no books of that ISBN exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 books), 5 (1 note)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val ISBN2String = populatedBooks!!.listBooksBySelectedISBN(122).lowercase()
            assertTrue(ISBN2String.contains("no books"))
            assertTrue(ISBN2String.contains("2"))
        }

        @Test
        fun `listBooksBySelectedISBN returns all books that match that ISBN when books of that ISBN exist`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val ISBN1String = populatedBooks!!.listBooksBySelectedISBN(1935).lowercase()
            assertTrue(ISBN1String.contains("1 book"))
            assertTrue(ISBN1String.contains("priority 1"))
            assertTrue(ISBN1String.contains("fantasy"))
            assertFalse(ISBN1String.contains("bio"))
            assertFalse(ISBN1String.contains("learning kotlin"))
            assertFalse(ISBN1String.contains("code app"))
            assertFalse(ISBN1String.contains("music"))


            val ISBN4String = populatedBooks!!.listBooksBySelectedISBN(870).lowercase()
            assertTrue(ISBN4String.contains("2 note"))
            assertTrue(ISBN4String.contains("priority 4"))
            assertFalse(ISBN4String.contains("music"))
            assertTrue(ISBN4String.contains("code app"))
            assertTrue(ISBN4String.contains("bio"))
            assertFalse(ISBN4String.contains("learning kotlin"))
            assertFalse(ISBN4String.contains("fantasy"))
        }

    }

    @Test
    fun `saving and loading an empty collection in JSON doesn't crash app`() {
        // Saving an empty books.json file.
        val storingBooks = BookAPI(JSONSerializer(File("books.json")))
        storingBooks.store()

        //Loading the empty books.json file into a new object
        val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
        loadedBooks.load()

        //Comparing the source of the books (storingbooks) with the json loaded books (loadedbooks)
        assertEquals(0, storingBooks.numberOfBooks())
        assertEquals(0, loadedBooks.numberOfBooks())
        assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
    }

    @Test
    fun `saving and loading an loaded collection in JSON doesn't loose data`() {
        // Storing 3 books to the books.json file.
        val storingBooks = BookAPI(JSONSerializer(File("books.json")))
        storingBooks.add(music!!)
        storingBooks.add(codeApp!!)
        storingBooks.add(fantasy!!)
        storingBooks.store()

        //Loading books.json into a different collection
        val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
        loadedBooks.load()

        //Comparing the source of the books (storingbooks) with the json loaded books (loadedbooks)
        assertEquals(3, storingBooks.numberOfBooks())
        assertEquals(3, loadedBooks.numberOfBooks())
        assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
        assertEquals(storingBooks.findBook(0), loadedBooks.findBook(0))
        assertEquals(storingBooks.findBook(1), loadedBooks.findBook(1))
        assertEquals(storingBooks.findBook(2), loadedBooks.findBook(2))
    }
}
