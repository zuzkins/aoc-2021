package puzzle12

object PathFinding {

    fun Map<String, Collection<String>>.paths(): List<String> {
        return findPaths(
            connections = this,
            point = "start",
            currentPath = emptyList()
        ).filterNot { it.isEmpty() }.map { it.joinToString(",") }
    }

    private fun findPaths(
        connections: Map<String, Collection<String>>,
        point: String,
        currentPath: List<String>
    ): List<List<String>> {
        val newPath = currentPath + point
        if (point == "end") {
            return listOf(listOf(newPath.joinToString(",")))
        }
        // we already have been in this small cave so this path is no good
        if (point != "start" && point.all { it.isLowerCase() } && currentPath.contains(point)) {
            return emptyList()
        }
        val conns = connections[point] ?: error("Cannot move from point $point at path: $newPath")
        val allConnections = conns.flatMap { nextPoint ->
            findPaths(connections, nextPoint, newPath)
        }
        return allConnections
    }

    fun parse(input: String) = input.lines().fold(mapOf<String, List<String>>()) { connections, line ->
        val (from, to) = line.trim().split("-")
        val added = addConnection(connections, from, to)
        addConnection(added, to, from)
    }

    private fun addConnection(
        connections: Map<String, List<String>>,
        from: String,
        to: String
    ): Map<String, List<String>> {
        if (to == "start") {
            return connections
        }
        if (from == "end") {
            return connections
        }
        val currentConnections = connections.getOrDefault(from, emptyList())
        val newConnections = currentConnections + to
        return connections + (from to newConnections)
    }
}