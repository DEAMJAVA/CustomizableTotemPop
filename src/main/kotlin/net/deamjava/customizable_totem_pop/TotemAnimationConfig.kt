package net.deamjava.customizable_totem_pop

import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.io.File

data class TotemConfigData(
    var enabled: Boolean = true,
    var offsetX: Float = 0f,           // -1.0 to 1.0: shifts overlay left/right
    var offsetY: Float = 0f,           // -1.0 to 1.0: shifts overlay up/down
    var scale: Float = 1f,             // 0.1 to 5.0: multiplier on the base 0.8f model scale
    var opacity: Float = 1f,           // 0.0 to 1.0: max alpha of the animation
    var durationMultiplier: Float = 1f,// 0.1 to 3.0: >1 = slower, <1 = faster
    var flipHorizontal: Boolean = false,
    var flipVertical: Boolean = false,
)

object TotemAnimationConfig {
    private val GSON = GsonBuilder().setPrettyPrinting().create()
    private val CONFIG_FILE: File by lazy {
        FabricLoader.getInstance().configDir.resolve("customizable_totem_pop.json").toFile()
    }

    var data = TotemConfigData()
        private set

    fun load() {
        if (CONFIG_FILE.exists()) {
            try {
                data = GSON.fromJson(CONFIG_FILE.readText(), TotemConfigData::class.java)
                    ?: TotemConfigData()
            } catch (e: Exception) {
                data = TotemConfigData()
            }
        } else {
            save()
        }
    }

    fun save() {
        CONFIG_FILE.parentFile?.mkdirs()
        CONFIG_FILE.writeText(GSON.toJson(data))
    }

    // Convenience accessors used by the mixin
    val isEnabled get() = data.enabled
    val offsetX get() = data.offsetX
    val offsetY get() = data.offsetY
    val scale get() = data.scale
    val opacity get() = data.opacity
    val durationMultiplier get() = data.durationMultiplier
    val flipHorizontal get() = data.flipHorizontal
    val flipVertical get() = data.flipVertical
}