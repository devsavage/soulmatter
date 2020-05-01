package io.savagedev.soulmatter.blocks.soulpresser;

/*
 * ContainerSoulPresser.java
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

import io.savagedev.soulmatter.blocks.soulenchanter.ContainerSoulEnchanter;
import io.savagedev.soulmatter.blocks.soulenchanter.TileEntitySoulEnchanter;
import io.savagedev.soulmatter.helpers.ModItemStackHandler;
import io.savagedev.soulmatter.init.ModContainers;
import io.savagedev.soulmatter.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class ContainerSoulPresser extends Container
{
    private final Function<PlayerEntity, Boolean> isUsableByPlayer;
    private final ItemStackHandler inventory;

    public ContainerSoulPresser(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory) {
        this(type, id, playerInventory, p -> false, (new TileEntitySoulEnchanter()).getInventory());
    }

    private ContainerSoulPresser(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, ModItemStackHandler inv) {
        super(type, id);

        this.isUsableByPlayer = isUsableByPlayer;
        this.inventory = inv;

        this.addSlot(new SlotSoulMatter(inv, 0, 80, 34));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public static ContainerSoulPresser create(int windowId, PlayerInventory playerInventory) {
        return new ContainerSoulPresser(ModContainers.SOUL_PRESSER.get(), windowId, playerInventory);
    }

    public static ContainerSoulPresser create(int windowId, PlayerInventory playerInventory, Function<PlayerEntity, Boolean> isUsableByPlayer, ModItemStackHandler inventory) {
        return new ContainerSoulPresser(ModContainers.SOUL_PRESSER.get(), windowId, playerInventory, isUsableByPlayer, inventory);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex <  1) {
                if (!this.mergeItemStack(itemstack1, 0, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 0, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.isUsableByPlayer.apply(playerIn);
    }

    private final class SlotSoulMatter extends SlotItemHandler
    {
        private final ModItemStackHandler inventory;
        private final int index;

        public SlotSoulMatter(ModItemStackHandler inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
            this.inventory = inventoryIn;
            this.index = index;
        }

        @Override
        public boolean canTakeStack(PlayerEntity playerIn) {
            return !this.inventory.extractItemSuper(this.index, 1, true).isEmpty();
        }

        @Nonnull
        @Override
        public ItemStack decrStackSize(int amount) {
            return this.inventory.extractItemSuper(this.index, amount, false);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == ModItems.SOUL_MATTER.get();
        }
    }
}
