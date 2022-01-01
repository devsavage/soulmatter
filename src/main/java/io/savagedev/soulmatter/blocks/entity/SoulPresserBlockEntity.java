package io.savagedev.soulmatter.blocks.entity;

/*
 * SoulPresserBlockEntity.java
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
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.menus.SoulEnchanterContainerMenu;
import io.savagedev.soulmatter.menus.SoulPresserContainerMenu;
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulPresserBlockEntity extends BaseContainerBlockEntity
{
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(1);
    public boolean activated;

    public SoulPresserBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SOUL_PRESSER.get(), blockPos, blockState);
        this.activated = false;
    }

    public boolean getActiveStatus() {
        return this.activated;
    }

    public void setActiveStatus(boolean active) {
        this.activated = active;
    }

    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    public static void serverTick(Level world, BlockPos blockPos, BlockState blockState, SoulPresserBlockEntity soulPresser) {
        if(world.isClientSide) {
            return;
        }

        if(soulPresser.getActiveStatus()) {
            soulPresser.tryPress();
            soulPresser.setActiveStatus(false);
        }
    }

    private void tryPress() {
        if(!this.getInventory().getStackInSlot(0).isEmpty() && this.getInventory().getStackInSlot(0).getItem() == ModItems.SOUL_MATTER.get()) {
            ItemStack soulStack = new ItemStack(ModItems.SOUL_MATTER_COMPACT.get(), 1);
            this.getInventory().extractItemSuper(0, 2, false);
            Containers.dropItemStack(this.getLevel(), this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ(), soulStack);
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        ContainerHelper.loadAllItems(compoundTag, this.getInventory().getStacks());
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, this.getInventory().getStacks());

        return super.save(compoundTag);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent(ModNames.Menus.SOUL_PRESSER);
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory inventory) {
        return SoulPresserContainerMenu.create(this, inventory, windowId);
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
    public ItemStack getItem(int slotIndex) {
        return this.getInventory().getStackInSlot(slotIndex);
    }

    @Override
    public ItemStack removeItem(int slotIndex, int amount) {
        return ContainerHelper.removeItem(this.getInventory().getStacks(), slotIndex, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotIndex) {
        return ContainerHelper.takeItem(this.getInventory().getStacks(), slotIndex);
    }

    @Override
    public void setItem(int slotIndex, ItemStack stack) {
        boolean flag = !stack.isEmpty() && stack.sameItem(stack) && ItemStack.tagMatches(stack, stack);

        this.getInventory().getStacks().set(slotIndex, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if(slotIndex == 0 && !flag) {
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
