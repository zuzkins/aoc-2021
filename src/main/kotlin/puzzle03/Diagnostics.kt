package puzzle03

import Utils.intFromBinary
import puzzle03.DiagnosticsUtils.toEpsilon

data class Diagnostics(val gamma: Int = 0, val epsilon: Int = 0) {
    val powerConsumption = gamma * epsilon
}

data class Report(val lines: List<Int>, val magnitude: Int) {

    val diagnostics by lazy {
        val gamma = computeGamma()
        Diagnostics(gamma, gamma.toEpsilon(magnitude))
    }

    fun computeGamma(): Int {
        val inputCount = lines.size
        val threshold = inputCount / 2.0
        var result = 0
        repeat(magnitude) { pos ->
            val mask = 1 shl pos
            val onesCount = lines.fold(0) { ones, cur ->
                if ((cur and mask) > 0) {
                    ones + 1
                } else {
                    ones
                }
            }
            if (onesCount > threshold) {
                result = result or mask
            }
        }
        return result
    }
}

object DiagnosticsUtils {

    fun List<String>.parseDiagnosticReport(): Report {
        if (this.isEmpty()) {
            return Report(emptyList(), 0)
        }
        val magnitude = this.first().length
        val lines = map { line ->
            check(line.length == magnitude) {
                "Required to have only inputs of length $magnitude: '$line'"
            }
            check(line.all { it.isDigit() }) {
                "Required to have only inputs with digits: '$line'"
            }
            line.intFromBinary()
        }

        return Report(lines, magnitude)
    }

    fun Int.toEpsilon(magnitude: Int) = "1".repeat(magnitude).intFromBinary() xor this
}