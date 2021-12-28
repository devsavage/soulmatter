package io.savagedev.soulmatter.blocks;

/*
 * SoulEnchanterMenu.java
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
import io.savagedev.soulmatter.helpers.BaseContainerMenu;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.init.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;


public class SoulEnchanterMenu extends BaseContainerMenu
{
    private SoulEnchanterBlockEntity soulEnchanter;
    private final BaseItemStackHandler inventory;
    private final ContainerData containerData;

    public static SoulEnchanterMenu create(SoulEnchanterBlockEntity soulEnchanter, Inventory playerInv, int windowId) {
        return new SoulEnchanterMenu(windowId, soulEnchanter).init(playerInv);
    }

    protected SoulEnchanterMenu(int windowId, SoulEnchanterBlockEntity soulEnchanter) {
        super(ModMenus.SOUL_ENCHANTER.get(), windowId);

        this.soulEnchanter = soulEnchanter;
        this.inventory = soulEnchanter.getInventory();
        this.containerData = soulEnchanter.getContainerData();
    }

    public int getProgress() {
        return this.containerData.get(0);
    }

    public int getFuelStored() {
        return this.containerData.get(1);
    }

    public boolean isInfusing() {
        return this.getProgress() > 0;
    }

    protected int getFuelCapacity() {
        return SoulEnchanterBlockEntity.getFuelCapacity();
    }

    public int getInfuseProgressScaled(int pixels) {
        int i = this.getProgress();
        int j = SoulEnchanterBlockEntity.getOperationTime();

        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFuelLeftScaled(int scale) {
        double stored = this.getFuelStored();
        double max = this.getFuelCapacity();
        double value = ((stored /max) * scale);

        return (int)value;
    }

    @Override
    protected void addContainerSlots() {
        this.addSlot(new SlotSoulStealer(this.inventory, 0, 8, 62));
        this.addSlot(new SlotInput(this.inventory, 1, 49, 35));
        this.addSlot(new SlotSoulStealer(this.inventory, 2, 111, 35));
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.soulEnchanter.stillValid(playerIn);
    }

    private final class SlotSoulStealer extends SlotItemHandler
    {
        private final BaseItemStackHandler inventory;
        private final int slotIndex;

        public SlotSoulStealer(BaseItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.inventory = itemHandler;
            this.slotIndex = index;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return !this.inventory.extractItemSuper(this.slotIndex, 1, true).isEmpty();
        }

        @Nonnull
        @Override
        public ItemStack remove(int amount) {
            return this.inventory.extractItemSuper(this.slotIndex, amount, false);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return stack.getItem() == ModItems.SOUL_STEALER.get();
        }
    }

    private final class SlotInput extends SlotItemHandler
    {
        private final BaseItemStackHandler inventory;
        private final int slotIndex;

        public SlotInput(BaseItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.inventory = itemHandler;
            this.slotIndex = index;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return !this.inventory.extractItemSuper(this.slotIndex, 1, true).isEmpty();
        }

        @Nonnull
        @Override
        public ItemStack remove(int amount) {
            return this.inventory.extractItemSuper(this.slotIndex, amount, false);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return stack.getItem() == ModItems.RAW_SOUL_MATTER.get();
        }
    }

    private final class SlotOutput extends SlotItemHandler
    {
        private final BaseItemStackHandler inventory;
        private final int slotIndex;

        public SlotOutput(BaseItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.inventory = itemHandler;
            this.slotIndex = index;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return !this.inventory.extractItemSuper(this.slotIndex, 1, true).isEmpty();
        }

        @Nonnull
        @Override
        public ItemStack remove(int amount) {
            return this.inventory.extractItemSuper(this.slotIndex, amount, false);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }
    }
}
