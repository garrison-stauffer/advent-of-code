package twentytwentythree

object Day03 {
    val input: List<String> = Day03::class.java.getResource("/twentytwentythree/day03pt1.txt")!!
        .readText()
        .lines()
        .filter(String::isNotBlank)

    fun part1(input: List<String>): Long {
        var sum: Long = 0
        input.forEachIndexed { index, line ->
            println("parsing row $index")
            val hasComponentAt = fun(row: Int, col: Int): Boolean {
                if (row < 0 || row >= input.size || col < 0 || col >= input[row].length) {
                    return false
                }

                return !input[row][col].isDigit() && input[row][col] != '.'
            }

            sum += processLine(line, index, hasComponentAt)
        }

        return sum
    }

    // I can use (row, col) as an id... hmm
    data class Engine(val rows: List<EngineRow>) {
        fun lookupPartNumberAt(row: Int, column: Int): EngineComponent.PartNumber? {
            if (row < 0 || row >= rows.size) return null

            val component = rows[row].components[column]

            return component as? EngineComponent.PartNumber
        }
    }
    data class EngineRow(
        val components: Map<Int, EngineComponent>
    )
    sealed class EngineComponent {
        data class PartNumber(val id: String, val value: Long): EngineComponent()
        object Cog: EngineComponent()
        object OtherPart: EngineComponent()
    }

    fun part2(input: List<String>): Long {
        var lines = mutableListOf<EngineRow>()
        input.forEachIndexed { index, line ->
            lines.add(processLineV2(line, index))
        }
        val engine = Engine(lines)

        val lookups = listOf(
            -1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1
        )

        var gearRatioSummed = 0L
        engine.rows.forEachIndexed { rowIndex, row ->
            row.components.filter { it.value is EngineComponent.Cog }.forEach {(column, cog) ->
                // for each cog, lookup to see if there are 2 around it
                val seen = mutableSetOf<String>()
                var gearRatio = 1L
                lookups.forEach { (rowAdjustment, colAdjustment) ->
                    val partNumber = engine.lookupPartNumberAt(rowIndex + rowAdjustment, column + colAdjustment)
                    if (partNumber != null && seen.add(partNumber.id)) {
                        gearRatio *= partNumber.value
                    }
                }

                if (seen.size == 2) {
                    gearRatioSummed += gearRatio
                }
            }
        }

        return gearRatioSummed
    }

    fun processLine(line: String, row: Int, hasComponentAt: (row: Int, column: Int) -> Boolean): Int {
        var partNumberBuffer = ""
        var bufferHasLinkedComponent = false
        var sum = 0

        val emptyBuffer = fun() {
            if (partNumberBuffer == "") return

            val partNumber = partNumberBuffer.toInt()
            if (bufferHasLinkedComponent) {
                sum += partNumber
            }

            // reset variables
            partNumberBuffer = ""
            bufferHasLinkedComponent = false
        }

        line.forEachIndexed { column, char ->
            if (char.isDigit()) {
                partNumberBuffer += char
                // look up to see if there was a component
                val lookups = listOf(
                    -1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1
                )
                if (!bufferHasLinkedComponent) {
                    bufferHasLinkedComponent = lookups.any { (rowAdjust, colAdjust) ->
                        hasComponentAt(row + rowAdjust, column + colAdjust)
                    }
                }

                return@forEachIndexed
            }

            emptyBuffer()
        }

        emptyBuffer()
        return sum
    }

    fun processLineV2(line: String, row: Int): EngineRow {
        var columnStart = -1
        var partNumberBuffer = ""
        var lines = mutableMapOf<Int, EngineComponent>()

        val emptyBuffer = fun() {
            if (partNumberBuffer == "") return

            val partNumber = partNumberBuffer.toLong()
            val partId = "${row}_$columnStart"
            // add it for each index
            for (index in columnStart until columnStart + partNumberBuffer.length) {
                lines[index] = EngineComponent.PartNumber(partId, partNumber)
            }
            // reset variables
            partNumberBuffer = ""
            columnStart = -1
        }

        line.forEachIndexed { column, char ->
            if (char.isDigit()) {
                partNumberBuffer += char
                if (columnStart < 0) {
                    columnStart = column
                }

                return@forEachIndexed
            }

            if (char == '*') {
                lines[column] = EngineComponent.Cog
            } else if (char != '.') {
                lines[column] = EngineComponent.OtherPart
            }

            emptyBuffer()
        }

        emptyBuffer()
        return EngineRow(lines)
    }
}

fun main() {
    val part1 = Day03.part1(Day03.input)
    val part2 = Day03.part2(Day03.input)

    println("part1 result: $part1") // 520135
    println("part2 result: $part2") // 72514855
}
