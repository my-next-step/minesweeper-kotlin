package minesweeper.ui

import global.strategy.OutputStrategy
import minesweeper.domain.block.Block
import minesweeper.domain.Board
import minesweeper.domain.block.Cell
import minesweeper.domain.block.Mines
import minesweeper.domain.block.Position
import minesweeper.ui.ResultView.Companion.Mark.CELL
import minesweeper.ui.ResultView.Companion.Mark.MINES

class ResultView(private val outputStrategy: OutputStrategy) {

    fun startGame(board: Board) {
        outputStrategy.execute(START_GAME)
        val stringBuilder = board.blocks.fold(StringBuilder()) { sb, block -> sb.append(blockMapToMark(block)) }
        outputStrategy.execute(stringBuilder.toString())
    }

    private fun blockMapToMark(block: Block): String =
        when (block) {
            is Mines -> calculatePrefixNewLine(block.position, MINES)
            is Cell -> calculatePrefixNewLine(block.position, CELL)
        }

    private fun calculatePrefixNewLine(position: Position, mark: String): String {
        if (position.y == 0) {
            return "\n" + mark
        }
        return mark
    }

    companion object {
        private const val START_GAME = "지뢰찾기 게임 시작"

        object Mark {
            const val MINES = "*"
            const val CELL = "C"
        }
    }
}
