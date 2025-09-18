package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.features.module.Category
import net.ccbluex.liquidbounce.features.module.Module

object AutoJump : Module("AutoJump", Category.COMBAT, subjective = true) {
    private val mode by choices("Mode", arrayOf("Aura", "Click", "Both"), "Aura")
    private val minDelay by int("MinDelay", 300, 0..2000)
    private val maxDelay by int("MaxDelay", 600, 0..3000)

    private var lastJumpTime = 0L
    private var nextDelay = 0

    private fun generateNextDelay() {
        val min = minDelay
        val max = if (maxDelay < min) min else maxDelay
        nextDelay = (min..max).random()
    }

    override fun onEnable() {
        super.onEnable()
        generateNextDelay()
        lastJumpTime = 0L
    }

    private fun tryJump() {
        val player = mc.thePlayer ?: return
        val current = System.currentTimeMillis()

        if (player.onGround && current - lastJumpTime >= nextDelay) {
            player.jump()
            lastJumpTime = current
            generateNextDelay()
        }
    }

    // KillAura đánh entity
    val onAttack = handler<AttackEvent> {
        if (mode.equals("Aura", true) || mode.equals("Both", true)) {
            tryJump()
        }
    }

    // Đánh tay / AutoClicker (chuột trái nhấn xuống)
    val onUpdate = handler<UpdateEvent> {
        if ((mode.equals("Click", true) || mode.equals("Both", true)) &&
            mc.gameSettings.keyBindAttack.isKeyDown
        ) {
            tryJump()
        }
    }
}
