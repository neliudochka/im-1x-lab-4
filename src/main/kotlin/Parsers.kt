fun stringToField(str: List<String>): Field {
    if (str.isEmpty() || str.size == 1) {
        throw RuntimeException("File cannot be empty or contain only one row.")
    }

    val size = str[0].split(' ');

    if (size.size != 2) {
        throw RuntimeException("First row should contain two size values.")
    }

    val height = size[0].toIntOrNull()
    val width = size[1].toIntOrNull()

    if (width == null || height == null) {
        throw RuntimeException("Size values should be integers.")
    }

    val figure: MutableSet<Point> = mutableSetOf()
    val landscape: MutableSet<Point> = mutableSetOf()
    val field = str.subList(1, str.size)

    if (field.size != height) {
        throw RuntimeException("Invalid count of field's rows.")
    }

    for (s: String in field) {
        if (s.length != width) {
            throw RuntimeException("Invalid count of field's columns.")
        }
    }

    for (i: Int in field.indices) {
        for (j: Int in field[i].indices) {
            when (field[i][j]) {
                '.' -> continue;
                '#' -> landscape.add(Point(i, j));
                'p' -> figure.add(Point(i, j));
                else -> {
                    throw RuntimeException("Field should contain only .p#")
                }
            }
        }
    }

    return Field(height, width, figure, landscape);
}

fun fieldToString(field: Field): String {
    val stringBuilder = StringBuilder()

    for (y in 0 until field.getHeight()) {
        for (x in 0 until field.getWidth()) {
            val point = Point(x, y)
            val checkCoords = field.getFigure().any { it.x == point.x && it.y == point.y }

            if (checkCoords) {
                stringBuilder.append('p')
            } else if (point in field.getLandscape()) {
                stringBuilder.append('#')
            } else {
                stringBuilder.append('.')
            }
        }
        stringBuilder.append('\n')
    }

    return stringBuilder.toString().trim()
}
