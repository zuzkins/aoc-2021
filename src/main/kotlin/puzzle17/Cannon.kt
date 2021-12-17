package puzzle17

import puzzle05.Point
import java.util.*
import kotlin.math.sign

data class Target(val x: IntRange, val y: IntRange) {

    fun hit(probeX: Int, probeY: Int): Boolean = probeX in x && probeY in y
}

object Cannon {

    fun Target.shootWith(vx: Int, vy: Int): List<Point> {
        var curVx = vx
        var curVy = vy
        var probeX = 0
        var probeY = 0

        val result = LinkedList<Point>()
        while (true) {
            if (this.hit(probeX, probeY)) return result
            probeX += curVx
            probeY += curVy
            curVx = curVx.dragX()
            curVy = curVy.gravity()
            if (probeX > x.last || probeY < y.first) {
                return emptyList()
            }
            result += probeX to probeY
        }
    }

    fun Target.showerThemWithNukes(): List<List<Point>> {
        val result = LinkedList<List<Point>>()
        (0..1000).forEach { initialX ->
            (0..1000).forEach { initialY ->
                val shot = this.shootWith(initialX, initialY)
                if (shot.isNotEmpty()) {
                    result.add(shot)
                }
            }
        }

        return result
    }

    fun List<List<Point>>.highestShot() = maxByOrNull { shot ->
        shot.maxOf { it.second }
    } ?: error("Could not find highest shot of $this")

    fun Int.dragX(): Int {
        if (this == 0) return 0
        val sign = sign(this.toDouble()).toInt()
        val next = this + (sign * -1)
        return next
    }

    fun Int.gravity(): Int = this - 1
}
