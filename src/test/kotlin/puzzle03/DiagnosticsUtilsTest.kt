package puzzle03

import Utils.intFromBinary
import Utils.readLinesFromResource
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import puzzle03.DiagnosticsUtils.parseDiagnosticReport
import puzzle03.DiagnosticsUtils.toEpsilon

internal class DiagnosticsUtilsTest {

    @Test
    fun `can parse no diagnostic instructions`() {
        assertThat(emptyList<String>().parseDiagnosticReport()).isEqualTo(Report(emptyList(), 0))
    }

    @Test
    fun `requires input to same number of characters`() {
        assertThatCode {
            listOf("011", "0111").parseDiagnosticReport()
        }.hasMessage("Required to have only inputs of length 3: '0111'")
    }

    @Test
    fun `requires input to be digits only`() {
        assertThatCode {
            listOf("1234x").parseDiagnosticReport()
        }.hasMessage("Required to have only inputs with digits: '1234x'")
    }

    @Test
    fun `can parse diagnostic instruction`() {
        assertThat(listOf("00000").parseDiagnosticReport()).isEqualTo(Report(listOf(0), 5))
    }

    @Test
    fun `can compute simple gamma`() {
        val gamma = listOf("00100").parseDiagnosticReport().computeGamma()

        assertThat(gamma).isEqualTo("00100".intFromBinary())
    }

    @Test
    fun `can determine most common count correctly`() {
        val gamma = listOf("00100", "10100").parseDiagnosticReport().computeGamma()
        assertThat(gamma).isEqualTo("00100".intFromBinary())
    }

    @Test
    fun `can compute slightly complicated gamma`() {
        val gamma = listOf("00100", "10100", "11111").parseDiagnosticReport().computeGamma()
        assertThat(gamma).isEqualTo("10100".intFromBinary())
    }

    val testExample = listOf(
        "11110",
        "10110",
        "10111",
        "10101",
        "01111",
        "00111",
        "11100",
        "10000",
        "11001",
        "00010",
        "01010",
    )

    @Test
    fun `can compute gamma from the example`() {
        val gamma = testExample.parseDiagnosticReport().computeGamma()
        assertThat(gamma).isEqualTo("10110".intFromBinary())
    }

    @Test
    fun `can compute epsilon from gamma`() {
        val epsilon = "10110".intFromBinary().toEpsilon(5)
        assertThat(epsilon).isEqualTo("01001".intFromBinary())
    }

    @Test
    fun `can compute power consumption correctly`() {
        val gamma = testExample.parseDiagnosticReport().computeGamma()
        assertThat(Diagnostics(gamma, gamma.toEpsilon(5)).powerConsumption).isEqualTo(198)
    }

    val exampleInput = listOf(
        "111111010011",
        "110011001100",
        "010011111000",
        "101001100011",
        "011011100110",
        "110100011011",
        "001000001010",
        "011011101000",
        "110011001000",
        "001011101010",
        "100000001000",
        "001000000111",
        "111110110101",
        "101000100110",
        "110101110001",
        "101101010111",
        "000010001001",
        "101110010100",
        "101000101011",
        "010110100011",
        "110010011001",
        "110110011111",
        "100111000001",
        "100101010011",
        "101011011001",
        "111101110010",
        "000111000001",
    )

    @Test
    fun `example input`() {
        val diagnostics = exampleInput.parseDiagnosticReport().diagnostics

        assertThat(Integer.toBinaryString(diagnostics.gamma)).isEqualTo("101011000011")
    }

    @Test
    fun `puzzle solution`() {
        val lines = readLinesFromResource("/puzzle03/input")
        val diagnostics = lines.parseDiagnosticReport().diagnostics

        assertThat(Integer.toBinaryString(diagnostics.gamma)).isEqualTo("100111000110")
        assertThat(Integer.toBinaryString(diagnostics.epsilon)).isEqualTo("11000111001")
        assertThat(diagnostics.powerConsumption).isEqualTo(3985686)
    }
}