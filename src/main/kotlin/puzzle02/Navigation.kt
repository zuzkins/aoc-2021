package puzzle02

data class Navigation(val horizontalPosition: Int = 0, val verticalPosition: Int = 0) {
    fun move(instruction: NavigationInstruction) = copy(
        horizontalPosition = horizontalPosition + instruction.horizontal,
        verticalPosition = verticalPosition + instruction.vertical
    )

    val positionSum = horizontalPosition * verticalPosition

    companion object {
        fun List<NavigationInstruction>.move() = this.fold(Navigation()) { nav, instruction ->
            nav.move(instruction)
        }

        fun List<NavigationInstruction>.moveWithAim() = this.fold(NavigationWithAim()) { nav, instruction ->
            nav.move(instruction)
        }
    }
}

data class NavigationInstruction(val horizontal: Int = 0, val vertical: Int = 0)

object NavigationUtils {
    fun List<String>.parseInstructions() = this.map(::parseInstruction)

    private fun parseInstruction(instruction: String): NavigationInstruction {
        val (direction, distanceStr) = instruction.split(" ")
        val distance = distanceStr.toInt()
        return when (direction) {
            "forward" -> NavigationInstruction(horizontal = distance)
            "down" -> NavigationInstruction(vertical = distance)
            "up" -> NavigationInstruction(vertical = -distance)
            else -> error("Could not parse navigation instruction '${instruction}'")
        }
    }
}