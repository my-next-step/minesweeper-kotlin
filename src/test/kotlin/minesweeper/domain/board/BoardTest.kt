package minesweeper.domain.board

import minesweeper.domain.area.Area
import minesweeper.domain.area.Height
import minesweeper.domain.area.Width
import minesweeper.domain.block.Block
import minesweeper.domain.block.Blocks
import minesweeper.domain.block.EmptyBlock
import minesweeper.domain.block.MineBlock
import minesweeper.domain.block.Position
import minesweeper.domain.block.state.Opened
import minesweeper.domain.board.state.Lose
import minesweeper.domain.board.state.Win
import minesweeper.exception.MinesCountOverAreaException
import minesweeper.fixture.BoardFixture
import minesweeper.fixture.BoardFixture.TEST_MINE_BLOCK_GENERATE_STRATEGY
import minesweeper.fixture.BoardFixture.createPositions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("보드(Board)")
internal class BoardTest {

    @ParameterizedTest(name = "입력 값: {0}, {1}, {2}")
    @CsvSource(value = ["1:1:1", "10:1:10", "1:10:1", "10:10:1"], delimiter = ':')
    fun `넓이와 지뢰수 그리고 생성 전략을 통해 생성할 수 있다`(widthInt: Int, heightInt: Int, minesCountInt: Int) {
        val area = Area(Width(widthInt), Height(heightInt))
        val minesCount = MineCount(minesCountInt)
        val strategy = TEST_MINE_BLOCK_GENERATE_STRATEGY

        val positions = createPositions(area.width, area.height)
        val minesPositions = strategy.generate(positions, minesCountInt)
        val expected = Blocks(positions.associateWith { Block.create(it in minesPositions) })

        val board = Board.of(area, minesCount, strategy)
        assertThat(board.blocks).isEqualTo((expected))
    }

    @ParameterizedTest(name = "입력 값: {0}, {1}, {2}")
    @CsvSource(value = ["1:1:2", "10:1:11", "1:10:11", "10:10:101"], delimiter = ':')
    fun `지뢰수가 넓이 보다 클 수 없다`(widthInt: Int, heightInt: Int, minesCountInt: Int) {
        val area = Area(Width(widthInt), Height(heightInt))
        val minesCount = MineCount(minesCountInt)

        val exception =
            assertThrows<MinesCountOverAreaException> { Board.of(area, minesCount, TEST_MINE_BLOCK_GENERATE_STRATEGY) }
        assertThat(exception.message).isEqualTo(
            "'%s'는 area 의 크기(%s)를 넘었습니다.".format(minesCountInt, widthInt * heightInt)
        )
    }

    @ParameterizedTest(name = "입력 값: {0}, {1}")
    @CsvSource(value = ["1:1", "1:2", "1:3"], delimiter = ':')
    fun `지뢰를 클릭하면 null을 반환한다`(clickX: Int, clickY: Int) {
        val board = BoardFixture.createBoard(3, 3, 3, TEST_MINE_BLOCK_GENERATE_STRATEGY)
        val expected = Board(board.blocks, Lose)
        val clickPosition = Position(clickX, clickY)
        val actual = board.open(clickPosition)

        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest(name = "입력 값: {0}, {1}")
    @CsvSource(value = ["3:1", "3:2", "3:3"], delimiter = ':')
    fun `주변 지뢰개수가 0인 영역을 클릭하면 영역 주변이 열린다`(clickX: Int, clickY: Int) {
        val board = BoardFixture.createBoard(3, 3, 3, TEST_MINE_BLOCK_GENERATE_STRATEGY)
        val clickPosition = Position(clickX, clickY)
        val actual = board.open(clickPosition)

        val expected = Board(
            Blocks(
                mapOf(
                    Position(1, 1) to MineBlock(),
                    Position(1, 2) to MineBlock(),
                    Position(1, 3) to MineBlock(),
                    Position(2, 1) to EmptyBlock(Opened),
                    Position(2, 2) to EmptyBlock(Opened),
                    Position(2, 3) to EmptyBlock(Opened),
                    Position(3, 1) to EmptyBlock(Opened),
                    Position(3, 2) to EmptyBlock(Opened),
                    Position(3, 3) to EmptyBlock(Opened),
                )
            ),
            Win
        )
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest(name = "입력 값: {0}, {1}")
    @CsvSource(value = ["3:1", "3:2", "3:3"], delimiter = ':')
    fun `지뢰가 아닌 영역을 모두 오픈했다면 종료 상태가 된다`(clickX: Int, clickY: Int) {
        val board = BoardFixture.createBoard(3, 3, 3, TEST_MINE_BLOCK_GENERATE_STRATEGY)
        val clickPosition = Position(clickX, clickY)
        val actual = board.open(clickPosition)

        assertThat(actual!!.isFinish()).isTrue
    }
}
