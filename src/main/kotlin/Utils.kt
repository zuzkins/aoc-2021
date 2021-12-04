import java.io.BufferedReader

object Utils {

    fun String.intFromBinary() = Integer.parseInt(this, 2)
    fun readLinesFromResource(file: String) = readResource(file).lines()

    fun readResource(file: String) = this::class.java.getResourceAsStream(file)!!
        .bufferedReader()
        .use(BufferedReader::readText)
}