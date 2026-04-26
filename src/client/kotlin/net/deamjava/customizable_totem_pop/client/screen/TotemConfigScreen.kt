package net.deamjava.customizable_totem_pop.client.screen

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.deamjava.customizable_totem_pop.TotemAnimationConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screens.Screen
import 	net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
object TotemConfigScreen {

    fun create(parent: Screen): Screen {
        val cfg = TotemAnimationConfig.data
        val builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("Customizable Totem Pop"))
            .setSavingRunnable { TotemAnimationConfig.save() }

        val eb = builder.entryBuilder()
        val cat = builder.getOrCreateCategory(Component.literal("Animation"))

        cat.addEntry(
            eb.startBooleanToggle(Component.literal("Enabled"), cfg.enabled)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Toggle the totem pop animation on/off."))
                .setSaveConsumer { cfg.enabled = it }
                .build()
        )
        cat.addEntry(
            eb.startFloatField(Component.literal("X Offset"), cfg.offsetX)
                .setDefaultValue(0f)
                .setMin(-10f).setMax(10f)
                .setTooltip(Component.literal("Horizontal offset. -1 = far left, +1 = far right."))
                .setSaveConsumer { cfg.offsetX = it }
                .build()
        )
        cat.addEntry(
            eb.startFloatField(Component.literal("Y Offset"), cfg.offsetY)
                .setDefaultValue(0f)
                .setMin(-10f).setMax(10f)
                .setTooltip(Component.literal("Vertical offset. -1 = bottom, +1 = top."))
                .setSaveConsumer { cfg.offsetY = it }
                .build()
        )
        cat.addEntry(
            eb.startFloatField(Component.literal("Scale"), cfg.scale)
                .setDefaultValue(1f)
                .setMin(0.1f).setMax(5f)
                .setTooltip(Component.literal("Size multiplier for the totem model."))
                .setSaveConsumer { cfg.scale = it }
                .build()
        )
        cat.addEntry(
            eb.startFloatField(Component.literal("Opacity"), cfg.opacity)
                .setDefaultValue(1f)
                .setMin(0f).setMax(1f)
                .setTooltip(Component.literal("Max brightness/opacity of the animation. 0 = dark, 1 = full-bright (vanilla)."))
                .setSaveConsumer { cfg.opacity = it }
                .build()
        )
        cat.addEntry(
            eb.startFloatField(Component.literal("Duration Multiplier"), cfg.durationMultiplier)
                .setDefaultValue(1f)
                .setMin(0.1f).setMax(3f)
                .setTooltip(Component.literal(">1 = slower animation, <1 = faster."))
                .setSaveConsumer { cfg.durationMultiplier = it }
                .build()
        )
        cat.addEntry(
            eb.startBooleanToggle(Component.literal("Flip Horizontal"), cfg.flipHorizontal)
                .setDefaultValue(false)
                .setTooltip(Component.literal("Mirror the model left-to-right."))
                .setSaveConsumer { cfg.flipHorizontal = it }
                .build()
        )
        cat.addEntry(
            eb.startBooleanToggle(Component.literal("Flip Vertical"), cfg.flipVertical)
                .setDefaultValue(false)
                .setTooltip(Component.literal("Mirror the model top-to-bottom."))
                .setSaveConsumer { cfg.flipVertical = it }
                .build()
        )

        return builder.build()
    }
}