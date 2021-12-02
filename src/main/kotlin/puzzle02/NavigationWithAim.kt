package puzzle02

data class NavigationWithAim(val horizontalPosition: Int = 0, val verticalPosition: Int = 0, val aim: Int = 0) {

    val positionSum = horizontalPosition * verticalPosition

    fun move(navigation: NavigationInstruction) = NavigationWithAim(
        horizontalPosition = horizontalPosition + navigation.horizontal,
        verticalPosition = verticalPosition + aim * navigation.horizontal,
        aim = aim + navigation.vertical
    )
}