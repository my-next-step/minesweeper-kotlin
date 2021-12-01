package minesweeper.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("높이(Height)")
internal class HeightTest {

    @ParameterizedTest(name = "입력 값: {0}")
    @ValueSource(ints = [1, 10, 100, 1000, Integer.MAX_VALUE])
    fun `1이상의 숫자로 이루어진다`(heightInt: Int) {
        val height = Height(heightInt)

        assertAll(
            { assertThat(height).isNotNull },
            { assertThat(height).isExactlyInstanceOf(Height::class.java) },
        )
    }

    @ParameterizedTest(name = "입력 값: {0}")
    @ValueSource(ints = [0, -1, -10, -100, Integer.MIN_VALUE])
    fun `0이하의 값으로 이루어질 수 없다`() {
        val exception = assertThrows<InvalidHeightRangeException> { Height(0) }

        assertThat(exception.messgae).isEqualTo("'%s'는 올바른 Height 의 범위가 아닙니다.")
    }
}
