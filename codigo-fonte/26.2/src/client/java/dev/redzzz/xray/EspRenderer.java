package dev.redzzz.xray;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

public final class EspRenderer {
    private static final GizmoStyle PLAYER_STYLE = GizmoStyle.strokeAndFill(0xFFFF3B30, 2.5F, 0x22FF3B30);
    private static final GizmoStyle CHEST_STYLE = GizmoStyle.strokeAndFill(0xFFFFC107, 2.5F, 0x22FFC107);
    private static final GizmoStyle BED_STYLE = GizmoStyle.strokeAndFill(0xFF00E5FF, 2.5F, 0x2200E5FF);
    private static final GizmoStyle HOSTILE_MOB_STYLE = GizmoStyle.strokeAndFill(0xFFFF3B30, 2.5F, 0x22FF3B30);
    private static final GizmoStyle PASSIVE_MOB_STYLE = GizmoStyle.strokeAndFill(0xFF7CFF4F, 2.5F, 0x227CFF4F);

    private EspRenderer() {}

    public static void register() {
        LevelExtractionEvents.END_EXTRACTION.register(context -> {
            ClientLevel level = context.level();
            Minecraft client = Minecraft.getInstance();

            if (XrayState.isPlayerEspEnabled()) {
                for (AbstractClientPlayer player : level.players()) {
                    if (player != client.player && player.isAlive() && !player.isSpectator()
                            && client.player != null
                            && client.player.distanceToSqr(player) <= XrayState.getEntityEspRange() * XrayState.getEntityEspRange()) {
                        Gizmos.cuboid(player.getBoundingBox().inflate(0.05D), PLAYER_STYLE).setAlwaysOnTop();
                        String health = formatHealth(player.getHealth()) + "/"
                                + formatHealth(player.getMaxHealth()) + " HP";
                        String label = player.getName().getString() + "  " + health;
                        Vec3 labelPos = new Vec3(player.getX(), player.getBoundingBox().maxY + 0.35D,
                                player.getZ());
                        Gizmos.billboardText(label, labelPos,
                                TextGizmo.Style.forColorAndCentered(0xFFFF3B30).withScale(0.85F))
                                .setAlwaysOnTop();
                    }
                }
            }

            if (XrayState.isMobEspEnabled()) {
                for (Entity entity : level.entitiesForRendering()) {
                    if (entity instanceof Mob mob && mob.isAlive() && !mob.isInvisible()
                            && client.player != null
                            && client.player.distanceToSqr(mob) <= XrayState.getEntityEspRange() * XrayState.getEntityEspRange()) {
                        renderMob(mob);
                    }
                }
            }

            if ((!XrayState.isChestEspEnabled() && !XrayState.isBedEspEnabled()) || client.player == null) {
                return;
            }

            int centerChunkX = client.player.getBlockX() >> 4;
            int centerChunkZ = client.player.getBlockZ() >> 4;
            int chunkRadius = XrayState.getBlockEspChunkRadius();
            for (int chunkX = centerChunkX - chunkRadius; chunkX <= centerChunkX + chunkRadius; chunkX++) {
                for (int chunkZ = centerChunkZ - chunkRadius; chunkZ <= centerChunkZ + chunkRadius; chunkZ++) {
                    LevelChunk chunk = level.getChunkSource().getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                    if (chunk == null) {
                        continue;
                    }
                    for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                        renderBlockEntity(blockEntity);
                    }
                    if (XrayState.isBedEspEnabled()) {
                        chunk.findBlocks(
                                state -> state.getBlock() instanceof BedBlock,
                                (pos, state) -> Gizmos.cuboid(pos, 0.03F, BED_STYLE).setAlwaysOnTop()
                        );
                    }
                }
            }
        });
    }

    private static void renderMob(Mob mob) {
        boolean hostile = mob instanceof Enemy;
        int color = hostile ? 0xFFFF3B30 : 0xFF7CFF4F;
        GizmoStyle style = hostile ? HOSTILE_MOB_STYLE : PASSIVE_MOB_STYLE;
        Gizmos.cuboid(mob.getBoundingBox().inflate(0.04D), style).setAlwaysOnTop();

        String health = formatHealth(mob.getHealth()) + "/" + formatHealth(mob.getMaxHealth()) + " HP";
        String label = mob.getName().getString() + "  " + health;
        Vec3 labelPos = new Vec3(mob.getX(), mob.getBoundingBox().maxY + 0.35D, mob.getZ());
        Gizmos.billboardText(label, labelPos,
                TextGizmo.Style.forColorAndCentered(color).withScale(0.85F)).setAlwaysOnTop();
    }

    private static String formatHealth(float health) {
        if (Math.abs(health - Math.round(health)) < 0.05F) {
            return Integer.toString(Math.round(health));
        }
        return String.format(Locale.ROOT, "%.1f", health);
    }

    private static void renderBlockEntity(BlockEntity blockEntity) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(blockEntity.getBlockState().getBlock());
        String path = id.getPath();
        BlockPos pos = blockEntity.getBlockPos();

        if (XrayState.isChestEspEnabled()
                && (path.contains("chest") || path.equals("barrel"))) {
            Gizmos.cuboid(pos, 0.03F, CHEST_STYLE).setAlwaysOnTop();
        }
    }
}
