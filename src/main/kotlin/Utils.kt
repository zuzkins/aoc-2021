import java.io.BufferedReader

object Utils {

    fun String.intFromBinary() = Integer.parseInt(this, 2)
    fun readLinesFromResource(file: String) = this::class.java.getResourceAsStream(file)!!
        .bufferedReader()
        .use(BufferedReader::readText)
        .lines()
}