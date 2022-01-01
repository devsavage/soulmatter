package io.savagedev.soulmatter.menus;

/*
 * SoulPresserContainerMenu.java
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
import io.savagedev.soulmatter.base.BaseContainerMenu;
import io.savagedev.soulmatter.blocks.entity.SoulPresserBlockEntity;
import io.savagedev.soulmatter.init.ModContainerMenus;
import io.savagedev.soulmatter.init.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SoulPresserContainerMenu extends BaseContainerMenu<SoulPresserBlockEntity>
{
    private SoulPresserBlockEntity soulPresserBlockEntity;
    private final BaseItemStackHandler inventory;

    public static SoulPresserContainerMenu create(SoulPresserBlockEntity soulPresserBlockEntity, Inventory playerInv, int windowId) {
        return new SoulPresserContainerMenu(windowId, soulPresserBlockEntity).init(playerInv);
    }

    protected SoulPresserContainerMenu(int windowId, SoulPresserBlockEntity soulPresserBlockEntity) {
        super(ModContainerMenus.SOUL_PRESSER.get(), windowId, soulPresserBlockEntity);

        this.soulPresserBlockEntity = soulPresserBlockEntity;
        this.inventory = soulPresserBlockEntity.getInventory();
    }

    @Override
    protected void addContainerSlots() {
        this.addSlot(new SlotSoulMatter(this.inventory, 0, 80, 34));
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.soulPresserBlockEntity.stillValid(playerIn);
    }

    private final class SlotSoulMatter extends SlotItemHandler
    {
        private final BaseItemStackHandler inventory;
        private final int index;

        public SlotSoulMatter(BaseItemStackHandler inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);

            this.inventory = inventoryIn;
            this.index = index;
        }

        @Override
        public boolean mayPickup(Player player) {
            return !this.inventory.extractItemSuper(this.index, 1, true).isEmpty();
        }

        @Nonnull
        @Override
        public ItemStack remove(int amount) {
            return this.inventory.extractItemSuper(this.index, amount, false);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return stack.getItem() == ModItems.SOUL_MATTER.get();
        }
    }
}
