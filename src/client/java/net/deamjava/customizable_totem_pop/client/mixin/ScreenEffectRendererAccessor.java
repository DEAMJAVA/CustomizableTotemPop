package net.deamjava.customizable_totem_pop.client.mixin;

import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScreenEffectRenderer.class)
public interface ScreenEffectRendererAccessor {

    @Accessor("itemActivationItem")
    ItemStack getItemActivationItem();

    @Accessor("itemActivationTicks")
    int getItemActivationTicks();

    @Accessor("itemActivationOffX")
    float getItemActivationOffX();

    @Accessor("itemActivationOffY")
    float getItemActivationOffY();
}