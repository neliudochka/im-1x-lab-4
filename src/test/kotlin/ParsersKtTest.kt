import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class ParsersKtTest {
    private lateinit var list: MutableList<String>
    @BeforeEach
    fun setUp() {
        list = mutableListOf()
    }

    @Test
    fun `Should throw an error if input file is empty`() {
        val expectedExc = "File cannot be empty or contain only one row.";
        val exception = assertThrows<RuntimeException> {
            stringToField(list)
        }

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should throw an error if input file contains only one row`() {
        list.add("7 8")

        val expectedExc = "File cannot be empty or contain only one row.";
        val exception = assertThrows<RuntimeException> {
            stringToField(list)
        }

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should throw an error if first row contain less than two size values`() {
        list.add("7")
        list.add("....")

        val expectedExc = "First row should contain two size values.";
        val exception = assertFailsWith<RuntimeException> {
            stringToField(list)
        }

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should throw an error if first row contain more than two size values`() {
        list.add("7 5 5")
        list.add("....")

        val expectedExc = "First row should contain two size values.";
        val exception = assertFailsWith<RuntimeException> {
            stringToField(list)
        }
        println(exception)

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should throw an error if size values are not integers`() {
        list.add("7 a")
        list.add("....")

        val expectedExc = "Size values should be integers.";
        val exception = assertFailsWith<RuntimeException> {
            stringToField(list)
        }
        println(exception)

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should throw an error if count of field's rows is invalid`() {
        list.add("2 3")
        list.add("...")

        val expectedExc = "Invalid count of field's rows.";
        val exception = assertFailsWith<RuntimeException> {
            stringToField(list)
        }
        println(exception)

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should throw an error if count of field's columns is invalid`() {
        list.add("2 3")
        list.add("...")
        list.add("..")

        val expectedExc = "Invalid count of field's columns.";
        val exception = assertFailsWith<RuntimeException> {
            stringToField(list)
        }
        println(exception)

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should throw an error if field contains something except specific values`() {
        list.add("1 6")
        list.add("..pa.1")

        val expectedExc = "Field should contain only .p#";
        val exception = assertFailsWith<RuntimeException> {
            stringToField(list)
        }
        println(exception)

        assertEquals(expectedExc, exception.message)
    }

    @Test
    fun `Should return Field object with specific parameters`() {
        list.add("2 4")
        list.add("..p#")
        list.add(".pp.")

        val figure = mutableSetOf(Point(2, 0), Point(1, 1), Point(2, 1))
        val landscape = mutableSetOf(Point(3, 0))

        val actualField = stringToField(list)
        val expectedField = Field(2, 4, figure, landscape)

        assertEquals(expectedField, actualField)
    }

    @Test
    fun `Should return expected string when no figure`() {
        val field = Field(
            2,
            3,
            setOf(),
            setOf(Point(0, 1), Point(1, 0))
        )

        val expectedResult = """
            .#.
            #..
        """.trimIndent()

        val actualResult = fieldToString(field)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Should return expected string when no landscape`() {
        val field = Field(
            2,
            3,
            setOf(Point(0, 1), Point(1, 1), Point(1, 0)),
            setOf()
        )

        val expectedResult = """
            .p.
            pp.
        """.trimIndent()

        val actualResult = fieldToString(field)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Should return expected string when no figure and landscape`() {
        val field = Field(
            2,
            3,
            setOf(),
            setOf()
        )

        val expectedResult = """
            ...
            ...
        """.trimIndent()

        val actualResult = fieldToString(field)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Should return expected string for small field`() {
        val field = Field(
            2,
            3,
            setOf(Point(1, 0), Point(1, 1)),
            setOf(Point(0, 0), Point(0, 1), Point(2, 1))
        )

        val expectedResult = """
            #p.
            #p#
        """.trimIndent()

        val actualResult = fieldToString(field)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Should return expected string`() {
        val field = Field(
            7,
            8,
            setOf(
                Point(2, 2),
                Point(1, 3),
                Point(2, 3),
                Point(3, 3),
                Point(2, 4)),
            setOf(
                Point(3, 4),
                Point(3, 5),
                Point(7, 5),
                Point(0, 6),
                Point(3, 6),
                Point(4, 6),
                Point(5, 6),
                Point(6, 6),
                Point(7, 6)
            )
        )

        val expectedResult = """
            ........
            ........
            ..p.....
            .ppp....
            ..p#....
            ...#...#
            #..#####
        """.trimIndent()

        val actualResult = fieldToString(field)

        assertEquals(expectedResult, actualResult)
    }
}