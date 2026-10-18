package dev.redzzz.xray.mixin;

import dev.redzzz.xray.XrayState;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Redirect(method = "sendPosition", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z"))
    private boolean redclient$spoofGroundForNoFall(LocalPlayer player) {
        boolean flyProtection = XrayState.isFlyEnabled() && !player.onGround();
        boolean fallProtection = XrayState.isNoFallEnabled()
                && player.fallDistance > 2.5D
                && player.getDeltaMovement().y < 0.0D;
        if (flyProtection || fallProtection) {
            return true;
        }
        return player.onGround();
    }
}
