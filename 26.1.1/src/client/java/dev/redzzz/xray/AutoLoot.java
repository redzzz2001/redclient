package dev.redzzz.xray;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;

public final class AutoLoot {
    private static int cooldown;

    private AutoLoot() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(AutoLoot::tick);
    }

    private static void tick(Minecraft client) {
        if (cooldown > 0) {
            cooldown--;
        }

        LocalPlayer player = client.player;
        if (!XrayState.isAutoLootEnabled() || player == null || client.gameMode == null || cooldown > 0) {
            return;
        }

        AbstractContainerMenu menu = player.containerMenu;
        if (!(menu instanceof ChestMenu || menu instanceof ShulkerBoxMenu
                || menu instanceof HopperMenu || menu instanceof DispenserMenu)) {
            return;
        }

        for (int menuSlot = 0; menuSlot < menu.slots.size(); menuSlot++) {
            Slot slot = menu.slots.get(menuSlot);
            if (slot.container == player.getInventory() || !slot.hasItem() || !slot.mayPickup(player)) {
                continue;
            }
            client.gameMode.handleContainerInput(
                    menu.containerId, menuSlot, 0, ContainerInput.QUICK_MOVE, player);
            cooldown = XrayState.getAutoLootDelay();
            return;
        }
    }
}
