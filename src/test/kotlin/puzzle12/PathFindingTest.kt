package puzzle12

import Utils.readResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import puzzle12.PathFinding.paths

internal class PathFindingTest {
    @Test
    fun `can parse simple connection`() {
        assertThat(PathFinding.parse("start-A")).isEqualTo(mapOf("start" to listOf("A")))
    }

    val simplePath = """
        start-A
        A-end
        """.trimIndent()

    @Test
    fun `can parse simple navigation`() {
        assertThat(PathFinding.parse(simplePath)).isEqualTo(mapOf("start" to listOf("A"), "A" to listOf("end")))
    }

    @Test
    fun `can find simple route`() {
        val connections = PathFinding.parse(simplePath)
        assertThat(connections.paths()).isEqualTo(listOf("start,A,end"))
    }

    @Test
    fun `can only visit small caves once`() {
        val connections = PathFinding.parse(
            """
            start-A
            A-b
            b-end
            """.trimIndent()
        )
        assertThat(connections.paths()).isEqualTo(listOf("start,A,b,end"))
    }

    val exampleInput = """
    start-A
    start-b
    A-c
    A-b
    b-d
    A-end
    b-end
    """.trimIndent()

    @Test
    fun `can find all paths in example`() {
        val connections = PathFinding.parse(exampleInput)
        assertThat(connections.paths()).containsAll(
            """
            start,A,b,A,c,A,end
            start,A,b,A,end
            start,A,b,end
            start,A,c,A,b,A,end
            start,A,c,A,b,end
            start,A,c,A,end
            start,A,end
            start,b,A,c,A,end
            start,b,A,end
            start,b,end
            """.trimIndent().lines()
        )
    }

    val biggerExampleInput = """
        dc-end
        HN-start
        start-kj
        dc-start
        dc-HN
        LN-dc
        HN-end
        kj-sa
        kj-HN
        kj-dc
    """.trimIndent()

    @Test
    fun `can solve bigger example`() {
        val connections = PathFinding.parse(biggerExampleInput)
        assertThat(connections.paths()).containsAll(
            """
            start,HN,dc,HN,end
            start,HN,dc,HN,kj,HN,end
            start,HN,dc,end
            start,HN,dc,kj,HN,end
            start,HN,end
            start,HN,kj,HN,dc,HN,end
            start,HN,kj,HN,dc,end
            start,HN,kj,HN,end
            start,HN,kj,dc,HN,end
            start,HN,kj,dc,end
            start,dc,HN,end
            start,dc,HN,kj,HN,end
            start,dc,end
            start,dc,kj,HN,end
            start,kj,HN,dc,HN,end
            start,kj,HN,dc,end
            start,kj,HN,end
            start,kj,dc,HN,end
            start,kj,dc,end
            """.trimIndent().lines()
        )
    }

    val biggestExample = """
        fs-end
        he-DX
        fs-he
        start-DX
        pj-DX
        end-zg
        zg-sl
        zg-pj
        pj-he
        RW-he
        fs-DX
        pj-RW
        zg-RW
        start-pj
        he-WI
        zg-he
        pj-fs
        start-RW
        """.trimIndent()

    @Test
    fun `can solve biggest example`() {
        val connections = PathFinding.parse(biggestExample)
        assertThat(connections.paths()).hasSize(226)
    }

    val puzzleInput by lazy { readResource("/puzzle12/input") }

    @Test
    fun `puzzle solution 1`() {
        val connections = PathFinding.parse(puzzleInput)
        assertThat(connections.paths()).hasSize(5333)
    }

    @Test
    fun `can revisit small cave`() {
        val connections = PathFinding.parse(exampleInput)
        assertThat(connections.paths(allowSmallCaveRevisit = true)).containsAll(
            """
            start,A,b,A,b,A,c,A,end
            start,A,b,A,b,A,end
            start,A,b,A,b,end
            start,A,b,A,c,A,b,A,end
            start,A,b,A,c,A,b,end
            start,A,b,A,c,A,c,A,end
            start,A,b,A,c,A,end
            start,A,b,A,end
            start,A,b,d,b,A,c,A,end
            start,A,b,d,b,A,end
            start,A,b,d,b,end
            start,A,b,end
            start,A,c,A,b,A,b,A,end
            start,A,c,A,b,A,b,end
            start,A,c,A,b,A,c,A,end
            start,A,c,A,b,A,end
            start,A,c,A,b,d,b,A,end
            start,A,c,A,b,d,b,end
            start,A,c,A,b,end
            start,A,c,A,c,A,b,A,end
            start,A,c,A,c,A,b,end
            start,A,c,A,c,A,end
            start,A,c,A,end
            start,A,end
            start,b,A,b,A,c,A,end
            start,b,A,b,A,end
            start,b,A,b,end
            start,b,A,c,A,b,A,end
            start,b,A,c,A,b,end
            start,b,A,c,A,c,A,end
            start,b,A,c,A,end
            start,b,A,end
            start,b,d,b,A,c,A,end
            start,b,d,b,A,end
            start,b,d,b,end
            start,b,end
            """.trimIndent().lines()
        )
    }

    @Test
    fun `puzzle solution 2`() {
        val connections = PathFinding.parse(puzzleInput)
        val paths = connections.paths(allowSmallCaveRevisit = true)
        assertThat(paths).hasSize(146553)
    }
}