package dev.redzzz.xray;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class XrayBasicClient implements ClientModInitializer {
    static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("redclient", "main")
    );
    private static final KeyMapping MENU_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.xraybasic.menu", InputConstants.Type.KEYBOARD, InputConstants.KEY_RALT, CATEGORY
    ));
    private static final List<ModuleKey> MODULE_KEYS = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        registerModuleKeys();
        EspRenderer.register();
        AimBot.register();
        CombatMovementFeatures.register();
        SafeCriticals.register();
        AutoTotem.register();
        AutoLoot.register();
        TriggerBot.register();
        FriendManager.register(CATEGORY);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MENU_KEY.consumeClick()) {
                client.setScreenAndShow(new XrayMenuScreen());
            }
            for (ModuleKey moduleKey : MODULE_KEYS) {
                while (moduleKey.mapping().consumeClick()) {
                    moduleKey.toggle().run();
                }
            }
        });
    }

    private static void registerModuleKeys() {
        registerModuleKey("night_vision", XrayState::toggleNightVision);
        registerModuleKey("player_esp", XrayState::togglePlayerEsp);
        registerModuleKey("mob_esp", XrayState::toggleMobEsp);
        registerModuleKey("chest_esp", XrayState::toggleChestEsp);
        registerModuleKey("bed_esp", XrayState::toggleBedEsp);
        registerModuleKey("pvp_crosshair", XrayState::togglePvpCrosshair);
        registerModuleKey("aim_bot", XrayState::toggleAimBot);
        registerModuleKey("kill_aura", XrayState::toggleKillAura);
        registerModuleKey("criticals", XrayState::toggleCriticals);
        registerModuleKey("trigger_bot", XrayState::toggleTriggerBot);
        registerModuleKey("reach", XrayState::toggleReach);
        registerModuleKey("fly", XrayState::toggleFly);
        registerModuleKey("speed", XrayState::toggleSpeed);
        registerModuleKey("super_jump", XrayState::toggleSuperJump);
        registerModuleKey("jesus", XrayState::toggleJesus);
        registerModuleKey("no_fall", XrayState::toggleNoFall);
        registerModuleKey("auto_totem", XrayState::toggleAutoTotem);
        registerModuleKey("auto_loot", XrayState::toggleAutoLoot);
        registerModuleKey("friend_protect", XrayState::toggleFriendProtect);
        registerModuleKey("fast_mine", XrayState::toggleFastMine);
    }

    private static void registerModuleKey(String name, Runnable toggle) {
        KeyMapping mapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.redclient." + name, InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(), CATEGORY
        ));
        MODULE_KEYS.add(new ModuleKey(mapping, toggle));
    }

    private record ModuleKey(KeyMapping mapping, Runnable toggle) {}

}
