package dev.redzzz.xray.mixin;

import dev.redzzz.xray.XrayState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Redirect(method = "pick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;raycastHitResult(FLnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/HitResult;"))
    private HitResult redclient$applyReach(LocalPlayer player, float partialTick, Entity cameraEntity) {
        if (!XrayState.isReachEnabled()) {
            return player.raycastHitResult(partialTick, cameraEntity);
        }

        float reach = XrayState.getReachDistance();
        AttackRange range = new AttackRange(0.0F, reach, 0.0F, reach, 0.1F, 1.0F);
        HitResult result = range.getClosesetHit(cameraEntity, partialTick, EntitySelector.CAN_BE_PICKED);
        return result != null ? result : player.raycastHitResult(partialTick, cameraEntity);
    }
}
