package puzzle16

import Packet
import Packets
import Packets.parse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PacketsTest {

    @Test
    fun `can return binary form`() {
        assertThat(Packets.parseInput("D2FE28")).isEqualTo("110100101111111000101000")
    }

    @Test
    fun `can parse literal value packet`() {
        val packets = "110100101111111000101000".parse()
        assertThat(packets).hasSize(1)
        assertThat(packets.first()).isEqualTo(Packet.LiteralValue(version = 6, value = 2021))
    }

    @Test
    fun `can parse bits encoded sub packet`() {
        val packets = "00111000000000000110111101000101001010010001001000000000".parse()
        assertThat(packets).hasSize(1)
        assertThat(packets.first()).isEqualTo(
            Packet.SubPacket(
                version = 1,
                type = 6,
                typeId = 0,
                packets = listOf(
                    Packet.LiteralValue(version = 6, value = 10),
                    Packet.LiteralValue(version = 2, value = 20),
                )
            )
        )
    }

    @Test
    fun `can parse count encoded sub packet`() {
        val packets = "11101110000000001101010000001100100000100011000001100000".parse()
        assertThat(packets).hasSize(1)
        assertThat(packets.first()).isEqualTo(
            Packet.SubPacket(
                version = 7,
                type = 3,
                typeId = 1,
                packets = listOf(
                    Packet.LiteralValue(version = 2, value = 1),
                    Packet.LiteralValue(version = 4, value = 2),
                    Packet.LiteralValue(version = 1, value = 3),
                )
            )
        )
    }

    @Test
    fun `can parse example sub packet 1`() {
        val packets = parseWholePacket("8A004A801A8002F478")
        assertThat(packets).isEqualTo(
            listOf(
                Packet.SubPacket(
                    version = 4,
                    type = 2,
                    typeId = 1,
                    packets = listOf(
                        Packet.SubPacket(
                            version = 1,
                            type = 2,
                            typeId = 1,
                            packets = listOf(
                                Packet.SubPacket(
                                    version = 5,
                                    type = 2,
                                    typeId = 0,
                                    packets = listOf(Packet.LiteralValue(version = 6, value = 15))
                                )
                            )
                        )
                    )
                )
            )
        )

        assertThat(packets.sumOf { it.packetVersionSum }).isEqualTo(16)
    }

    @Test
    fun `can parse example sub packet 2`() {
        val packets = parseWholePacket("620080001611562C8802118E34")
        assertThat(packets.sumOf { it.packetVersionSum }).isEqualTo(12)
    }

    @Test
    fun `can parse example sub packet 3`() {
        val packets = parseWholePacket("C0015000016115A2E0802F182340")
        assertThat(packets.sumOf { it.packetVersionSum }).isEqualTo(23)
    }

    @Test
    fun `can parse example sub packet 4`() {
        val packets = parseWholePacket("A0016C880162017C3686B18A3D4780")
        assertThat(packets.sumOf { it.packetVersionSum }).isEqualTo(31)
    }

    val puzzleInput =
        "005532447836402684AC7AB3801A800021F0961146B1007A1147C89440294D005C12D2A7BC992D3F4E50C72CDF29EECFD0ACD5CC016962099194002CE31C5D3005F401296CAF4B656A46B2DE5588015C913D8653A3A001B9C3C93D7AC672F4FF78C136532E6E0007FCDFA975A3004B002E69EC4FD2D32CDF3FFDDAF01C91FCA7B41700263818025A00B48DEF3DFB89D26C3281A200F4C5AF57582527BC1890042DE00B4B324DBA4FAFCE473EF7CC0802B59DA28580212B3BD99A78C8004EC300761DC128EE40086C4F8E50F0C01882D0FE29900A01C01C2C96F38FCBB3E18C96F38FCBB3E1BCC57E2AA0154EDEC45096712A64A2520C6401A9E80213D98562653D98562612A06C0143CB03C529B5D9FD87CBA64F88CA439EC5BB299718023800D3CE7A935F9EA884F5EFAE9E10079125AF39E80212330F93EC7DAD7A9D5C4002A24A806A0062019B6600730173640575A0147C60070011FCA005000F7080385800CBEE006800A30C023520077A401840004BAC00D7A001FB31AAD10CC016923DA00686769E019DA780D0022394854167C2A56FB75200D33801F696D5B922F98B68B64E02460054CAE900949401BB80021D0562344E00042A16C6B8253000600B78020200E44386B068401E8391661C4E14B804D3B6B27CFE98E73BCF55B65762C402768803F09620419100661EC2A8CE0008741A83917CC024970D9E718DD341640259D80200008444D8F713C401D88310E2EC9F20F3330E059009118019A8803F12A0FC6E1006E3744183D27312200D4AC01693F5A131C93F5A131C970D6008867379CD3221289B13D402492EE377917CACEDB3695AD61C939C7C10082597E3740E857396499EA31980293F4FD206B40123CEE27CFB64D5E57B9ACC7F993D9495444001C998E66B50896B0B90050D34DF3295289128E73070E00A4E7A389224323005E801049351952694C000"

    @Test
    fun `puzzle solution 1`() {
        val packets = parseWholePacket(puzzleInput)
        assertThat(packets.sumOf { it.packetVersionSum }).isEqualTo(981)
    }

    @Test
    fun `example sum 1`() {
        assertThat(parseWholePacket("C200B40A82").sum).isEqualTo(3)
    }

    @Test
    fun `example sum 2`() {
        assertThat(parseWholePacket("04005AC33890").sum).isEqualTo(54)
    }

    @Test
    fun `example sum 3`() {
        assertThat(parseWholePacket("880086C3E88112").sum).isEqualTo(7)
    }

    @Test
    fun `example sum 4`() {
        assertThat(parseWholePacket("CE00C43D881120").sum).isEqualTo(9)
    }

    @Test
    fun `example sum 5`() {
        assertThat(parseWholePacket("D8005AC2A8F0").sum).isEqualTo(1)
    }

    @Test
    fun `example sum 6`() {
        assertThat(parseWholePacket("F600BC2D8F").sum).isEqualTo(0)
    }

    @Test
    fun `example sum 7`() {
        assertThat(parseWholePacket("9C005AC2F8F0").sum).isEqualTo(0)
    }

    @Test
    fun `example sum 8`() {
        assertThat(parseWholePacket("9C0141080250320F1802104A08").sum).isEqualTo(1)
    }

    @Test
    fun `puzzle solution 2`() {
        assertThat(parseWholePacket(puzzleInput).sum).isEqualTo(299227024091)
    }

    private fun parseWholePacket(input: String) = Packets.parseInput(input).parse()

    private val List<Packet>.sum get() = sumOf { it.sum }
}
