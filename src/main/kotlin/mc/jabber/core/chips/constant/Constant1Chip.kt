package mc.jabber.core.chips.constant

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_constant_1")
class Constant1Chip : ChipProcess() {
    override val id = Global.id("const1")

    override fun receive(data: CardinalData, pos: Vec2I, chipData: HashMap<Vec2I, NbtTransformable<*>>): CardinalData {
        return CardinalData(1, 1, 1, 1)
    }
}
