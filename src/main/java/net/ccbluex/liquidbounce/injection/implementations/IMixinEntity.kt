/*
 * RinBounce Reborn Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/ItsDawnyy/RinBounce-Reborn
 */
package net.ccbluex.liquidbounce.injection.implementations

interface IMixinEntity {
	var lerpX: Double
	var lerpY: Double
	var lerpZ: Double
	var trueX: Double
	var trueY: Double
	var trueZ: Double
	var truePos: Boolean
}