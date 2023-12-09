package twentytwentythree


object Day08 {
    val input: List<String> = Day08::class.java.getResource("/twentytwentythree/day08pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        val (desertMap, _) = parseInput(input)

        var isDone = false
        var numIterations = 0
        var currentNode = desertMap.nodes.getValue("AAA")
        do {
            for (instruction in desertMap.instructions) {
                if (instruction == 'R') {
                    currentNode = desertMap.nodes.getValue(currentNode.right)
                } else if (instruction == 'L') {
                    currentNode = desertMap.nodes.getValue(currentNode.left)
                } else {
                    throw IllegalStateException("unexpected instruction $instruction")
                }
            }

            if (currentNode.id.endsWith("Z")) {
                isDone = true
            }
            numIterations++
        } while (!isDone)
        return (numIterations * desertMap.instructions.length).toLong()
    }

    fun parseInput(input: List<String>): Pair<DesertMap, String> {
        // line 0 is the rlrl instructions
        val instructions = input[0]
        val nodes = input.drop(1).map {
            val regex = """(?<id>\w+) = \((?<left>\w+), (?<right>\w+)\)""".toRegex()
            val match = regex.matchEntire(it) ?: throw IllegalStateException("error parsing $it")
            val id = match.groups["id"]!!.value
            val left = match.groups["left"]!!.value
            val right = match.groups["right"]!!.value
            Node(id, left, right)
        }

        val nodeMap = mutableMapOf<String, Node>()
        for (node in nodes) {
            nodeMap[node.id] = node
        }

        return DesertMap(instructions, nodeMap) to nodes[0].id
    }

    data class DesertMap(val instructions: String, val nodes: Map<String, Node>)
    data class Node(val id: String, val left: String, val right: String)

    fun part2(input: List<String>): Long {
        val (desertMap, _) = parseInput(input)

        val nodesThatStartWithA = desertMap.nodes.map { it.key }.filter { it.last() == 'A' }
        var currentNodes = nodesThatStartWithA.map {
            desertMap.nodes.getValue(it)
        }.toMutableList()

        println("number of nodes processing: ${currentNodes.size} - $currentNodes")

        var isDone = false
        var numIterations = 0
        var numSteps = 0

        var idToLeastCommonMultiple = mutableMapOf<String, Long>()
        do {
            numIterations++
            for (instruction in desertMap.instructions) {
                numSteps++
                for (index in currentNodes.indices) {
                    val node = currentNodes[index]
                    if (node.id.last() == 'Z') continue

                    currentNodes[index] = when (instruction) {
                        'R' -> {
                            desertMap.nodes.getValue(node.right)
                        }
                        'L' -> {
                            desertMap.nodes.getValue(node.left)
                        }
                        else -> {
                            throw IllegalStateException("unexpected instruction $instruction")
                        }
                    }
                }
            }

            if (numIterations % 10000 == 0) {
                println("checking in at $numIterations - $currentNodes")
            }

            currentNodes.filter { it.id.last() == 'Z' }.forEach {
                idToLeastCommonMultiple.putIfAbsent(it.id, numIterations.toLong())
            }

            isDone = currentNodes.all { it.id.last() == 'Z' }
        } while (!isDone)

        println("find least common multiple online from: ${idToLeastCommonMultiple.values} - then multiply by ${numSteps * desertMap.instructions.length}")

        return -1
    }
}


fun main() {
    val part1 = Day08.part1(Day08.input)
    val part2 = Day08.part2(Day08.input)

    println("part1 result: $part1")
    println("part2 result: $part2")
}
