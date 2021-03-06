package mc.jabber.core.chips.special

import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

/**
 * A custom chip process, mostly for quick testing or debugging chips
 *
 * Also, highly useful in unit tests as to not accidentally try to load the registry
 *
 * Does not support making initial entities / storage, also always receives and sends on all
 *
 * @see ChipProcess
 */
class CustomChip(
    override val id: Identifier,
    override val isInput: Boolean = false,
    override val receiveDirections: DirBitmask = DirBitmask.ALL,
    override val sendDirections: DirBitmask = DirBitmask.ALL,
    buildParams: ChipParams,
    private val method: (CardinalData, Vec2I, HashMap<Vec2I, NbtTransformable<*>>) -> CardinalData
) : ChipProcess(buildParams) {
    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        return method(data, pos, chipData)
    }
}
