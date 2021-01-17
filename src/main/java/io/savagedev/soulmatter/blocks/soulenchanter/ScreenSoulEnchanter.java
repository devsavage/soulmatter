package io.savagedev.soulmatter.blocks.soulenchanter;

/*
 * ScreenSoulEnchanter.java
 * Copyright (C) 2020 Savage - github.com/devsavage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.mojang.blaze3d.matrix.MatrixStack;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ScreenSoulEnchanter extends ContainerScreen<ContainerSoulEnchanter>
{
    private static final ResourceLocation background = new ResourceLocation(ModReference.MOD_ID, "textures/gui/gui_soul_enchanter.png");
    private final PlayerInventory playerInventory;

    public ScreenSoulEnchanter(ContainerSoulEnchanter screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.playerInventory = inv;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        String title = this.getTitle().getString();

        this.font.drawString(matrixStack, title, (float) (this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 4210752);
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), this.xSize - 58, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.getMinecraft().getTextureManager().bindTexture(background);

        int xStart = (this.width - this.xSize) / 2;
        int yStart = (this.height - this.ySize) / 2;

        this.blit(matrixStack, xStart, yStart, 0, 0, this.xSize, this.ySize);

        ContainerSoulEnchanter containerSoulEnchanter = this.getContainer();

        int bufferScale = container.getFuelLeftScaled(39);
        this.blit(matrixStack, xStart + 10, yStart + 57 - bufferScale + 1, 0, 206 - bufferScale, 12, bufferScale);

        if(containerSoulEnchanter.isInfusing()) {
            int progressScaled = container.getInfuseProgressScaled(24);
            this.blit(matrixStack, xStart + 74, yStart + 35, 176, 0, progressScaled + 1, 16);
        }
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        ContainerSoulEnchanter container = this.getContainer();

        if (mouseX > this.guiLeft + 10 && mouseX < this.guiLeft + 23 && mouseY > this.guiTop + 19 && mouseY < this.guiTop + 60) {
            this.renderTooltip(matrixStack, new StringTextComponent(container.getFuelStored() + " / " + container.getFuelCapacity()), mouseX, mouseY);
        }
    }
}
