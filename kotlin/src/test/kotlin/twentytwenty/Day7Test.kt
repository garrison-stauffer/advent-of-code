package twentytwenty

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day7Test {
    val testFileInput = this.javaClass.classLoader.getResource("twentytwenty/day7test.txt").readText().lines().filter { it.isNotBlank() }

    @Test
    fun `parse line with many bags`() {
        val testString = "dark orange bags contain 3 bright white bags, 4 muted yellow bags."

        val result = Day7.parseLineIntoBagNode(testString)
        assertThat(result).isEqualTo(
            BagDescriptor(color = "dark orange") to listOf(
                BagPolicy(BagDescriptor(color = "bright white"), 3),
                BagPolicy(BagDescriptor(color = "muted yellow"), 4)
            )
        )
    }

    @Test
    fun `parse line with 1 bag case`() {
        // important thing to note is the plurality, 1 = bag, 2+ = bags
        val testString = "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags."
        val result = Day7.parseLineIntoBagNode(testString)
        assertThat(result).isEqualTo(
            BagDescriptor(color = "shiny gold") to listOf(
                BagPolicy(BagDescriptor(color = "dark olive"), 1),
                BagPolicy(BagDescriptor(color = "vibrant plum"), 2)
            )
        )
    }

    @Test
    fun `parse line with no bags inside case`() {
        val testString = "dotted black bags contain no other bags."
        val result = Day7.parseLineIntoBagNode(testString)
        assertThat(result).isEqualTo(
            BagDescriptor(color = "dotted black") to listOf<BagPolicy>())
    }

    @Test
    fun `test example for part 1`() {
        val result = Day7.part1(testFileInput)
        assertThat(result).isEqualTo(4)
    }

    @Test
    fun `test example for part 2`() {
        val testInput = listOf(
            "shiny gold bags contain 2 dark red bags.",
            "dark red bags contain 2 dark orange bags.",
            "dark orange bags contain 2 dark yellow bags.",
            "dark yellow bags contain 2 dark green bags.",
            "dark green bags contain 2 dark blue bags.",
            "dark blue bags contain 2 dark violet bags.",
            "dark violet bags contain no other bags."
        )

        val result = Day7.part2(testInput)

        assertThat(result).isEqualTo(126)
    }
}
