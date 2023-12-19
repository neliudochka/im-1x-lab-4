import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TetrisTest {
    @Test
    fun `Should move figure till the landscape if there is free space`() {
        val field = Field(
            5,
            5,
            setOf(Point(2, 0), Point(2, 1)),
            setOf(Point(2, 4))
        )

        val finalField = Tetris.play(field)

        assertEquals(Point(2, 2), finalField.getFigure().first())
        assertEquals(Point(2, 3), finalField.getFigure().last())
    }

    @Test
    fun `Should move figure till the bottom of field if there is no landscape`() {
        val field = Field(
            6,
            6,
            setOf(Point(2, 0), Point(3, 0), Point(3, 1), Point(4, 1)),
            emptySet()
        )

        val finalField = Tetris.play(field)

        assertEquals(setOf(Point(2, 4), Point(3, 4), Point(3, 5), Point(4, 5)),
            finalField.getFigure())
    }

    @Test
    fun `Should not move figure if there is no space`() {
        val field = Field(
            4,
            5,
            setOf(Point(2, 0), Point(2, 1), Point(2, 2), Point(3, 2)),
            setOf(Point(0, 3), Point(1, 3), Point(2, 3), Point(3, 3), Point(4, 3))
        )

        val finalField = Tetris.play(field)

        assertEquals(setOf(Point(2, 0), Point(2, 1), Point(2, 2), Point(3, 2))
            , finalField.getFigure())
    }

    @Test
    fun `Should move figure lower that landscape if there is space under figure`() {
        val field = Field(
            5,
            6,
            setOf(Point(2, 0), Point(2, 1), Point(2, 2), Point(3, 2)),
            setOf(Point(1, 1), Point(1, 2), Point(1, 3), Point(1, 4),
                Point(4, 1), Point(4, 2), Point(4, 3), Point(4, 4))
        )

        val finalField = Tetris.play(field)

        assertEquals(
            setOf(Point(2, 2), Point(2, 3), Point(2, 4), Point(3, 4))
            , finalField.getFigure())
    }

    @Test
    fun `Should move figure to the bottom if landscape is not under the figure`() {
        val field = Field(
            6,
            3,
            setOf(Point(2, 0), Point(2, 1)),
            setOf(Point(1, 0), Point(1, 1), Point(1, 2), Point(1, 3), Point(1, 4), Point(1, 5))
        )

        val finalField = Tetris.play(field)

        assertEquals(
            setOf(Point(2, 4), Point(2, 5))
            , finalField.getFigure())
    }

    @Test
    fun `Should return the same field if the figure cannot move`() {
        val field = Field(
            2,
            4,
            setOf(Point(1, 0), Point(2, 0)),
            setOf(Point(1, 1), Point(2, 1))
        )

        val result = Tetris.play(field)

        assertEquals(field, result)
    }
}