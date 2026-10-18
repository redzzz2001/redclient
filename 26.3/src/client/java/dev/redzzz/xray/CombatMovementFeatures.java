package dev.redzzz.xray;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public final class CombatMovementFeatures {
    private static boolean flyApplied;
    private static float previousFlySpeed = 0.05F;

    private CombatMovementFeatures() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(CombatMovementFeatures::tick);
    }

    private static void tick(Minecraft client) {
        if (client.player == null || client.level == null) {
            flyApplied = false;
            return;
        }

        handleFly(client.player);
        handleJesus(client);
        handleSpeedAndJump(client.player);
        handleKillAura(client);
    }

    private static void handleFly(LocalPlayer player) {
        Abilities abilities = player.getAbilities();
        if (XrayState.isFlyEnabled()) {
            if (!flyApplied) {
                previousFlySpeed = abilities.getFlyingSpeed();
            }
            abilities.mayfly = true;
            abilities.flying = true;
            abilities.setFlyingSpeed(XrayState.getFlySpeed());
            flyApplied = true;
        } else if (flyApplied) {
            abilities.setFlyingSpeed(previousFlySpeed);
            if (!player.isCreative() && !player.isSpectator()) {
                abilities.flying = false;
                abilities.mayfly = false;
            }
            flyApplied = false;
        }
    }

    private static void handleJesus(Minecraft client) {
        LocalPlayer player = client.player;
        if (!XrayState.isJesusEnabled() || XrayState.isFlyEnabled()) {
            return;
        }

        double feetY = player.getBoundingBox().minY;
        BlockPos waterPos = BlockPos.containing(player.getX(), feetY - 0.08D, player.getZ());
        FluidState water = client.level.getFluidState(waterPos);

        // While submerged, the feet block is the reliable check. Near the surface,
        // sampling slightly below the feet keeps the player supported over water.
        if (!water.typeHolder().is(FluidTags.WATER)) {
            waterPos = BlockPos.containing(player.getX(), feetY, player.getZ());
            water = client.level.getFluidState(waterPos);
        }
        if (!water.typeHolder().is(FluidTags.WATER)) {
            return;
        }

        double surfaceY = waterPos.getY() + water.getHeight(client.level, waterPos);
        double depth = surfaceY - feetY;
        Vec3 movement = player.getDeltaMovement();

        player.setSwimming(false);
        if (depth > 0.04D || player.isUnderWater()) {
            // Bring the player to the surface instead of doing nothing underwater.
            double lift = depth > 0.65D ? 0.24D : Math.max(0.10D, depth * 0.45D);
            player.setDeltaMovement(movement.x, lift, movement.z);
            player.setOnGround(false);
        } else {
            // Lock the feet just above the fluid surface so gravity cannot sink them.
            player.setPos(player.getX(), surfaceY + 0.015D, player.getZ());
            player.setDeltaMovement(movement.x, 0.0D, movement.z);
            player.setOnGround(true);
        }
    }

    private static void handleSpeedAndJump(LocalPlayer player) {
        if (XrayState.isFlyEnabled() || player.isPassenger()) {
            return;
        }

        Vec3 movement = player.getDeltaMovement();
        double horizontalSpeed = Math.hypot(movement.x, movement.z);
        double speedMultiplier = XrayState.getSpeedMultiplier();
        double maximumSpeed = Math.min(1.35D, 0.45D * speedMultiplier);
        if (XrayState.isSpeedEnabled() && horizontalSpeed > 0.015D
                && !player.isInWater() && horizontalSpeed < maximumSpeed) {
            double boostedSpeed = Math.min(maximumSpeed, horizontalSpeed * speedMultiplier);
            double scale = boostedSpeed / horizontalSpeed;
            movement = new Vec3(movement.x * scale, movement.y, movement.z * scale);
            player.setDeltaMovement(movement);
        }

        if (XrayState.isSuperJumpEnabled() && player.input != null
                && player.input.keyPresses.jump()
                && movement.y > 0.30D && movement.y < 0.60D
                && !player.isInWater()) {
            player.setDeltaMovement(movement.x, XrayState.getJumpPower(), movement.z);
        }
    }

    private static void handleKillAura(Minecraft client) {
        LocalPlayer player = client.player;
        if (!XrayState.isKillAuraEnabled() || client.gameMode == null
                || player.getAttackStrengthScale(0.0F) < 0.92F) {
            return;
        }

        LivingEntity closest = null;
        double closestDistance = XrayState.getKillAuraRange() * XrayState.getKillAuraRange();
        for (Entity entity : client.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living) || living == player || !living.isAlive()
                    || living.isSpectator() || FriendManager.shouldProtect(player, living)
                    || !player.hasLineOfSight(living)) {
                continue;
            }
            double distance = player.distanceToSqr(living);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = living;
            }
        }

        if (closest != null) {
            client.gameMode.attack(player, closest);
            player.swing(InteractionHand.MAIN_HAND,
                    player.getMainHandItem().getAttackAnimation(), false);
        }
    }
}
