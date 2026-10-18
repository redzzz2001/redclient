package dev.redzzz.xray;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class FriendManagerScreen extends Screen {
    private final Screen parent;
    private EditBox nameBox;
    private FriendsList friendsList;

    public FriendManagerScreen(Screen parent) {
        super(Component.translatable("screen.xraybasic.friends_title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        addRenderableWidget(new StringWidget(centerX - 100, 8, 200, 20, title, font));

        nameBox = addRenderableWidget(new EditBox(font, centerX - 100, 32, 200, 20,
                Component.translatable("screen.xraybasic.friend_name")));
        nameBox.setHint(Component.translatable("screen.xraybasic.friend_name"));
        nameBox.setMaxLength(32);

        addRenderableWidget(Button.builder(Component.translatable("screen.xraybasic.friend_add"), button -> {
            if (FriendManager.add(nameBox.getValue())) {
                nameBox.setValue("");
                friendsList.refresh();
            }
        }).bounds(centerX - 100, 58, 98, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("screen.xraybasic.friend_remove"), button -> {
            if (FriendManager.remove(nameBox.getValue())) {
                nameBox.setValue("");
                friendsList.refresh();
            }
        }).bounds(centerX + 2, 58, 98, 20).build());

        friendsList = addRenderableWidget(new FriendsList(width, Math.max(35, height - 120), 84));
        friendsList.refresh();

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(centerX - 100, height - 28, 200, 20).build());
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        extractTransparentBackground(graphics);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private final class FriendsList extends ContainerObjectSelectionList<FriendEntry> {
        private FriendsList(int width, int height, int y) {
            super(FriendManagerScreen.this.minecraft, width, height, y, 24);
            centerListVertically = false;
        }

        private void refresh() {
            clearEntries();
            for (String name : FriendManager.names()) {
                addEntry(new FriendEntry(name));
            }
        }

        @Override
        public int getRowWidth() {
            return 220;
        }
    }

    private final class FriendEntry extends ContainerObjectSelectionList.Entry<FriendEntry> {
        private final AbstractWidget removeButton;

        private FriendEntry(String name) {
            removeButton = Button.builder(Component.translatable("screen.xraybasic.friend_remove_entry", name), b -> {
                FriendManager.remove(name);
                friendsList.refresh();
            }).bounds(0, 0, 200, 20).build();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
                                   boolean hovered, float partialTick) {
            removeButton.setX(getContentXMiddle() - removeButton.getWidth() / 2);
            removeButton.setY(getContentY());
            removeButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(removeButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(removeButton);
        }
    }
}
