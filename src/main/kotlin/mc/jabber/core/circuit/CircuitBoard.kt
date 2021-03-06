package mc.jabber.core.circuit

import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.math.Vec2I

data class CircuitBoard(val sizeX: Int, val sizeY: Int) {
    init {
        assert(sizeY > 0) { "Attempted to create a CircuitBoard with a negative or 0 amount of rows! ($sizeY)" }
        assert(sizeX > 0) { "Attempted to create a CircuitBoard with a negative or 0 amount of columns! ($sizeX)" }
    }

    @PublishedApi
    internal val board = Array<Array<ChipProcess?>>(sizeY) { Array(sizeX) { null } }

    operator fun get(x: Int, y: Int): ChipProcess? = board[y][x]
    operator fun get(vec: Vec2I): ChipProcess? = get(vec.x, vec.y)

    operator fun set(vec: Vec2I, process: ChipProcess?) {
        board[vec.y][vec.x] = process
    }

    operator fun set(x: Int, y: Int, process: ChipProcess?) {
        board[y][x] = process
    }

    fun isInBounds(x: Int, y: Int): Boolean = (x >= 0 && y >= 0) && (x < sizeX && y < sizeY)
    fun isInBounds(vec: Vec2I) = isInBounds(vec.x, vec.y)

    override fun toString(): String {
        return buildString {
            for (y in 0 until sizeY) {
                for (x in 0 until sizeX) {
                    append("|${if (get(x, y) == null) " " else "x"}|")
                }
                append('\n')
            }
            setLength(length - 1)
        }
    }

    inline fun forEach(action: (Vec2I, ChipProcess) -> Unit) {
        board.forEachIndexed { y, array ->
            array.forEachIndexed { x, chipProcess ->
                if (chipProcess != null) {
                    action(Vec2I(x, y), chipProcess)
                }
            }
        }
    }

    inline fun forEachInput(action: (Vec2I, ChipProcess) -> Unit) {
        forEach { vec2I, chipProcess ->
            if (chipProcess.isInput) {
                action(vec2I, chipProcess)
            }
        }
    }

    fun longHashCode(): Long {
        var result = sizeX.toLong()
        result = 31 * result + sizeY
        result = 31 * result + board.contentDeepHashCode()
        return result
    }
}
