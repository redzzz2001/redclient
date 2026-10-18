package dev.redzzz.xray.mixin;

import dev.redzzz.xray.XrayState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class HudMixin {
    @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
    private void redclient$extractPvpCrosshair(GuiGraphicsExtractor graphics,
                                              DeltaTracker deltaTracker,
                                              CallbackInfo ci) {
        if (!XrayState.isPvpCrosshairEnabled()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.options.getCameraType().isFirstPerson()) {
            int x = graphics.guiWidth() / 2;
            int y = graphics.guiHeight() / 2;
            boolean target = client.crosshairPickEntity instanceof LivingEntity living && living.isAlive();
            int color = target ? 0xFFFF3B30 : 0xFFFFFFFF;

            // Compact 5x5 PvP crosshair. One GUI pixel stays crisp at every GUI scale.
            graphics.fill(x - 2, y, x + 3, y + 1, color);
            graphics.fill(x, y - 2, x + 1, y + 3, color);
            if (target) {
                graphics.fill(x, y, x + 1, y + 1, 0xFFFFFF00);
            }
        }
        ci.cancel();
    }
}
