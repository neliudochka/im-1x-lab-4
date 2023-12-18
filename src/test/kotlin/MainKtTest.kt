import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MainKtTest {
    val messages = mutableListOf<String>()
    private val output: MainOutput = object : MainOutput {
        override fun printLine(msg: String) {
            messages.add(msg)
        }
    }

    private val defaultFsMock = object : FileSystem {
        override fun doesFileExist(filePaths: String): Boolean {
            throw RuntimeException("Unexpected call of doesFileExist method")
        }

        override fun readFileAsString(filePaths: String): String {
            throw RuntimeException("Unexpected call of readFileAsString method")
        }
    }

    @BeforeEach
    fun setUp() {
        messages.clear()
    }

    @Test
    fun `Should return message with instructions if no args were passed`() {
        mainHandler(emptyArray(), output, defaultFsMock)

        //щоб не було помилки аут оф баундс
        assertEquals(1, messages.size)
        assertEquals(Messages.noArgs, messages[0])
    }

    @Test
    fun `Should return message if an input file does not exist`() {
        val fsMock = object : FileSystem {
            override fun doesFileExist(filePaths: String): Boolean = false
            override fun readFileAsString(filePaths: String): String {
                throw RuntimeException("Unexpected call of readFileAsString method")
            }
        }

        mainHandler(arrayOf("fileDoesNotExist.txt"), output, fsMock)

        assertEquals(1, messages.size)
        assertEquals(Messages.inputFileDoesNotExist, messages[0])
    }

    @Test
    fun `Should throw an error if input file exists and has a syntax error`() {
        val fsMock = object : FileSystem {
            override fun doesFileExist(filePaths: String): Boolean = true
            override fun readFileAsString(filePaths: String): String = "Wrong input file body"
        }

        mainHandler(arrayOf("input.txt"), output, fsMock)

        assertEquals(1, messages.size)
        assertEquals(Messages.inputFileContainsWrongData, messages[0])
    }

    @Test
    fun `Should print result field`() {
        val fsMock = object : FileSystem {
            override fun doesFileExist(filePaths: String): Boolean = true
            override fun readFileAsString(filePaths: String): String = """
                ..p.....
                .ppp....
                ..p.....
                ........
                ...#....
                ...#...#
                #..#####
            """.trimIndent()
        }

        mainHandler(arrayOf("input.txt"), output, fsMock)

        assertEquals(1, messages.size)

        val resultField = """
            ........
            ........
            ..p.....
            .ppp....
            ..p#....
            ...#...#
            #..#####
        """.trimIndent();
        assertEquals(resultField, messages[0])
    }
}