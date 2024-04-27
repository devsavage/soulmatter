package io.savagedev.soulmatter.handlers;

/*
 * SMToolLevelHandler.java
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

import io.savagedev.savagecore.nbt.NBTHelper;
import io.savagedev.soulmatter.init.ModConfig;
import io.savagedev.soulmatter.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SMToolLevelHandler
{
    public static final String SOUL_TOOL_TAG = "SoulTool";
    public static final String SOUL_TOOL_TAG_XP = "SoulToolXP";
    public static final String SOUL_TOOL_TAG_LEVEL = "SoulToolLevel";
    public static final int SOUL_TOOL_MAX_LEVEL = ModConfig.MAX_SOUL_TOOL_LEVEL.get();

    public static int getToolLevel(ItemStack soulTool) {
        if (soulTool.getTag() == null) {
            soulTool.setTag(new CompoundTag());
        }

        if (!soulTool.getTag().contains(SOUL_TOOL_TAG_LEVEL)) {
            soulTool.getTag().putInt(SOUL_TOOL_TAG_LEVEL, 0);
        }

        return soulTool.getTag().getInt(SOUL_TOOL_TAG_LEVEL);
    }

    public static long getToolXp(ItemStack soulTool) {
        if (soulTool.getTag() == null) {
            soulTool.setTag(new CompoundTag());
        }

        if (!soulTool.getTag().contains(SOUL_TOOL_TAG_XP)) {
            soulTool.getTag().putLong(SOUL_TOOL_TAG_XP, 0L);
        }

        return soulTool.getTag().getLong(SOUL_TOOL_TAG_XP);
    }

    public static boolean hasXp(ItemStack tool) {
        return tool.hasTag() && tool.getTag().contains(SOUL_TOOL_TAG_XP);
    }

    public static boolean hasToolLevel(ItemStack soulTool) {
        return soulTool.hasTag() && soulTool.getTag().contains(SOUL_TOOL_TAG_LEVEL);
    }

    public static void addLevelTag(ItemStack stack) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }

        if (!stack.getTag().contains(SOUL_TOOL_TAG_LEVEL)) {
            stack.getTag().putInt(SOUL_TOOL_TAG_LEVEL, 1);
        }

        if (!stack.getTag().contains(SOUL_TOOL_TAG_XP)) {
            stack.getTag().putLong(SOUL_TOOL_TAG_XP, 0L);
        }
    }

    public static boolean hasLevelTags(ItemStack soulTool) {
        return soulTool.hasTag() && soulTool.getTag().contains(SOUL_TOOL_TAG_LEVEL) && soulTool.getTag().contains(SOUL_TOOL_TAG_XP);
    }

    public static boolean isMaxToolLevel(ItemStack soulTool) {
        if(!hasLevelTags(soulTool))
            return false;

        return getToolLevel(soulTool) >= SOUL_TOOL_MAX_LEVEL;
    }

    public static int getRequiredXp(ItemStack tool) {
        float baseXp = 100F;

        if (tool.getItem() == ModItems.SOUL_MATTER_SWORD.get())
            baseXp *= 5.5F;
        if (tool.getItem() == ModItems.SOUL_MATTER_SHOVEL.get())
            baseXp *= 3.5F;
        if (tool.getItem() == ModItems.SOUL_MATTER_AXE.get())
            baseXp *= 3.5F;
        if (tool.getItem() == ModItems.SOUL_MATTER_HOE.get())
            baseXp *= 4.5F;
        if (tool.getItem() == ModItems.SOUL_MATTER_PICKAXE.get())
            baseXp *= 4.5F;
        if (tool.getItem() == ModItems.SOUL_MATTER_HAMMER.get())
            baseXp *= 5.5F;

        baseXp *= 150 / 100F;

        int toolLevel = getToolLevel(tool);
        if (toolLevel >= 1)
            baseXp *= Math.pow(1.15F, toolLevel - 1);

        return Math.round(baseXp);
    }

    public static void addXp(ItemStack soulTool, Player player, long xPToAdd) {
        if(player.isCreative())
            return;

        if(soulTool == null || !soulTool.hasTag())
            return;

        if(!hasToolLevel(soulTool) || !hasXp(soulTool))
            return;

        long newToolXp = -1L;

        if(hasXp(soulTool))
            newToolXp = getToolXp(soulTool) + xPToAdd;

        updateToolXp(soulTool, player, newToolXp);
    }

    public static void updateToolXp(ItemStack soulTool, Player player, Long newToolXp) {
        if(!hasToolLevel(soulTool))
            return;

        int toolLevel = getToolLevel(soulTool);
        boolean levelUpTool = false;

        if(newToolXp >= 0 && hasXp(soulTool) && toolLevel > 0 && !isMaxToolLevel(soulTool)) {
            soulTool.getTag().putLong(SOUL_TOOL_TAG_XP, newToolXp);

            if(newToolXp >= getRequiredXp(soulTool)) {
                increaseToolLevel(soulTool, player);
                levelUpTool = true;
            }
        }

        if((levelUpTool) && !player.level().isClientSide) {
            int level = getToolLevel(soulTool);

            if(isMaxToolLevel(soulTool))
                triggerMaxLevel(soulTool, player);
            else {
                player.sendSystemMessage(Component.translatable("Your " +
                        ChatFormatting.DARK_AQUA + soulTool.getDisplayName().getString() +
                        ChatFormatting.RESET + " has been leveled up to level " + ChatFormatting.AQUA + level + ChatFormatting.RESET + "!"));
                player.playNotifySound(SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.AMBIENT, 1.0F, 1.0F);
            }
        }
    }

    public static void increaseToolLevel(ItemStack soulTool, Player playerEntity) {
        int level = getToolLevel(soulTool);
        level++;

        soulTool.getTag().putInt(SOUL_TOOL_TAG_LEVEL, level);
        soulTool.getTag().putLong(SOUL_TOOL_TAG_XP, 0L);
    }

    public static void triggerMaxLevel(ItemStack tool, Player player) {
        player.sendSystemMessage(Component.translatable(ChatFormatting.GOLD + "You have reached the max level for your " + ChatFormatting.AQUA +
                tool.getDisplayName().getString() + ChatFormatting.GOLD + "!"));
        player.playNotifySound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.AMBIENT, 1.0F, 1.0F);
    }

    public static void setMaxLevelCreative(ItemStack soulTool, Player playerEntity) {
        if(!hasToolLevel(soulTool))
            return;

        if (!soulTool.getTag().contains(SOUL_TOOL_TAG_LEVEL)) {
            soulTool.getTag().putInt(SOUL_TOOL_TAG_LEVEL, SOUL_TOOL_MAX_LEVEL);
        }

        triggerMaxLevel(soulTool, playerEntity);
    }
}
