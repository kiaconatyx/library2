package controllers

import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NoteAPITest {

    private var learnKotlin: Book? = null
    private var music: Book? = null
    private var codeApp: Book? = null
    private var bio: Book? = null
    private var dragon: Book? = null
    private var populatedBooks: BookAPI? = BookAPI()
    private var emptyBooks: BookAPI? = BookAPI()

    @BeforeEach
    fun setup(){
        learnKotlin = Book("Learning Kotlin", 1, 9099, "fantasy", true)
        music = Book("LEarn to Play Guitar", 5, 10999, "music", true)
        codeApp = Book("Coding 101", 4, 9666, "HowTo", true)
        bio = Book("Buddy Holly", 1, 111, "Bio", false )
        dragon = Book("Dragon Adventures", 3, 6577, "Fiction", false)

        //adding 5 Note to the notes api
        populatedBooks!!.add(learnKotlin!!)
        populatedBooks!!.add(music!!)
        populatedBooks!!.add(codeApp!!)
        populatedBooks!!.add(bio!!)
        populatedBooks!!.add(dragon!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        music = null
        codeApp = null
        bio = null
        dragon = null
        populatedBooks = null
        emptyBooks = null
    }

    @Test
    fun `adding a Book to a populated list adds to ArrayList`(){
        val newBook = Book("Alien Invasion", 4, 8766, "Sci-Fi", true)
        assertTrue(populatedBooks!!.add(newBook))
    }

    @Test
    fun `adding a Book to an empty list adds to ArrayList`(){
        val newBook = Book("Johnny Cash Life Story", 1, 21, "bio", false)
        assertTrue(emptyBooks!!.add(newBook))
    }

    @Test
    fun `adding a Book to a populated list adds to ArrayList`(){
        val newBook = Book("Cary Grant Life", 3, 8777, "Bio", false)
        assertEquals(5, populatedBooks!!.numberOfBooks())
        assertTrue(populatedBooks!!.add(newBook))
        assertEquals(6, populatedBooks!!.numberOfBooks())
        assertEquals(newBook, populatedBooks!!.findNote(populatedBooks!!.numberOfBooks() - 1))
    }

    @Test
    fun `adding a Book to an empty list adds to ArrayList`(){
        val newBook = Book("Cary Grant Life", 3, 8777, "Bio", false)
        assertEquals(0, emptyBooks!!.numberOfBooks())
        assertTrue(emptyBooks!!.add(newBook))
        assertEquals(1, emptyBooks!!.numberOfBooks())
        assertEquals(newBook, emptyBooks!!.findNote(emptyBooks!!.numberOfBooks() - 1))
    }

    @Test
    fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
        assertEquals(0, emptyBooks!!.numberOfBooks())
        assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books"))
    }

    @Test
    fun `listAllBooks returns Notes when ArrayList has notes stored`() {
        assertEquals(5, populatedBooks!!.numberOfBooks())
        val booksString = populatedBooks!!.listAllBooks().lowercase()
        assertTrue(booksString.contains("learning kotlin"))
        assertTrue(booksString.contains("code app"))
        assertTrue(booksString.contains("music"))
        assertTrue(booksString.contains("bio"))
        assertTrue(booksString.contains("dragon"))
    }
}