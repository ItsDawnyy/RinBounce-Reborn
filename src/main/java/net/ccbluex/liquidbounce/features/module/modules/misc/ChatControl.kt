/*
 * RinBounce Reborn Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/ItsDawnyy/RinBounce-Reborn
 */
 package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.features.module.Category
import net.ccbluex.liquidbounce.features.module.Module

object ChatControl : Module("ChatControl", Category.MISC, gameDetecting = false) {

    val noChatClear by boolean("NoChatClear", true)
    val stackMessage by boolean("StackMessage", true)
    val noLengthLimit by boolean("NoLengthLimit", true)
    val forceUnicodeChat by boolean("ForceUnicodeChat", false)

}