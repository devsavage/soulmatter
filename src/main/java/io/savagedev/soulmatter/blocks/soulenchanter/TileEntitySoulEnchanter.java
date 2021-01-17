package io.savagedev.soulmatter.blocks.soulenchanter;

/*
 * TileEntitySoulEnchanter.java
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

import com.google.common.collect.Lists;
import io.savagedev.savagecore.item.BaseItemStackHandler;
import io.savagedev.soulmatter.SoulMatter;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.init.ModTileEntities;
import io.savagedev.soulmatter.util.LogHelper;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.savagecore.nbt.NBTHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TileEntitySoulEnchanter extends TileEntity implements INamedContainerProvider, ITickableTileEntity
{
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(3);
    private int progress;
    private int totalFuelStored;
    protected static int fuelCapacity = 14000;
    public static int operationTime = 5;

    private final IIntArray data = new IIntArray()
    {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TileEntitySoulEnchanter.this.getProgress();
                case 1:
                    return TileEntitySoulEnchanter.this.getFuelStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int size() {
            return 2;
        }
    };

    public TileEntitySoulEnchanter() {
        super(ModTileEntities.SOUL_ENCHANTER.get());
        this.inventory.setOutputSlots(2);
    }

    @Override
    public void tick() {
        boolean dirty = false;

        World world = this.getWorld();
        if (world == null || world.isRemote())
            return;

        if(!this.inventory.getStackInSlot(0).isEmpty()) {
            ItemStack soulStealer = this.inventory.getStackInSlot(0).getStack();

            if(NBTHelper.hasTag(soulStealer, "SoulsTaken")) {
                final int count = NBTHelper.getInt(soulStealer, "SoulsTaken");

                if(!isFuelFull()) {
                    for(int i = 0; i < count; i++) {
                        this.totalFuelStored += 1;
                    }

                    if(count > 0)
                        NBTHelper.setInt(soulStealer, "SoulsTaken", count - 1);
                    else
                        NBTHelper.setInt(soulStealer, "SoulsTaken", 0);
                }

                dirty = true;
            }
        }

        if(canInfuse()) {
            this.progress++;
            int fuelAfterComplete = this.totalFuelStored - this.getOperationCost();
            this.totalFuelStored--;

            if(this.progress >= getOperationTime()) {
                infuse();
                this.progress = 0;


                if(this.getFuelStored() <= 0) {
                    this.setFuelStored(0);
                } else {
                    this.totalFuelStored = fuelAfterComplete;
                }
            }

            dirty = true;
        } else {
            if(this.progress > 0) {
                this.progress = 0;
                dirty = true;
            }
        }

        if(dirty) {
            this.markDirty();
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.progress = compound.getInt("Progress");
        this.totalFuelStored = compound.getInt("FuelStored");
        ItemStackHelper.loadAllItems(compound, this.getInventory().getStacks());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Progress", this.getProgress());
        compound.putInt("FuelStored", this.getFuelStored());
        ItemStackHelper.saveAllItems(compound, this.getInventory().getStacks());

        return super.write(compound);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(ModNames.Containers.SOUL_ENCHANTER);
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity p_createMenu_3_) {
        return ContainerSoulEnchanter.create(windowId, playerInv, this::isUsableByPlayer, this.inventory, this.data);
    }

    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        BlockPos pos = this.getPos();

        return player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    private boolean canInfuse() {
        if(this.getFuelStored() >= getOperationCost()) {
            return !this.inventory.getStackInSlot(1).isEmpty() &&
                    this.inventory.getStackInSlot(1).getItem() == ModItems.RAW_SOUL_MATTER.get() &&
                    !this.inventory.getStackInSlot(0).isEmpty() &&
                    this.inventory.getStackInSlot(0).getItem() == ModItems.SOUL_STEALER.get();
        }

        return false;
    }

    private void infuse() {
        ItemStack soulStealerStack = this.inventory.getStackInSlot(0).getStack();
        ItemStack outputStack = new ItemStack(ModItems.SOUL_MATTER.get());

        this.inventory.extractItemSuper(1, 1, false);

        if(this.inventory.getStackInSlot(2).isEmpty()) {
            this.inventory.setStackInSlot(2, outputStack.copy());
        } else {
            this.inventory.getStackInSlot(2).grow(outputStack.getCount());
        }
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
}
