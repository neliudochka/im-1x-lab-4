fun main(args: Array<String>) {
    println("Hello World!")
    val output = object : MainOutput {
        override fun printLine(msg: String) {
            println(msg)
        }
    }
}

interface MainOutput {
    fun printLine(msg: String)
}

interface FileSystem {
    fun doesFileExist(filePaths: String): Boolean
    fun readFileAsString(filePaths: String): String
}
fun mainHandler(args: Array<String>, output: MainOutput, fs: FileSystem) {
    if (args.isEmpty()) {
        output.printLine(Messages.noArgs)
        return
    }

    if (!fs.doesFileExist(args[0])) {
        output.printLine(Messages.inputFileDoesNotExist)
        return
    }

    val field = try {
        stringToField(fs.readFileAsString(args[0]))
    } catch (e: Exception) {
        null
    }

    if (field == null) {
        output.printLine(Messages.inputFileContainsWrongData)
    } else {
        val resField = Tetris.play(field);
        output.printLine(fieldToString(resField))
    }
}

object Messages {
    val noArgs = """
    Hi, this is tetris game! If you want to play, please, pass an input file. For example:
    $ tetris input.txt
  """.trimIndent()

    val inputFileDoesNotExist = """
        The file you passed does not exist
    """.trimIndent()

    val inputFileContainsWrongData = """
        Wrong data!
    """.trimIndent()
}