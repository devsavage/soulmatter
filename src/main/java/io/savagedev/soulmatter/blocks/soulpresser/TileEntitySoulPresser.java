package io.savagedev.soulmatter.blocks.soulpresser;

/*
 * TileEntitySoulPresser.java
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

import io.savagedev.savagecore.item.BaseItemStackHandler;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.init.ModTileEntities;
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TileEntitySoulPresser extends TileEntity implements INamedContainerProvider, ITickableTileEntity
{
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(1);

    public boolean activated;

    public TileEntitySoulPresser() {
        super(ModTileEntities.SOUL_PRESSER.get());
        this.activated = false;
    }

    @Override
    public void tick() {
        if(getActiveStatus()) {
            tryPress();
            setActiveStatus(false);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(ModNames.Containers.SOUL_PRESSER);
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity playerEntity) {
        return ContainerSoulPresser.create(windowId, playerInv, this::isUsableByPlayer, this.inventory);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        
        ItemStackHelper.loadAllItems(compound, this.getInventory().getStacks());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.getInventory().getStacks());

        return super.write(compound);
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        BlockPos pos = this.getPos();

        return player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    public BaseItemStackHandler getInventory() {
        return inventory;
    }

    public boolean getActiveStatus() {
        return this.activated;
    }

    public void setActiveStatus(boolean active) {
        this.activated = active;
    }

    public void tryPress() {
        if(!inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ModItems.SOUL_MATTER.get()) {
            ItemStack soulStack = new ItemStack(ModItems.SOUL_MATTER_COMPACT.get(), 1);
            this.inventory.extractItemSuper(0, 1, false);
            InventoryHelper.spawnItemStack(this.getWorld(), this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ(), soulStack);
        }
    }
}
