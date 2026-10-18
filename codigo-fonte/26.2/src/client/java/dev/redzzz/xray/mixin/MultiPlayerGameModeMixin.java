package dev.redzzz.xray.mixin;

import dev.redzzz.xray.FriendManager;
import dev.redzzz.xray.SafeCriticals;
import dev.redzzz.xray.XrayState;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow
    private int destroyDelay;

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void redclient$protectFriendsAndCritical(Player attacker, Entity target, CallbackInfo ci) {
        if (!(attacker instanceof LocalPlayer player)) {
            return;
        }
        if (target instanceof LivingEntity living && FriendManager.shouldProtect(player, living)) {
            ci.cancel();
            return;
        }
        if (SafeCriticals.queue(player, target)) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void redclient$removeMiningCooldown(CallbackInfo ci) {
        if (XrayState.isFastMineEnabled()) {
            destroyDelay = 0;
        }
    }

    @Redirect(method = "continueDestroyBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    private float redclient$accelerateMiningProgress(BlockState state, Player player,
            BlockGetter level, BlockPos pos) {
        float progress = state.getDestroyProgress(player, level, pos);
        return XrayState.isFastMineEnabled()
                ? progress * XrayState.getFastMineMultiplier()
                : progress;
    }
}
