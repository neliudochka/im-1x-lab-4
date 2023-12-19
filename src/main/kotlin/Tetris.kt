class Tetris {
    // need to implement
    companion object {
        fun play(field: Field): Field {
            var prevField = field
            var finalField = field.nextStep()
            while (prevField != finalField) {
                prevField = finalField
                finalField = finalField.nextStep()
            }
            return finalField
        }
        fun playMultipleFields(field: Field): List<Field> {
            val fieldsList = mutableListOf<Field>()
            var currentField = field

            while (true) {
                fieldsList.add(currentField)
                val nextField = currentField.nextStep()

                if (currentField == nextField) {
                    break
                }

                currentField = nextField
            }

            return fieldsList
        }
    }
}
