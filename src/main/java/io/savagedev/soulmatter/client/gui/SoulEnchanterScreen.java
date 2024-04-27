package io.savagedev.soulmatter.client.gui;

/*
 * SoulEnchanterScreen.java
 * Copyright (C) 2014 - 2024 Savage - github.com/devsavage
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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

//public class SoulEnchanterScreen extends AbstractContainerScreen<SoulEnchanterContainerMenu>
//{
//    private static final ResourceLocation background = new ResourceLocation(ModReference.MOD_ID, "textures/gui/gui_soul_enchanter.png");
//
//    public SoulEnchanterScreen(SoulEnchanterContainerMenu screenContainer, Inventory inventory, Component titleIn) {
//        super(screenContainer, inventory, titleIn);
//
//        this.imageWidth = 176;
//        this.imageHeight = 166;
//    }
//
//    @Override
//    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
//        this.renderBackground(guiGraphics);
//        super.render(guiGraphics, mouseX, mouseY, delta);
//        this.renderTooltip(guiGraphics, mouseX, mouseY);
//    }
//
//    @Override
//    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, background);
//
//        int xStart = (this.width - this.imageWidth) / 2;
//        int yStart = (this.height - this.imageHeight) / 2;
//
//        guiGraphics.blit(background, leftPos, topPos, 0, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
//
//        SoulEnchanterContainerMenu soulEnchanterMenu = this.getMenu();
//
//        int bufferScale = soulEnchanterMenu.getFuelLeftScaled(39);
//
//        guiGraphics.blit(background, xStart + 10, yStart + 57 - bufferScale + 1, 0, 206 - bufferScale, 12, bufferScale);
//
//        if(soulEnchanterMenu.isInfusing()) {
//            int progressScaled = soulEnchanterMenu.getInfuseProgressScaled(24);
//            guiGraphics.blit(background, xStart + 74, yStart + 35, 176, 0, progressScaled + 1, 16);
//        }
//    }
//
//    @Override
//    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
//        String title = this.getTitle().getString();
//
//        guiGraphics.drawString(this.font, title, (float) (this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, 4210752, true);
//        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.imageWidth - 58, this.imageHeight - 96 + 2, 4210752);
//    }
//
//    @Override
//    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
//        super.renderTooltip(guiGraphics, mouseX, mouseY);
//
//        SoulEnchanterContainerMenu soulEnchanterMenu = this.getMenu();
//
//        if(mouseX > this.getGuiLeft() + 10 && mouseX < this.getGuiLeft() + 23 && mouseY > this.getGuiTop() + 19 && mouseY < this.getGuiTop() + 60) {
//            guiGraphics.renderTooltip(this.font, Component.nullToEmpty(soulEnchanterMenu.getFuelStored() + " / " + soulEnchanterMenu.getFuelCapacity()), mouseX, mouseY);
//        }
//    }
//}
