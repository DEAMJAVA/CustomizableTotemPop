package net.deamjava.customizable_totem_pop.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object CustomizableTotemPopClient : ClientModInitializer {
	override fun onInitializeClient() {
		TotemAnimationConfig.load()
	}
}