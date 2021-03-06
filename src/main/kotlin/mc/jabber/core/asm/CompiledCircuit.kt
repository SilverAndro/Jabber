package mc.jabber.core.asm

import mc.jabber.core.data.CircuitDataStorage
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Basic interface for runtime compiled circuits
 */
interface CompiledCircuit {
    // Simulation / management
    fun setup()
    fun simulate(context: ExecutionContext?, memory: LongArray)

    // Serializtion
    fun getChipStorage(): HashMap<Vec2I, NbtTransformable<*>>
    fun getChipState(): CircuitDataStorage

    // De-serialization
    fun stateFrom(chipStorage: HashMap<Vec2I, NbtTransformable<*>>, chipState: CircuitDataStorage)
}
