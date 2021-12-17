import Utils.intFromBinary
import Utils.longFromBinary
import java.util.*

typealias BinaryString = String

sealed class Packet {
    abstract val version: Int
    abstract val type: Int
    abstract val packetVersionSum: Int

    abstract val sum: Long

    data class SubPacket(
        override val version: Int,
        override val type: Int,
        val packets: List<Packet>,
        val typeId: Int,
    ) : Packet() {

        override val packetVersionSum = version + packets.sumOf { it.packetVersionSum }
        override val sum: Long by lazy {
            when (type) {
                0 -> packets.sumOf { it.sum }
                1 -> packets.fold(1L) { acc, it -> acc * it.sum }
                2 -> packets.minOf { it.sum }
                3 -> packets.maxOf { it.sum }
                5, 6, 7 -> {
                    check(packets.size == 2) {
                        "Packet of type $type expected to have exactly 2 sub packets but has: ${packets.size}"
                    }
                    val (f, s) = packets
                    val comparisonOk = when (type) {
                        5 -> f.sum > s.sum
                        6 -> f.sum < s.sum
                        7 -> f.sum == s.sum
                        else -> error("Unexpected packet type $type")
                    }
                    if (comparisonOk) 1 else 0
                }
                else -> error("Invalid packet type $type")
            }
        }
    }

    data class LiteralValue(override val version: Int, val value: Long) : Packet() {
        override val type = 4
        override val packetVersionSum = version
        override val sum = value
    }
}

object Packets {

    fun parseInput(input: String): BinaryString {
        return input.map { ch ->
            Integer.parseInt(ch.toString(), 16).toString(2).padStart(4, '0')
        }.joinToString("")
    }

    fun BinaryString.isPacketEmpty(): Boolean {
        return this.length < 25 && all { it == '0' }
    }

    fun BinaryString.parse(): List<Packet> {
        if (isPacketEmpty()) {
            return emptyList()
        }

        val (result, rest) = doParse(maxPackets = null)
        check(rest.isPacketEmpty())
        return result
    }

    private fun BinaryString.doParse(maxPackets: Int? = null): Pair<List<Packet>, BinaryString> {
        val result = LinkedList<Packet>()

        var rest = this
        while (!rest.isPacketEmpty() && (maxPackets == null || result.size < maxPackets)) {
            val parseResult = rest.readPacket()
            val packet = parseResult.first
            rest = parseResult.second
            result += packet
        }

        return result to rest
    }

    fun BinaryString.readPacket(): Pair<Packet, BinaryString> {
        val (version, restType) = getVersion()
        val (type, restPacket) = restType.getVersion()
        if (type == 4) {
            val (value, rest) = restPacket.readLiteralValue()
            return Packet.LiteralValue(version = version, value = value) to rest
        } else {
            return restPacket.readSubPacket(version, type)
        }
    }

    fun BinaryString.getVersion(): Pair<Int, BinaryString> {
        return this.take(3).intFromBinary() to this.drop(3)
    }

    fun BinaryString.getType(): Pair<Int, BinaryString> {
        return this.take(3).intFromBinary() to this.drop(3)
    }

    fun BinaryString.readLiteralValue(): Pair<Long, BinaryString> {
        var result = ""
        var lastSegment = false
        var rest = this
        while (!lastSegment) {
            val segment = rest.take(5)
            rest = rest.drop(5)
            lastSegment = segment.first() == '0'
            result += segment.drop(1)
        }

        return result.longFromBinary() to rest
    }

    fun BinaryString.readSubPacket(version: Int, type: Int): Pair<Packet, BinaryString> {
        val typeId = this.take(1)
        val restTypeId = this.drop(1)
        return if (typeId == "0") {
            restTypeId.parseBitSubPacket(version, type, typeId.toInt())
        } else if (typeId == "1") {
            restTypeId.parseLenSubPacket(version, type, typeId.toInt())
        } else {
            error("can only parse sub packets '0'")
        }
    }

    private fun BinaryString.parseBitSubPacket(
        version: Int,
        type: Int,
        typeId: Int,
    ): Pair<Packet.SubPacket, BinaryString> {
        val bitLen = take(15).intFromBinary()
        val packetsBase = drop(15).take(bitLen)
        val restPacket = drop(15 + bitLen)
        val packets = packetsBase.parse()
        return Packet.SubPacket(
            version = version,
            type = type,
            typeId = typeId,
            packets = packets
        ) to restPacket
    }

    private fun BinaryString.parseLenSubPacket(
        version: Int,
        type: Int,
        typeId: Int,
    ): Pair<Packet.SubPacket, BinaryString> {
        val packetCount = take(11).intFromBinary()
        val restPacket = drop(11)
        val (packets, rest) = restPacket.doParse(maxPackets = packetCount)
        return Packet.SubPacket(
            version = version,
            type = type,
            typeId = typeId,
            packets = packets
        ) to rest
    }
}
