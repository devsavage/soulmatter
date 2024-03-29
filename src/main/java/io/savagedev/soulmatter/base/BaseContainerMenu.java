package io.savagedev.soulmatter.base;

/*
 * Parts of this file were taken from (https://github.com/TerraFirmaCraft/TerraFirmaCraft). See license below.
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseContainerMenu<T extends BaseContainerBlockEntity> extends AbstractContainerMenu
{
    protected final T blockEntity;

    public static BaseContainerMenu create(MenuType<?> type, int windowId, Inventory playerInv, BaseContainerBlockEntity blockEntity) {
        return new BaseContainerMenu(type, windowId, blockEntity).init(playerInv);
    }

    protected int containerSlots; // The number of slots in the container (not including the player inventory)

    protected BaseContainerMenu(MenuType<?> type, int windowId, T blockEntity) {
        super(type, windowId);

        this.blockEntity = blockEntity;
    }

    /**
     * Problem: calling add slots from the container superclass, means that we cannot access subclass parameters, such as an item fluid or tile entity, which are necessary in order to do some things such as setup container slots.
     * Solutions for running this at the right time are very difficult.
     * So, we have an explicit post-constructor-initialization method, which needs to be ran externally, but will always run after final fields have been initialized.
     *
     * @return The current container, casted down as required.
     */
    @SuppressWarnings("unchecked")
    public <C extends BaseContainerMenu> C init(Inventory playerInventory, int yOffset) {
        addContainerSlots();
        containerSlots = slots.size();
        addPlayerInventorySlots(playerInventory, yOffset);
        return (C) this;
    }

    public <C extends BaseContainerMenu> C init(Inventory playerInventory) {
        return init(playerInventory, 0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        final Slot slot = slots.get(index);

        // Only move a fluid when the index clicked has any contents
        if (slot.hasItem()) {
            final ItemStack stack = slot.getItem(); // The fluid in the current slot
            final ItemStack original = stack.copy(); // The original amount in the slot
            if (moveStack(stack, index)) {
                return ItemStack.EMPTY;
            }

            if (stack.getCount() == original.getCount()) {
                return ItemStack.EMPTY;
            }

            // Handle updates,
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(player, stack);
            return original;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return blockEntity.stillValid(playerIn);
    }

    /**
     * Handles the actual movement of stacks in {@link #quickMoveStack(Player, int)} with as little boilerplate as possible.
     * The default implementation only moves stacks between the main inventory and the hotbar.
     *
     * @return {@code true} if no movement is possible, or the result of {@code !moveItemStackTo(...) || ...}
     */
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex)) {
                    case CONTAINER -> true;
                    case HOTBAR -> !moveItemStackTo(stack, containerSlots, containerSlots + 27, false);
                    case MAIN_INVENTORY -> !moveItemStackTo(stack, containerSlots + 27, containerSlots + 36, false);
                };
    }

    /**
     * Adds container slots.
     * These are added before the player inventory (and as such, the player inventory will be shifted upwards by the number of slots added here.
     */
    protected void addContainerSlots() {}

    /**
     * Adds the player inventory slots to the container.
     */
    protected final void addPlayerInventorySlots(Inventory playerInv, int yOffset) {
        // Main Inventory. Indexes [0, 27)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yOffset));
            }
        }

        // Hotbar. Indexes [27, 36)
        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142 + yOffset));
        }
    }

    protected final IndexType typeOf(int index) {
        if (index < containerSlots) {
            return IndexType.CONTAINER;
        } else if (index < containerSlots + 27) {
            return IndexType.MAIN_INVENTORY;
        }

        return IndexType.HOTBAR;
    }

    public enum IndexType {
        CONTAINER,
        MAIN_INVENTORY,
        HOTBAR
    }

    @FunctionalInterface
    public interface Factory<T extends BlockEntity, C extends BaseContainerMenu>
    {
        C create(T tile, Inventory playerInventory, int windowId);
    }
}
