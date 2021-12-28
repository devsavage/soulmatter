package io.savagedev.soulmatter.blocks;

/*
 * SoulEnchanterBlockEntity.java
 * Copyright (C) 2014 - 2021 Savage - github.com/devsavage
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
import io.savagedev.soulmatter.init.ModBlockEntities;
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SoulEnchanterBlockEntity extends BaseContainerBlockEntity
{
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(3);
    private int progress;
    private int totalFuelStored;
    protected static int fuelCapacity = 14000;
    public static int operationTime = 5;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return SoulEnchanterBlockEntity.this.getProgress();
                case 1:
                    return SoulEnchanterBlockEntity.this.getFuelStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() {
            return 2;
        }
    };

    public SoulEnchanterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SOUL_ENCHANTER.get(), blockPos, blockState);
        this.getInventory().setOutputSlots(this.containerData.getCount());
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    private int getProgress() {
        return this.progress;
    }

    public static int getOperationTime() {
        return 200;
    }

    public static int getOperationCost() {
        return 100;
    }

    private int getFuelStored() {
        return this.totalFuelStored;
    }

    private void setFuelStored(int i) {
        this.totalFuelStored = i;
    }

    private boolean isFuelFull() {
        return this.getFuelStored() >= getFuelCapacity();
    }

    protected static int getFuelCapacity() {
        return fuelCapacity;
    }

    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.progress = compoundTag.getInt("Progress");
        this.totalFuelStored = compoundTag.getInt("FuelStored");

        ContainerHelper.loadAllItems(compoundTag, this.getInventory().getStacks());
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag = super.save(compoundTag);
        compoundTag.putInt("Progress", this.getProgress());
        compoundTag.putInt("FuelStored", this.getFuelStored());

        ContainerHelper.saveAllItems(compoundTag, this.getInventory().getStacks());

        return super.save(compoundTag);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent(ModNames.Menus.SOUL_ENCHANTER);
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory inventory) {
        return SoulEnchanterMenu.create(this, inventory, windowId);
    }

    @Override
    public int getContainerSize() {
        return this.getInventory().getStacks().size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.getInventory().getStacks()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.getInventory().getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int index) {
        return ContainerHelper.removeItem(this.getInventory().getStacks(), slot, index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return ContainerHelper.takeItem(this.getInventory().getStacks(), p_18951_);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {

    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {

    }
}
