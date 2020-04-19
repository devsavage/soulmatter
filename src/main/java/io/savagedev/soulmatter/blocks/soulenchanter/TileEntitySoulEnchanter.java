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
import io.savagedev.soulmatter.helpers.ModItemStackHandler;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.init.ModTileEntities;
import io.savagedev.soulmatter.util.ModNames;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntitySoulEnchanter extends TileEntity implements INamedContainerProvider, ITickableTileEntity
{
    private final ModItemStackHandler inventory = new ModItemStackHandler(3);
    private int progress;
    protected static int operationTime = 5;

    private final IIntArray data = new IIntArray()
    {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TileEntitySoulEnchanter.this.getProgress();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int size() {
            return 1;
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

        if(canInfuse()) {
            if(getSurroundingMonsters().size() >= 4) {
                killSurroundingMonsters();

                this.progress++;

                if(this.progress >= this.getOperationTime()) {
                    infuse();
                    this.progress = 0;
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
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.progress = compound.getInt("Progress");
        ItemStackHelper.loadAllItems(compound, this.getInventory().getStacks());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Progress", this.getProgress());
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

    public ModItemStackHandler getInventory() {
        return this.inventory;
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        BlockPos pos = this.getPos();

        return player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    // TODO: Check for soul infuser here
    private boolean canInfuse() {
        if(!this.inventory.getStackInSlot(1).isEmpty() && this.inventory.getStackInSlot(1).getItem() == ModItems.RAW_SOUL_MATTER.get()) {
            return true;
        }

        return false;
    }

    private List<MonsterEntity> getSurroundingMonsters() {
        BlockPos tileBlock = new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(tileBlock);

        return this.getWorld().getEntitiesWithinAABB(MonsterEntity.class, axisAlignedBB.grow(6, 0, 6));
    }

    private void killSurroundingMonsters() {
        List<MonsterEntity> mobs = this.getSurroundingMonsters();

        for(MonsterEntity monster : mobs) {
            monster.attackEntityFrom(DamageSource.GENERIC, 200);
        }
    }

    private void infuse() {
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

    protected static int getOperationTime() {
        return operationTime;
    }
}
