package dev.redzzz.xray;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class FriendManager {
    private static final Path FRIENDS_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("red-client-friends.txt");
    private static final Set<String> FRIENDS = new HashSet<>();
    private static KeyMapping friendKey;

    private FriendManager() {}

    public static void register(KeyMapping.Category category) {
        load();
        friendKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xraybasic.friend", InputConstants.Type.MOUSE,
                InputConstants.MOUSE_BUTTON_MIDDLE, category));
        ClientTickEvents.END_CLIENT_TICK.register(FriendManager::tick);
    }

    public static boolean shouldProtect(LocalPlayer owner, LivingEntity entity) {
        if (!XrayState.isFriendProtectEnabled() || !(entity instanceof AbstractClientPlayer player)) {
            return false;
        }
        return owner.isAlliedTo(player) || FRIENDS.contains(normalize(player.getName().getString()));
    }

    public static boolean add(String name) {
        String normalized = normalize(name.trim());
        if (normalized.isBlank() || normalized.length() > 32 || !FRIENDS.add(normalized)) {
            return false;
        }
        save();
        return true;
    }

    public static boolean remove(String name) {
        boolean removed = FRIENDS.remove(normalize(name.trim()));
        if (removed) {
            save();
        }
        return removed;
    }

    public static List<String> names() {
        return FRIENDS.stream().sorted().toList();
    }

    private static void tick(Minecraft client) {
        while (friendKey != null && friendKey.consumeClick()) {
            if (!(client.crosshairPickEntity instanceof AbstractClientPlayer target) || target == client.player) {
                continue;
            }
            String displayName = target.getName().getString();
            String name = normalize(displayName);
            boolean added;
            if (remove(name)) {
                added = false;
            } else {
                added = add(name);
            }
            if (client.player != null) {
                client.player.sendOverlayMessage(Component.literal(
                        (added ? "Amigo adicionado: " : "Amigo removido: ") + displayName)
                        .withStyle(added ? ChatFormatting.GREEN : ChatFormatting.RED));
            }
        }
    }

    private static String normalize(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    private static void load() {
        try {
            if (Files.exists(FRIENDS_FILE)) {
                for (String line : Files.readAllLines(FRIENDS_FILE, StandardCharsets.UTF_8)) {
                    if (!line.isBlank()) {
                        FRIENDS.add(normalize(line.trim()));
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static void save() {
        try {
            Files.createDirectories(FRIENDS_FILE.getParent());
            List<String> names = FRIENDS.stream().sorted().toList();
            Files.write(FRIENDS_FILE, names, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }
}
