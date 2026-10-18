package dev.redzzz.xray;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class AimBot {
    private static final float MAX_ANGLE = 60.0F;

    private AimBot() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(AimBot::tick);
    }

    private static void tick(Minecraft client) {
        if (!XrayState.isAimBotEnabled() || client.player == null || client.level == null) {
            return;
        }

        LocalPlayer player = client.player;
        AbstractClientPlayer bestTarget = null;
        float bestAngle = MAX_ANGLE;

        for (AbstractClientPlayer candidate : client.level.players()) {
            if (candidate == player || !candidate.isAlive() || candidate.isSpectator()
                    || FriendManager.shouldProtect(player, candidate)
                    || player.distanceToSqr(candidate) > XrayState.getAimRange() * XrayState.getAimRange()
                    || !player.hasLineOfSight(candidate)) {
                continue;
            }

            Rotation rotation = rotationTo(player, candidate);
            float angle = (float) Math.hypot(
                    Mth.wrapDegrees(rotation.yaw() - player.getYRot()),
                    rotation.pitch() - player.getXRot());
            if (angle < bestAngle) {
                bestAngle = angle;
                bestTarget = candidate;
            }
        }

        if (bestTarget != null) {
            Rotation target = rotationTo(player, bestTarget);
            float yawDelta = Mth.wrapDegrees(target.yaw() - player.getYRot());
            float pitchDelta = target.pitch() - player.getXRot();
            float strength = XrayState.getAimStrength();
            player.setYRot(player.getYRot() + yawDelta * strength);
            player.setXRot(Mth.clamp(player.getXRot() + pitchDelta * strength, -90.0F, 90.0F));
        }
    }

    private static Rotation rotationTo(LocalPlayer player, AbstractClientPlayer target) {
        Vec3 from = player.getEyePosition();
        Vec3 to = target.getEyePosition();
        double x = to.x - from.x;
        double y = to.y - from.y;
        double z = to.z - from.z;
        double horizontal = Math.sqrt(x * x + z * z);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0F;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, horizontal));
        return new Rotation(yaw, pitch);
    }

    private record Rotation(float yaw, float pitch) {}
}
