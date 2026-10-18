package dev.redzzz.xray;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;

public final class AutoTotem {
    private static final int SWAP_OFFHAND_BUTTON = Inventory.SLOT_OFFHAND;
    private static int cooldown;

    private AutoTotem() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(AutoTotem::tick);
    }

    private static void tick(Minecraft client) {
        if (cooldown > 0) {
            cooldown--;
        }

        LocalPlayer player = client.player;
        if (!XrayState.isAutoTotemEnabled() || player == null || client.gameMode == null
                || !player.isAlive() || player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING
                || player.containerMenu != player.inventoryMenu || cooldown > 0) {
            return;
        }

        Inventory inventory = player.getInventory();
        for (int inventorySlot = 0; inventorySlot < Inventory.INVENTORY_SIZE; inventorySlot++) {
            if (inventory.getItem(inventorySlot).getItem() != Items.TOTEM_OF_UNDYING) {
                continue;
            }

            int menuSlot = inventorySlot < Inventory.getSelectionSize()
                    ? 36 + inventorySlot
                    : inventorySlot;
            client.gameMode.handleContainerInput(
                    player.inventoryMenu.containerId,
                    menuSlot,
                    SWAP_OFFHAND_BUTTON,
                    ContainerInput.SWAP,
                    player
            );
            cooldown = 5;
            return;
        }
    }
}
