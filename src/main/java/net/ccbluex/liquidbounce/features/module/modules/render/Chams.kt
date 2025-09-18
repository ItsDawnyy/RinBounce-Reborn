/*
 * RinBounce Reborn Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/ItsDawnyy/RinBounce-Reborn
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Category
import net.ccbluex.liquidbounce.features.module.Module

object Chams : Module("Chams", Category.RENDER) {
    val targets by boolean("Targets", true)
    val chests by boolean("Chests", true)
    val items by boolean("Items", true)
}
