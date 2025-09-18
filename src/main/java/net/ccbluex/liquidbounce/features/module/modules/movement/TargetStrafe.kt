/*
 * RinBounce Reborn Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/ItsDawnyy/RinBounce-Reborn
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.features.module.Category
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.utils.movement.MovementUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import kotlin.math.cos
import kotlin.math.sin

object TargetStrafe : Module("TargetStrafe", Category.MOVEMENT, gameDetecting = false) {

    private val mode by choices("Mode", arrayOf("Vanilla", "Grim", "Vulcan", "WatchDog"), "Vanilla")

    private val radius by float("Radius", 2.0F, 0.5F..6.0F)
    private val speed by float("Speed", 0.3F, 0.1F..1.0F)
    private val holdSpace by boolean("HoldSpace", true)
    private val renderCircle by boolean("RenderCircle", true)

    private var direction = 1
    private var currentTarget: EntityLivingBase? = null

    override fun onEnable() {
        direction = 1
        currentTarget = null
    }

    val onUpdate = handler<UpdateEvent> {
        val aura = net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
        if (aura.state && aura.target is EntityLivingBase) {
            currentTarget = aura.target as EntityLivingBase
        } else {
            currentTarget = null
        }
    }

    val onMove = handler<MoveEvent> { event ->
        val target = currentTarget ?: return@handler
        if (holdSpace && !mc.gameSettings.keyBindJump.isKeyDown) return@handler

        val player = mc.thePlayer ?: return@handler
        val dist = player.getDistanceToEntity(target)

        if (dist < radius - 0.3F) {
            direction = -direction
        }

        val baseYaw = MovementUtils.direction
        val strafeYaw = Math.toRadians((baseYaw + 90F * direction).toDouble())

        var moveSpeed = MovementUtils.speed + speed

        when (mode) {
            "Grim" -> moveSpeed *= 0.92F
            "Vulcan" -> moveSpeed *= if (player.ticksExisted % 2 == 0) 0.96F else 1.04F
            "WatchDog" -> {
                val jitter = if (player.ticksExisted % 5 == 0) 0.05F else 0.0F
                moveSpeed *= (0.94F + jitter)
            }
        }

        event.x = -sin(strafeYaw) * moveSpeed
        event.z = cos(strafeYaw) * moveSpeed
    }

    val onRender3D = handler<Render3DEvent> { event ->
        val target = currentTarget ?: return@handler
        if (!renderCircle) return@handler

        val tessellator = Tessellator.getInstance()
        val worldRenderer: WorldRenderer = tessellator.worldRenderer

        val x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.renderPosX
        val y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.renderPosY
        val z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.renderPosZ

        GlStateManager.pushMatrix()
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.color(0.2f, 0.8f, 1.0f, 0.5f)

        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION)
        val points = 90
        for (i in 0..points) {
            val angle = 2 * Math.PI * i / points
            val cx = x + radius * cos(angle)
            val cz = z + radius * sin(angle)
            worldRenderer.pos(cx, y + 0.1, cz).endVertex()
        }
        tessellator.draw()

        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }
}
