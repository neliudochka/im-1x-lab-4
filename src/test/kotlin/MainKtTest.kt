import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MainKtTest {
    private val output: MainOutput = mockk()
    private val defaultFsMock: FileSystem = mockk()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { output.printLine(any()) } just Runs
    }

    @Test
    fun `Should return message with instructions if no args were passed`() {
        mainHandler(emptyArray(), output, defaultFsMock)

        //щоб не було помилки аут оф баундс
        verify(exactly = 1) { output.printLine(Messages.noArgs) }
    }

    @Test
    fun `Should return message if an input file does not exist`() {
        val fsMock: FileSystem = mockk()
        every { fsMock.doesFileExist(any()) } returns false

        mainHandler(arrayOf("fileDoesNotExist.txt"), output, fsMock)

        verify(exactly = 1) { output.printLine(Messages.inputFileDoesNotExist) }
    }

    @Test
    fun `Should throw an error if input file exists and has a syntax error`() {
        val fsMock: FileSystem = mockk()
        every { fsMock.doesFileExist(any()) } returns true
        every { fsMock.readFileAsStringList(any()) } returns """
            Wrong data
            Wrong data
        """.trimIndent().lines()

        mainHandler(arrayOf("input.txt"), output, fsMock)

        verify(exactly = 1) { output.printLine(Messages.inputFileContainsWrongData) }
    }

    @Test
    fun `Should print result field`() {
        val fsMock: FileSystem = mockk()
        every { fsMock.doesFileExist(any()) } returns true
        every { fsMock.readFileAsStringList(any()) } returns """
                7 8
                ..p.....
                .ppp....
                ..p.....
                ........
                ...#....
                ...#...#
                #..#####
            """.trimIndent().lines()

        mainHandler(arrayOf("input.txt"), output, fsMock)

        val resultField = """
            ........
            ........
            ..p.....
            .ppp....
            ..p#....
            ...#...#
            #..#####
        """.trimIndent()

        verify(exactly = 1) { output.printLine(resultField) }
    }

    @Test
    fun `Should print each step if parameter exists`() {
        val fsMock: FileSystem = mockk()
        every { fsMock.doesFileExist(any()) } returns true
        every { fsMock.readFileAsStringList(any()) } returns """
                7 8
                ..p.....
                .ppp....
                ..p.....
                ........
                ...#....
                ...#...#
                #..#####
            """.trimIndent().lines()

        mainHandler(arrayOf("input.txt", "-printEachStep"), output, fsMock)

        val stepZero = """
            STEP 0
            ..p.....
            .ppp....
            ..p.....
            ........
            ...#....
            ...#...#
            #..#####
            
        """.trimIndent()

        val stepFirst = """
            STEP 1
            ........
            ..p.....
            .ppp....
            ..p.....
            ...#....
            ...#...#
            #..#####

        """.trimIndent()

        val stepSecond = """
            STEP 2
            ........
            ........
            ..p.....
            .ppp....
            ..p#....
            ...#...#
            #..#####
           
        """.trimIndent()

        verify {
            output.printLine(stepZero)
            output.printLine(stepFirst)
            output.printLine(stepSecond)
        }
    }
}