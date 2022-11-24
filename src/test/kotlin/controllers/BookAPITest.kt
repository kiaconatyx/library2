package controllers



import models.Book
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.assertEquals

class BookAPITest {

    private var learnKotlin: Book? = null
    private var music: Book? = null
    private var codeApp: Book? = null
    private var bio: Book? = null
    private var fantasy: Book? = null
    private var populatedBooks: BookAPI? = BookAPI()
    private var emptyBooks: BookAPI? = BookAPI()

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
        fun `listAllBooks returns Notes when ArrayList has books stored`() {
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
        fun `listArchivedBooks returns archived notes when ArrayList has archived books stored`() {
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
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val priority2String = populatedBooks!!.listBooksBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no books"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listBooksBySelectedPriority returns all notes that match that priority when books of that priority exist`() {
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
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val ISBN2String = populatedBooks!!.listBooksBySelectedISBN(122).lowercase()
            assertTrue(ISBN2String.contains("no books"))
            assertTrue(ISBN2String.contains("2"))
        }

        @Test
        fun `listBooksBySelectedISBN returns all notes that match that ISBN when books of that ISBN exist`() {
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

}
