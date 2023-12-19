data class Point(var x: Int,
            var y: Int)

data class Field(private val height: Int, private val width: Int, private val figure: Set<Point>, private val landscape: Set<Point>) {
    init {
        validateSize()
        validateFigure()
    }

    private fun validateFigure() {
        require(onlyOneFigure()) {"In field must be only one figure"}
    }

    private fun onlyOneFigure() : Boolean {
        return true
    }
    private fun validateSize() {
        require(width > 0) { "Width must be greater than 0" }
        require(height > 0) { "Height must be greater than 0" }
    }
    fun getHeight() : Int {
        return height
    }

    fun getWidth() : Int {
        return width
    }

    fun getFigure(): Set<Point> {
        return figure
    }

    fun getLandscape(): Set<Point> {
        return landscape
    }

    fun copy(): Field {
        val newFigure = figure.map { Point(it.x, it.y) }.toSet()
        val newLandscape = landscape.map { Point(it.x, it.y) }.toSet()
        return Field(height, width, newFigure, newLandscape)
    }

    private fun figureCanMove(): Boolean {
        for (figurePoint in figure) {
            val newY = figurePoint.y + 1

            if (newY == height) {
                return false
            }

            for (landscapePoint in landscape) {
                if (figurePoint.x == landscapePoint.x && newY == landscapePoint.y) {
                    return false
                }
            }
        }
        return true
    }

    //returns newField if there is place to move
    //returns same field, if it is not
    fun nextStep() : Field {
        if(!figureCanMove()) {
            return this;
        }

        val newField = this.copy();
        for (figurePoint in newField.figure) {
            figurePoint.y += 1
        }
        return newField
    }
}