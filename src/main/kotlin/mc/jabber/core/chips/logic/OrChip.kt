package mc.jabber.core.chips.logic

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Preforms a logical OR on input, outputs only on not received channels
 */
@ChipID("chip_or", "OR")
class OrChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val id = Global.id("or")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        return if (data.any { _, t -> t != null && t != 0L }) {
            data.outputNotReceived(1)
        } else {
            data.outputNotReceived(0)
        }
    }
}
