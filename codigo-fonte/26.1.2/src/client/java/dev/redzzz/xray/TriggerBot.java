package dev.redzzz.xray;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public final class TriggerBot {
    private TriggerBot() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(TriggerBot::tick);
    }

    private static void tick(Minecraft client) {
        LocalPlayer player = client.player;
        if (!XrayState.isTriggerBotEnabled() || player == null || client.level == null
                || client.gameMode == null || client.screen != null
                || player.getAttackStrengthScale(0.0F) < XrayState.getTriggerStrength()
                || !(client.crosshairPickEntity instanceof LivingEntity target)
                || !target.isAlive() || target.isSpectator()
                || FriendManager.shouldProtect(player, target)
                || !player.hasLineOfSight(target)) {
            return;
        }

        client.gameMode.attack(player, target);
        player.swing(InteractionHand.MAIN_HAND);
    }
}
