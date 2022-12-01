package twentytwenty

import java.util.concurrent.ConcurrentHashMap

data class BagDescriptor(val color: String)

data class BagPolicy(val descriptor: BagDescriptor, val quantity: Int)

// mapOf<String, List<BagPolicy>>

// used to help assemble the map... will run into issue because don't ever know which ones have roots but OH WELL!
// could use some other map to track which BagDescriptors are guaranteed children but idgaf rn
val lookupTable: MutableMap<BagDescriptor, List<BagPolicy>> = ConcurrentHashMap()

object Day7 {
    fun part1(input: List<String>): Int {
        val graph = mutableMapOf<BagDescriptor, List<BagPolicy>>()
        input.map { parseLineIntoBagNode(it) }
            .forEach { graph[it.first] = it.second }

        val visited = mutableMapOf<BagDescriptor, Boolean>()
        val target = BagDescriptor("shiny gold")
        for (key in graph.keys) {
            doesNodeExistInBag(key, target, graph, visited)
        }

        return visited.filter { it.value }.count() - 1 // hacky, subtract one for the target being present
    }

    fun parseLineIntoBagNode(line: String): Pair<BagDescriptor, List<BagPolicy>> {
        val (bagName, policyString) = line.split(" bags contain ").let { it[0] to it[1].replace(".", "") }

        if (policyString == "no other bags") return BagDescriptor(color = bagName) to listOf()
        val children = policyString.split(", ").map {
            val matches = """(\d+) (\w+ \w+) bags?""".toRegex().matchEntire(it)?.groupValues ?: error("could not match string: $it")
            BagPolicy(descriptor = BagDescriptor(matches[2]), quantity = matches[1].toInt())
        }

        return BagDescriptor(color = bagName) to children
    }

    private fun doesNodeExistInBag(
        current: BagDescriptor,
        target: BagDescriptor,
        graph: Map<BagDescriptor, List<BagPolicy>>,
        visited: MutableMap<BagDescriptor, Boolean>
    ): Boolean {
        if (visited.containsKey(current)) return visited[current]!!

        if (current == target) {
            visited[current] = true
            return true
        }

        val doesItExistSomewhereBelow = (graph[current] ?: error(""))
            .any { doesNodeExistInBag(it.descriptor, target, graph, visited) }

        visited[current] = doesItExistSomewhereBelow

        return doesItExistSomewhereBelow
    }

    fun part2(input: List<String>): Int {
        val graph = input.map { parseLineIntoBagNode(it) }.toMap()
        val target = BagDescriptor("shiny gold")

        return calcNumberOfBagsPerSingle(target, graph) - 1 // for the gold bag >:(
    }

    private fun calcNumberOfBagsPerSingle(
        bag: BagDescriptor,
        graph: Map<BagDescriptor, List<BagPolicy>>
    ): Int {
        val bagsPolicies = graph[bag] ?: error("Could not find bag type $bag")

        // current bag + all bags inside of it
        return 1 + bagsPolicies
            .map { it.quantity * calcNumberOfBagsPerSingle(it.descriptor, graph) }
            .sum()
    }
}

fun main() {
    val fileInput = readFile(day = 7, part = 1)
    val part1Result = Day7.part1(fileInput) // should be 261
    val part2Result = Day7.part2(fileInput) // should be 3765

    println("""
        part 1: $part1Result
        part 2: $part2Result
    """.trimIndent())

}
