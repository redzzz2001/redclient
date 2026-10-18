package dev.redzzz.xray.mixin;

import dev.redzzz.xray.XrayState;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class NightVisionMixin {
    @Redirect(method = "extract", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private boolean xraybasic$fakeNightVision(LocalPlayer player, Holder<MobEffect> effect) {
        if (XrayState.isNightVisionEnabled() && effect.equals(MobEffects.NIGHT_VISION)) {
            return true;
        }
        return player.hasEffect(effect);
    }

    @Redirect(method = "extract", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;getNightVisionScale(Lnet/minecraft/world/entity/LivingEntity;F)F"))
    private float xraybasic$fullNightVision(LivingEntity entity, float partialTick) {
        if (XrayState.isNightVisionEnabled()) {
            return 1.0F;
        }
        return GameRenderer.getNightVisionScale(entity, partialTick);
    }
}
