package minesweeper.domain.block

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("좌표(Position)")
internal class PositionTest {

    @ParameterizedTest(name = "입력 값: {0},{1}")
    @CsvSource(value = ["0:0", "10:10", "0:10", "10:0"], delimiter = ':')
    fun `2개 이상의 정수로 이루어진다`(x: Int, y: Int) {
        val position = Position(x, y)

        assertAll(
            { assertThat(position).isNotNull },
            { assertThat(position).isExactlyInstanceOf(Position::class.java) }
        )
    }

    @ParameterizedTest(name = "입력 값: {0},{1}")
    @CsvSource(value = ["-1:0", "-10:0", "0:-10", "-1:-10"], delimiter = ':')
    fun `0미만의 값으로 이루어질 수 없다`(x: Int, y: Int) {
        val exception = assertThrows<InvalidPositionRangeException> { Position(x, y) }

        assertThat(exception.message).isEqualTo("좌표 {x='%s', y='%s'}는 Position 의 범위가 아닙니다.".format(x, y))
    }
}
