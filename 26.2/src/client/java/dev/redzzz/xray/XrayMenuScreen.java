package dev.redzzz.xray;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

public final class XrayMenuScreen extends Screen {
    private static final int HEADER_HEIGHT = 20;
    private static final int ROW_HEIGHT = 19;

    public XrayMenuScreen() {
        super(Component.translatable("screen.xraybasic.title"));
    }

    @Override
    protected void init() {
        int gap = 4;
        int columnWidth = Math.min(132, Math.max(86, (width - 28) / 4));
        int totalWidth = columnWidth * 4 + gap * 3;
        int startX = (width - totalWidth) / 2;
        int top = 38;
        int combatX = startX;
        int renderX = combatX + columnWidth + gap;
        int movementX = renderX + columnWidth + gap;
        int miscX = movementX + columnWidth + gap;

        addRenderableWidget(new CategoryHeader(combatX, top, columnWidth, "COMBAT"));
        addRenderableWidget(new CategoryHeader(renderX, top, columnWidth, "RENDER"));
        addRenderableWidget(new CategoryHeader(movementX, top, columnWidth, "MOVEMENT"));
        addRenderableWidget(new CategoryHeader(miscX, top, columnWidth, "MISC"));

        int y = top + HEADER_HEIGHT;
        addToggle(combatX, y, columnWidth, "screen.xraybasic.aim_bot", XrayState::isAimBotEnabled, XrayState::toggleAimBot);
        y += ROW_HEIGHT;
        addSlider(combatX, y, columnWidth, "screen.xraybasic.aim_strength", 0.1D, 1.0D,
                XrayState::getAimStrength, value -> XrayState.setAimStrength((float) value),
                value -> Long.toString(Math.round(value * 100.0D)));
        y += ROW_HEIGHT;
        addSlider(combatX, y, columnWidth, "screen.xraybasic.aim_range", 3.0D, 64.0D,
                XrayState::getAimRange, value -> XrayState.setAimRange((float) value), XrayMenuScreen::oneDecimal);
        y += ROW_HEIGHT;
        addToggle(combatX, y, columnWidth, "screen.xraybasic.kill_aura", XrayState::isKillAuraEnabled, XrayState::toggleKillAura);
        y += ROW_HEIGHT;
        addSlider(combatX, y, columnWidth, "screen.xraybasic.aura_range", 3.0D, 10.0D,
                XrayState::getKillAuraRange, value -> XrayState.setKillAuraRange((float) value), XrayMenuScreen::oneDecimal);
        y += ROW_HEIGHT;
        addToggle(combatX, y, columnWidth, "screen.xraybasic.criticals", XrayState::isCriticalsEnabled, XrayState::toggleCriticals);
        y += ROW_HEIGHT;
        addToggle(combatX, y, columnWidth, "screen.xraybasic.trigger_bot", XrayState::isTriggerBotEnabled, XrayState::toggleTriggerBot);
        y += ROW_HEIGHT;
        addSlider(combatX, y, columnWidth, "screen.xraybasic.trigger_strength", 0.1D, 1.0D,
                XrayState::getTriggerStrength, value -> XrayState.setTriggerStrength((float) value),
                value -> Math.round(value * 100.0D) + "%");
        y += ROW_HEIGHT;
        addToggle(combatX, y, columnWidth, "screen.xraybasic.reach", XrayState::isReachEnabled, XrayState::toggleReach);
        y += ROW_HEIGHT;
        addSlider(combatX, y, columnWidth, "screen.xraybasic.reach_distance", 3.0D, 10.0D,
                XrayState::getReachDistance, value -> XrayState.setReachDistance((float) value), XrayMenuScreen::oneDecimal);

        y = top + HEADER_HEIGHT;
        addToggle(renderX, y, columnWidth, "screen.xraybasic.night_vision", XrayState::isNightVisionEnabled, XrayState::toggleNightVision);
        y += ROW_HEIGHT;
        addToggle(renderX, y, columnWidth, "screen.xraybasic.player_esp", XrayState::isPlayerEspEnabled, XrayState::togglePlayerEsp);
        y += ROW_HEIGHT;
        addToggle(renderX, y, columnWidth, "screen.xraybasic.mob_esp", XrayState::isMobEspEnabled, XrayState::toggleMobEsp);
        y += ROW_HEIGHT;
        addToggle(renderX, y, columnWidth, "screen.xraybasic.chest_esp", XrayState::isChestEspEnabled, XrayState::toggleChestEsp);
        y += ROW_HEIGHT;
        addToggle(renderX, y, columnWidth, "screen.xraybasic.bed_esp", XrayState::isBedEspEnabled, XrayState::toggleBedEsp);
        y += ROW_HEIGHT;
        addToggle(renderX, y, columnWidth, "screen.xraybasic.pvp_crosshair", XrayState::isPvpCrosshairEnabled, XrayState::togglePvpCrosshair);
        y += ROW_HEIGHT;
        addSlider(renderX, y, columnWidth, "screen.xraybasic.entity_esp_range", 16.0D, 128.0D,
                XrayState::getEntityEspRange, value -> XrayState.setEntityEspRange((float) value),
                value -> Integer.toString((int) Math.round(value)));
        y += ROW_HEIGHT;
        addSlider(renderX, y, columnWidth, "screen.xraybasic.block_esp_chunks", 1.0D, 12.0D,
                XrayState::getBlockEspChunkRadius, value -> XrayState.setBlockEspChunkRadius((int) Math.round(value)),
                value -> Integer.toString((int) Math.round(value)));

        y = top + HEADER_HEIGHT;
        addToggle(movementX, y, columnWidth, "screen.xraybasic.fly", XrayState::isFlyEnabled, XrayState::toggleFly);
        y += ROW_HEIGHT;
        addSlider(movementX, y, columnWidth, "screen.xraybasic.fly_speed", 0.05D, 0.50D,
                XrayState::getFlySpeed, value -> XrayState.setFlySpeed((float) value),
                value -> String.format(Locale.ROOT, "%.2f", value));
        y += ROW_HEIGHT;
        addToggle(movementX, y, columnWidth, "screen.xraybasic.speed", XrayState::isSpeedEnabled, XrayState::toggleSpeed);
        y += ROW_HEIGHT;
        addSlider(movementX, y, columnWidth, "screen.xraybasic.speed_multiplier", 1.0D, 3.0D,
                XrayState::getSpeedMultiplier, value -> XrayState.setSpeedMultiplier((float) value),
                value -> String.format(Locale.ROOT, "%.2fx", value));
        y += ROW_HEIGHT;
        addToggle(movementX, y, columnWidth, "screen.xraybasic.super_jump", XrayState::isSuperJumpEnabled, XrayState::toggleSuperJump);
        y += ROW_HEIGHT;
        addSlider(movementX, y, columnWidth, "screen.xraybasic.jump_power", 0.42D, 1.50D,
                XrayState::getJumpPower, value -> XrayState.setJumpPower((float) value),
                value -> String.format(Locale.ROOT, "%.2f", value));
        y += ROW_HEIGHT;
        addToggle(movementX, y, columnWidth, "screen.xraybasic.jesus", XrayState::isJesusEnabled, XrayState::toggleJesus);
        y += ROW_HEIGHT;
        addToggle(movementX, y, columnWidth, "screen.xraybasic.no_fall", XrayState::isNoFallEnabled, XrayState::toggleNoFall);

        y = top + HEADER_HEIGHT;
        addToggle(miscX, y, columnWidth, "screen.xraybasic.auto_totem", XrayState::isAutoTotemEnabled, XrayState::toggleAutoTotem);
        y += ROW_HEIGHT;
        addToggle(miscX, y, columnWidth, "screen.xraybasic.auto_loot", XrayState::isAutoLootEnabled, XrayState::toggleAutoLoot);
        y += ROW_HEIGHT;
        addSlider(miscX, y, columnWidth, "screen.xraybasic.auto_loot_delay", 0.0D, 10.0D,
                XrayState::getAutoLootDelay, value -> XrayState.setAutoLootDelay((int) Math.round(value)),
                value -> Integer.toString((int) Math.round(value)));
        y += ROW_HEIGHT;
        addToggle(miscX, y, columnWidth, "screen.xraybasic.friend_protect", XrayState::isFriendProtectEnabled, XrayState::toggleFriendProtect);
        y += ROW_HEIGHT;
        addToggle(miscX, y, columnWidth, "screen.xraybasic.fast_mine", XrayState::isFastMineEnabled, XrayState::toggleFastMine);
        y += ROW_HEIGHT;
        addSlider(miscX, y, columnWidth, "screen.xraybasic.fast_mine_multiplier", 1.0D, 1.4D,
                XrayState::getFastMineMultiplier, value -> XrayState.setFastMineMultiplier((float) value),
                value -> String.format(Locale.ROOT, "%.2fx", value));
        y += ROW_HEIGHT;
        addRenderableWidget(new RedModuleButton(miscX, y, columnWidth, 18,
                () -> Component.translatable("screen.xraybasic.manage_friends"), null,
                () -> minecraft.setScreenAndShow(new FriendManagerScreen(this))));

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(width / 2 - 50, height - 25, 100, 18).build());
    }

    private void addToggle(int x, int y, int width, String key, Supplier<Boolean> state, Runnable action) {
        addRenderableWidget(new RedModuleButton(x, y, width, 18,
                () -> Component.literal(Component.translatable(key, "").getString()
                        .replace(":", "").trim()),
                state, action));
    }

    private void addSlider(int x, int y, int width, String key, double min, double max,
            DoubleSupplier getter, DoubleConsumer setter, Function<Double, String> formatter) {
        addRenderableWidget(new ConfigSlider(x + 3, y + 1, width - 6, 17,
                key, min, max, getter, setter, formatter));
    }

    private static String oneDecimal(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        extractTransparentBackground(graphics);
        graphics.fill(0, 0, width, 30, 0xE7101117);
        graphics.fill(0, 28, width, 30, 0xFFE21D3A);
        graphics.centeredText(font, "RED CLIENT", width / 2, 6, 0xFFFFFFFF);
        graphics.centeredText(font, "Modules   Config   GUI   Friends", width / 2, 18, 0xFFBFC3CC);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private final class CategoryHeader extends AbstractWidget {
        private final String label;

        private CategoryHeader(int x, int y, int width, String label) {
            super(x, y, width, HEADER_HEIGHT, Component.literal(label));
            this.label = label;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            graphics.fill(getX(), getY(), getRight(), getBottom(), 0xE89B0016);
            graphics.fill(getX(), getBottom() - 2, getRight(), getBottom(), 0xFFFF2448);
            graphics.centeredText(font, label, getX() + getWidth() / 2, getY() + 6, 0xFFFFFFFF);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
        }
    }

    private final class RedModuleButton extends AbstractWidget {
        private final Supplier<Component> label;
        private final Supplier<Boolean> state;
        private final Runnable action;

        private RedModuleButton(int x, int y, int width, int height, Supplier<Component> label,
                                Supplier<Boolean> state, Runnable action) {
            super(x, y, width, height, label.get());
            this.label = label;
            this.state = state;
            this.action = action;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            boolean enabled = state != null && state.get();
            int background = enabled ? 0xE8C31912 : (isHovered() ? 0xE84A5060 : 0xD92A2E38);
            graphics.fill(getX(), getY(), getRight(), getBottom(), background);
            if (enabled) {
                graphics.fill(getX(), getY(), getX() + 3, getBottom(), 0xFFFF5A54);
            }
            Component text = label.get();
            setMessage(text);
            graphics.centeredText(font, text, getX() + getWidth() / 2, getY() + 5, 0xFFFFFFFF);
        }

        @Override
        public void onClick(MouseButtonEvent event, boolean doubleClick) {
            action.run();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    private static final class ConfigSlider extends AbstractSliderButton {
        private final String key;
        private final double min;
        private final double max;
        private final DoubleSupplier getter;
        private final DoubleConsumer setter;
        private final Function<Double, String> formatter;

        private ConfigSlider(int x, int y, int width, int height, String key,
                double min, double max, DoubleSupplier getter, DoubleConsumer setter,
                Function<Double, String> formatter) {
            super(x, y, width, height, Component.empty(), (getter.getAsDouble() - min) / (max - min));
            this.key = key;
            this.min = min;
            this.max = max;
            this.getter = getter;
            this.setter = setter;
            this.formatter = formatter;
            updateMessage();
        }

        @Override protected void updateMessage() {
            setMessage(Component.translatable(key, formatter.apply(getter.getAsDouble())));
        }

        @Override protected void applyValue() {
            setter.accept(min + value * (max - min));
            updateMessage();
        }
    }
}
