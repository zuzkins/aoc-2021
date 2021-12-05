package puzzle05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MapUtilTest {

    @Test
    fun `can parse point`() {
        assertThat(MapUtil.parsePoint("32,197")).isEqualTo(Point(32, 197))
    }

    @Test
    fun `can parse points`() {
        assertThat(MapUtil.parsePoints("32,197 -> 32,891")).isEqualTo(Point(32, 197) to Point(32, 891))
    }
}