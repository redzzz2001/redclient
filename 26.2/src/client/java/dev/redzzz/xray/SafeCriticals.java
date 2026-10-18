package dev.redzzz.xray;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class SafeCriticals {
    private static final int MAX_WAIT_TICKS = 20;

    private static Entity pendingTarget;
    private static int waitTicks;
    private static boolean dispatching;

    private SafeCriticals() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(SafeCriticals::tick);
    }

    public static boolean queue(LocalPlayer player, Entity target) {
        if (dispatching || !XrayState.isCriticalsEnabled()
                || !(target instanceof LivingEntity living) || !living.isAlive()
                || !player.onGround() || player.isInWater() || player.isPassenger()
                || XrayState.isFlyEnabled()
                || player.getAttackStrengthScale(0.0F) < 0.85F) {
            return false;
        }

        if (pendingTarget == null) {
            pendingTarget = target;
            waitTicks = 0;
            player.jumpFromGround();
        }
        return true;
    }

    private static void tick(Minecraft client) {
        if (pendingTarget == null) {
            return;
        }

        LocalPlayer player = client.player;
        Entity target = pendingTarget;
        waitTicks++;

        if (!XrayState.isCriticalsEnabled() || player == null || client.level == null
                || client.gameMode == null || waitTicks > MAX_WAIT_TICKS
                || target.level() != client.level || !target.isAlive()
                || FriendManager.shouldProtect(player, (LivingEntity) target)) {
            clear();
            return;
        }

        if (!player.onGround() && player.fallDistance > 0.0F
                && player.getDeltaMovement().y < -0.01D) {
            dispatching = true;
            try {
                client.gameMode.attack(player, target);
                player.swing(InteractionHand.MAIN_HAND);
            } finally {
                dispatching = false;
                clear();
            }
        } else if (waitTicks > 2 && player.onGround()) {
            clear();
        }
    }

    private static void clear() {
        pendingTarget = null;
        waitTicks = 0;
    }
}
