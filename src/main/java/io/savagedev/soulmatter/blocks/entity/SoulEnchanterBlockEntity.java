package io.savagedev.soulmatter.blocks.entity;

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
import io.savagedev.savagecore.nbt.NBTHelper;
import io.savagedev.soulmatter.init.ModBlockEntities;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.menus.SoulEnchanterContainerMenu;
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulEnchanterBlockEntity extends BaseContainerBlockEntity
{
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(3);
    private int progress;
    private int totalFuelStored;
    protected static int fuelCapacity = 14000;
    public static int operationTime = 5;
    public static int operationCost = 200;

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
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    SoulEnchanterBlockEntity.this.progress = value;
                    break;
                case 1:
                    SoulEnchanterBlockEntity.this.totalFuelStored = value;
                    break;
            }
        }

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
        return operationTime;
    }

    public static int getOperationCost() {
        return operationCost;
    }

    private int getFuelStored() {
        return this.totalFuelStored;
    }

    private void setFuelStored(int i) {
        if(this.getFuelStored() >= getFuelCapacity()) {
            this.totalFuelStored = getFuelCapacity();
        }

        this.totalFuelStored = i;
    }

    private boolean isFuelFull() {
        return this.getFuelStored() >= getFuelCapacity();
    }

    public static int getFuelCapacity() {
        return fuelCapacity;
    }

    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    private boolean canInfuse() {
        if(this.getFuelStored() >= getOperationCost()) {
            return !this.getInventory().getStackInSlot(1).isEmpty() &&
                    this.getInventory().getStackInSlot(1).getItem() == ModItems.RAW_SOUL_MATTER.get() &&
                    !this.getInventory().getStackInSlot(0).isEmpty() &&
                    this.getInventory().getStackInSlot(0).getItem() == ModItems.SOUL_STEALER.get();
        }

        return false;
    }

    private void infuse() {
        ItemStack outputStack = new ItemStack(ModItems.SOUL_MATTER.get());

        this.getInventory().extractItemSuper(1, 1, false);

        if(this.getInventory().getStackInSlot(2).isEmpty()) {
            this.getInventory().setStackInSlot(2, outputStack.copy());
        } else {
            this.getInventory().getStackInSlot(2).grow(outputStack.getCount());
        }
    }

    public static void serverTick(Level world, BlockPos blockPos, BlockState blockState, SoulEnchanterBlockEntity soulEnchanterBlock) {
        boolean dirty = false;

        if(world.isClientSide) {
            return;
        }

        if(!soulEnchanterBlock.getInventory().getStackInSlot(0).isEmpty()) {
            ItemStack soulStealer = soulEnchanterBlock.getInventory().getStackInSlot(0);
            if(NBTHelper.hasTag(soulStealer, ModNames.Tags.SOULS_TAKEN)) {
                final int count = NBTHelper.getInt(soulStealer, ModNames.Tags.SOULS_TAKEN);

                if(!soulEnchanterBlock.isFuelFull()) {
                    for(int i = 0; i < count; i++) {
                        soulEnchanterBlock.totalFuelStored += 1;
                    }

                    if(count > 0) {
                        NBTHelper.setInt(soulStealer, ModNames.Tags.SOULS_TAKEN, count - 1);
                    } else {
                        NBTHelper.setInt(soulStealer, ModNames.Tags.SOULS_TAKEN, 0);
                    }
                }

                dirty = true;
            }
        }

        if(soulEnchanterBlock.canInfuse()) {
            soulEnchanterBlock.progress++;

            soulEnchanterBlock.totalFuelStored--;

            if(soulEnchanterBlock.progress >= getOperationTime()) {
                soulEnchanterBlock.infuse();
                soulEnchanterBlock.progress = 0;

                if(soulEnchanterBlock.getFuelStored() <= 0) {
                    soulEnchanterBlock.setFuelStored(0);
                }

                dirty = true;
            }
        } else {
            if(soulEnchanterBlock.progress > 0) {
                soulEnchanterBlock.progress = 0;
                dirty = true;
            }
        }

        if(dirty) {
            setChanged(world, blockPos, blockState);
        }
    }

    protected static void setChanged(Level world, BlockPos blockPos, BlockState blockState) {
        world.blockEntityChanged(blockPos);
        if (!blockState.isAir()) {
            world.updateNeighbourForOutputSignal(blockPos, blockState.getBlock());
        }
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
        return SoulEnchanterContainerMenu.create(this, inventory, windowId);
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
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.getInventory().getStacks(), slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotIndex) {
        return ContainerHelper.takeItem(this.getInventory().getStacks(), slotIndex);
    }

    @Override
    public void setItem(int slotIndex, ItemStack stack) {
        ItemStack itemstack = this.getInventory().getStacks().get(slotIndex);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
        this.getInventory().getStacks().set(slotIndex, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if(slotIndex == 0 && !flag) {
            this.progress = 0;
            this.setChanged();
        }
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
        this.getInventory().getStacks().clear();
    }
}
