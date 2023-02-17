package com.chain.autostoragesystem.screen.custom.storage_terminal;

import com.chain.autostoragesystem.ModMain;
import com.chain.autostoragesystem.api.Side;
import com.chain.autostoragesystem.network.ModMessages;
import com.chain.autostoragesystem.network.packet.MenuScrollPosPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class StorageTerminalScreen extends AbstractContainerScreen<StorageTerminalMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModMain.MOD_ID, "textures/gui/storage_terminal.png");

    private final Side side;

    /**
     * Amount scrolled in Creative mode inventory (0 = top, 1 = bottom)
     */
    private float scrollOffs;
    /**
     * True if the scrollbar is being dragged
     */
    private boolean scrolling;

    public StorageTerminalScreen(StorageTerminalMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 194;
        this.imageHeight = 202;

        side = Side.get(pPlayerInventory.player.getLevel());
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        // pDelta
        // < 0 going down
        // > 0 going up

        if (!this.canScroll()) {
            return false;
        } else {
            int i = this.menu.getScrollIndex();
            float f = (float) (pDelta / (double) i);
            float scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);

            if (scrollOffs == this.scrollOffs) {
                return false;
            }

            this.scrollOffs = scrollOffs;
            this.menu.scrollTo(this.scrollOffs);

            if (side == Side.CLIENT) {
                ModMessages.sendToServer(new MenuScrollPosPacket(this.scrollOffs));
            }
            return true;
        }
    }

    private boolean canScroll() {
        return this.menu.canScroll();
    }
}
