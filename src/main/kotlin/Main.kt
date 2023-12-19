import java.io.File

fun main(args: Array<String>) {

    val output = object : MainOutput {
        override fun printLine(msg: String) {
            println(msg)
        }
    }

    val fs = object : FileSystem {
        override fun doesFileExist(filePaths: String): Boolean = File(filePaths).exists()
        override fun readFileAsStringList(filePaths: String): List<String> = File(filePaths).readLines().toList()
    }

    mainHandler(args, output, fs);
}

interface MainOutput {
    fun printLine(msg: String)
}

interface FileSystem {
    fun doesFileExist(filePaths: String): Boolean
    fun readFileAsStringList(filePaths: String): List<String>
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

    val printEachStep = args.size > 1 && args[1] == "-printEachStep"

    val field = try {
        stringToField(fs.readFileAsStringList(args[0]))
    } catch (e: Exception) {
        null
    }

    if (field == null) {
        output.printLine(Messages.inputFileContainsWrongData)
    } else {
        if (printEachStep) {
            Tetris.playMultipleFields(field)
                .mapIndexed { index, stepField -> "STEP $index\n${fieldToString(stepField)}\n" }
                .forEach { output.printLine(it) }
        } else {
            val resField = Tetris.play(field);
            output.printLine(fieldToString(resField))
        }
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