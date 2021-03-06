package minesweeper.domain.directions

import minesweeper.domain.block.Position
import java.lang.Math.addExact

enum class MineSearchDirections(private val x: Int, private val y: Int) : Directions {
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1);

    override fun nextPosition(position: Position): Position =
        Position(addExact(this.x, position.x), addExact(this.y, position.y))
}
