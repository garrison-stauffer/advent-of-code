import java.io.BufferedReader
import java.io.FileReader

object DaySix {
    val INPUT_FILE_PATH = "/Users/garrisonstauffer/toast/git-repos/advent-of-code/src/main/resources/DaySixInput.txt"

    fun getNumberOfOrbitsForEntity(entity: Entity, visited: MutableSet<Entity> = mutableSetOf()): Int {
        entity.orbiting.forEach { orbit ->
            visited.add(orbit)
            getNumberOfOrbitsForEntity(orbit, visited)
        }

        return visited.size
    }

    fun calculateDistanceToQueryEntity(start: Entity, query: String): Int {
        val needToVisit = mutableListOf(BFSQueueItem(start, 0))
        val visited = mutableSetOf<Entity>()

        fun Entity.isTargetEntity(): Boolean = this.id == query

        do {
            val queueItem = needToVisit.removeAt(0)
            val currentEntity = queueItem.entity
            val currentDepth = queueItem.depth

            if (visited.contains(currentEntity)) continue
            if (currentEntity.isTargetEntity()) return currentDepth

            currentEntity.orbiting
                .filter { !visited.contains(it) }
                .map { BFSQueueItem(it, currentDepth + 1) }
                .forEach { needToVisit.add(it) }

            currentEntity.orbitedBy
                .filter { !visited.contains(it) }
                .map { BFSQueueItem(it, currentDepth + 1) }
                .forEach { needToVisit.add(it) }

            visited.add(currentEntity)
        } while (needToVisit.isNotEmpty())

        error("Did not find target entity in the graph")
    }

    data class BFSQueueItem(val entity: Entity, val depth: Int)

    fun partOne(entities: List<Entity>): Int {
        return entities.map { getNumberOfOrbitsForEntity(it) }.sum()
    }

}

class Entity(val id: String, val orbiting: MutableList<Entity> = mutableListOf(), val orbitedBy: MutableList<Entity> = mutableListOf()) {
    infix fun orbits(other: Entity) {
        orbiting.add(other)
        other.orbitedBy.add(this)
    }
}

fun main() {
    val reader = BufferedReader(FileReader(DaySix.INPUT_FILE_PATH))

    val entityMap = mutableMapOf<String, Entity>()

    val inputRows = reader.use { it.readLines() }

    fun getEntityOrCreate(label: String): Entity {
        if (entityMap[label] == null) {
            entityMap[label] = Entity(label)
        }
        return entityMap[label] ?: error("wat?")
    }

    inputRows.forEach {
        val labels: List<String> = it.split(")")

        val entityA = getEntityOrCreate(labels[0])
        val entityB = getEntityOrCreate(labels[1])

        entityB orbits entityA
    }

    println(DaySix.partOne(entityMap.values.toList()))

    println(DaySix.calculateDistanceToQueryEntity(entityMap["YOU"]!!, "SAN") - 2) // arbitrary distance, because it isn't how many jumps to get to santa, its how many orbits I need to change

}

