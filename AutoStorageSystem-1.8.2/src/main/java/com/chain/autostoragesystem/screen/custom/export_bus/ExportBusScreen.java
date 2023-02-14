package com.chain.autostoragesystem.screen.custom.export_bus;

import com.chain.autostoragesystem.ModMain;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class ExportBusScreen extends AbstractContainerScreen<ExportBusMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModMain.MOD_ID, "textures/gui/bus.png");

    /**
     * Amount scrolled in Creative mode inventory (0 = top, 1 = bottom)
     */
    private float scrollOffs;
    /**
     * True if the scrollbar is being dragged
     */
    private boolean scrolling;

    public ExportBusScreen(ExportBusMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 195;
        this.imageHeight = 168;

        this.menu.scrollTo(0.0F);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
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
            int i = this.menu.getFoo();
            float f = (float) (pDelta / (double) i);
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.menu.scrollTo(this.scrollOffs);
            return true;
        }
    }

    private boolean canScroll() {
        return this.menu.canScroll();
    }
}
