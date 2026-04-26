package net.deamjava.customizable_totem_pop.client.mixin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.deamjava.customizable_totem_pop.TotemAnimationConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "renderItemActivationAnimation",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ctp$renderItemActivationAnimation(
            PoseStack poseStack,
            float partialTicks,
            SubmitNodeCollector submitNodeCollector,
            CallbackInfo ci) {

        ci.cancel();

        if (!TotemAnimationConfig.INSTANCE.isEnabled()) return;

        ScreenEffectRendererAccessor acc = (ScreenEffectRendererAccessor)(Object) this;
        ItemStack item  = acc.getItemActivationItem();
        int       ticks = acc.getItemActivationTicks();

        if (item == null || ticks <= 0) return;

        // ── Progress along the animation curve ───────────────────────────────
        float durMul = TotemAnimationConfig.INSTANCE.getDurationMultiplier();
        int   elapsed = 40 - ticks;                    // vanilla: 0..39
        float scale   = Math.min((elapsed + partialTicks) / (40.0f * durMul), 1.0f);

        float ts = scale * scale;
        float tc = scale * ts;
        // Vanilla cubic-ease "pop" curve
        float smoothScale = 10.25f*tc*ts - 24.95f*ts*ts + 25.5f*tc - 13.8f*ts + 4.0f*scale;
        float piScale     = smoothScale * (float) Math.PI;

        // ── Config values ─────────────────────────────────────────────────────
        float cfgOffX  = TotemAnimationConfig.INSTANCE.getOffsetX();
        float cfgOffY  = TotemAnimationConfig.INSTANCE.getOffsetY();
        float cfgScale = TotemAnimationConfig.INSTANCE.getScale();
        float opacity  = TotemAnimationConfig.INSTANCE.getOpacity();
        boolean flipH  = TotemAnimationConfig.INSTANCE.getFlipHorizontal();
        boolean flipV  = TotemAnimationConfig.INSTANCE.getFlipVertical();

        float aspect   = (float) minecraft.getWindow().getWidth()
                / minecraft.getWindow().getHeight();

        // Random per-activation drift (vanilla behaviour) + user static offset
        float driftX = acc.getItemActivationOffX() * 0.3f * aspect * Mth.abs(Mth.sin(piScale * 2.0f));
        float driftY = acc.getItemActivationOffY() * 0.3f           * Mth.abs(Mth.sin(piScale * 2.0f));



        // User static offset is constant throughout — never passes through center
        float staticOffX = cfgOffX * 0.3f * aspect;
        float staticOffY = cfgOffY * 0.3f;

        poseStack.pushPose();

        poseStack.translate(
                driftX + staticOffX,
                driftY + staticOffY,
                -10.0f + 9.0f * Mth.sin(piScale)
        );


        float finalScale = 0.8f * cfgScale;
        poseStack.scale(
                finalScale * (flipH ? -1f : 1f),
                finalScale * (flipV ? -1f : 1f),
                finalScale
        );

        poseStack.mulPose(Axis.YP.rotationDegrees(900.0f * Mth.abs(Mth.sin(piScale))));
        poseStack.mulPose(Axis.XP.rotationDegrees(6.0f * Mth.cos(scale * 8.0f)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(6.0f * Mth.cos(scale * 8.0f)));

        // Scale packed full-bright light by opacity for a fade effect
        int combinedLight = Math.round(15728880 * opacity);

        minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        ItemStackRenderState itemState = new ItemStackRenderState();
        minecraft.getItemModelResolver().updateForTopItem(
                itemState, item, ItemDisplayContext.FIXED, minecraft.level, null, 0
        );
        itemState.submit(poseStack, submitNodeCollector, combinedLight, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}