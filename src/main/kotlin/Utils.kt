import java.io.BufferedReader
import java.math.BigInteger

object Utils {

    fun String.intFromBinary() = Integer.parseInt(this, 2)
    fun String.longFromBinary() = BigInteger(this, 2).toLong()
    fun readLinesFromResource(file: String) = readResource(file).lines()

    fun readResource(file: String) = this::class.java.getResourceAsStream(file)!!
        .bufferedReader()
        .use(BufferedReader::readText)
}