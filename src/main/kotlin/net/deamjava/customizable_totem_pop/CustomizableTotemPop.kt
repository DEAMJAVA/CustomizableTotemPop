package net.deamjava.customizable_totem_pop

import net.fabricmc.api.ModInitializer

// Common entrypoint — intentionally empty.
// All logic is client-only and lives in CustomizableTotemPopClient.
object CustomizableTotemPop : ModInitializer {
	const val MOD_ID = "customizable-totem-pop"

	override fun onInitialize() {
		// nothing to do on the common side
	}
}