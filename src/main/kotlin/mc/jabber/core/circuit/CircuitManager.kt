package mc.jabber.core.circuit

import mc.jabber.core.data.CircuitDataStorage
import mc.jabber.core.data.CircuitType
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int) {
    val board = CircuitBoard(sizeX, sizeY)

    val chipData: HashMap<Vec2I, NbtTransformable<*>> = hashMapOf()
    val state = CircuitDataStorage(sizeX, sizeY)
    val stagingMap = CircuitDataStorage(sizeX, sizeY)

    fun setup() {
        board.forEach { vec2I, process ->
            val initialData = process.makeInitialStateEntry()
            if (initialData != null) {
                chipData[vec2I] = initialData
            }
        }
    }

    fun simulate() {
        // Generate input
        val empty = type.templateData.empty()
        board.forEachInput { vec2I, chipProcess ->
            chipProcess.receive(empty, vec2I, chipData).forEach { dir, any ->
                val offset = vec2I + dir

                if (any != null && board.isInBounds(offset) && board[offset] != null) {
                    stagingMap[offset] = empty.with(dir, any)
                }
            }
        }

        // Simulate each state
        state.forEach { vec2I, data ->
            board[vec2I]!!.receive(data, vec2I, chipData).forEach { dir, any ->
                val offset = vec2I + dir

                if (any != null && board.isInBounds(offset) && board[offset] != null) {
                    stagingMap[offset] = data.empty().with(dir, any)
                }
            }
        }

        state.clear()

        // Copy the staged data back in
        stagingMap.forEach { point, data ->
            state[point] = data
        }

        // Delete the old data we copied in
        stagingMap.clear()
    }

    @Suppress("UnstableApiUsage")
    fun toNbt(): NbtCompound {
        val out = NbtCompound()

        val data = NbtList().also { chipData.forEach { (t, u) -> it.add(NbtCompound().also { it.put("p", t.toNbt()); it.put("s", u.toNbt()); it.put("t", NbtByte.of(u.type())) }) } }
        val state = state.toNbt()

        out.put("d", data)
        out.put("s", state)
        out.put("b", board.toNbt())
        return out
    }

    override fun toString(): String {
        return "\nCircuitManager(type=$type)\n$state\n$board"
    }
}
